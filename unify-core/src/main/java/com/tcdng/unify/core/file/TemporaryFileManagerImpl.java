/*
 * Copyright (c) 2018-2025 The Code Department.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.tcdng.unify.core.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.stream.Stream;

import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Periodic;
import com.tcdng.unify.core.annotation.PeriodicType;
import com.tcdng.unify.core.annotation.Synchronized;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.business.AbstractBusinessService;
import com.tcdng.unify.core.constant.FrequencyUnit;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.IOInfo;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.RandomUtils;

/**
 * Temporary file manager implementation.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Transactional
@Component(ApplicationComponents.APPLICATION_TEMPORARY_FILE_MANAGER)
public class TemporaryFileManagerImpl extends AbstractBusinessService implements TemporaryFileManager {

	private static final String TEMP_SUBFOLDER = "unifytmp";

	@Configurable("60")
	private int minutesToExpire;
	
	private final FactoryMap<String, TempFile> tempfiles;

	private Path workingTempFolder;

	public TemporaryFileManagerImpl() {
		this.tempfiles = new FactoryMap<String, TempFile>() {

			@Override
			protected TempFile create(String tempFileId, Object... params) throws Exception {
				return new TempFile(tempFileId, Files.createTempFile(workingTempFolder, "worktmp-", ".tmp"), getNow());
			}

		};
	}

	@Override
	public boolean isTemporaryFileExists(String tempFileId) throws UnifyException {
		return tempfiles.isKey(tempFileId);
	}

	@Override
	public long getTemporaryFileSizeInBytes(String tempFileId) throws UnifyException {
		if (tempfiles.isKey(tempFileId)) {
			return tempfiles.get(tempFileId).getLength();
		}

		return 0;
	}

	@Override
	public String createTemporaryFile() throws UnifyException {
		return tempfiles.get(RandomUtils.generateUUIDInBase64()).getTempFileId();
	}

	@Override
	public IOInfo writeAllToTemporaryFile(InputStream in) throws UnifyException {
		final String tempFileId = createTemporaryFile();
		final long length = writeAllToTemporaryFile(tempFileId, in);
		return new IOInfo(tempFileId, length);
	}

	@Override
	public long writeAllToTemporaryFile(String tempFileId, InputStream in) throws UnifyException {
		if (tempfiles.isKey(tempFileId)) {
			final TempFile tempFile = tempfiles.get(tempFileId);
			try (OutputStream out = Files.newOutputStream(tempFile.getTempFile(), StandardOpenOption.WRITE)) {
				final long len = IOUtils.writeAll(out, in);
				tempFile.setLength(len);
				return len;
			} catch (UnifyException e) {
				throw e;
			} catch (Exception e) {
				throwOperationErrorException(e);
			}
		}

		return 0;
	}

	@Override
	public IOInfo writeAllToTemporaryFile(InputStream in, byte[] detect) throws UnifyException {
		final String tempFileId = createTemporaryFile();
		final long length = writeAllToTemporaryFile(tempFileId, in, detect);
		return new IOInfo(tempFileId, length);
	}

	@Override
	public long writeAllToTemporaryFile(String tempFileId, InputStream in, byte[] detect) throws UnifyException {
		if (tempfiles.isKey(tempFileId)) {
			final TempFile tempFile = tempfiles.get(tempFileId);
			try (OutputStream out = Files.newOutputStream(tempFile.getTempFile(), StandardOpenOption.WRITE)) {
				final long len = IOUtils.writeAll(out, in, detect);
				tempFile.setLength(len);
				return len;
			} catch (UnifyException e) {
				throw e;
			} catch (Exception e) {
				throwOperationErrorException(e);
			}
		}

		return 0;
	}

	@Override
	public long readAllFromTemporaryFile(String tempFileId, OutputStream out) throws UnifyException {
		if (tempfiles.isKey(tempFileId)) {
			final TempFile tempFile = tempfiles.get(tempFileId);
			try (InputStream in = Files.newInputStream(tempFile.getTempFile(), StandardOpenOption.READ)) {
				return IOUtils.writeAll(out, in);
			} catch (UnifyException e) {
				throw e;
			} catch (Exception e) {
				throwOperationErrorException(e);
			}
		}

		return 0;
	}

	@Override
	public boolean deleteTemporaryFile(String tempFileId) throws UnifyException {
		final TempFile tempFile = tempfiles.remove(tempFileId);
		if (tempFile != null) {
			try {
				Files.deleteIfExists(tempFile.getTempFile());
			} catch (IOException e) {
				throwOperationErrorException(e);
			}

			return true;
		}

		return false;
	}

	@Periodic(PeriodicType.ERA)
	@Synchronized("tempfilemanager-housekeeping")
	public void clearExpiredTemporaryFiles(TaskMonitor taskMonitor) throws UnifyException {
		final Date expiryDate = CalendarUtils.getDateWithFrequencyOffset(getNow(), FrequencyUnit.MINUTE,
				-minutesToExpire);
		// Clear cache
		for (TempFile tempFile : new ArrayList<TempFile>(tempfiles.values())) {
			if (tempFile.isExpired(expiryDate)) {
				deleteTemporaryFile(tempFile.getTempFileId());
			}
		}

		// Clear files
		final long expiryTime = expiryDate.getTime();
		try (Stream<Path> paths = Files.list(workingTempFolder)) {
			Iterator<Path> it = paths.iterator();
			while (it.hasNext()) {
				Path tempFile = it.next();
				try {
					if (Files.getLastModifiedTime(tempFile).toMillis() < expiryTime) {
						Files.deleteIfExists(tempFile);
					}
				} catch (IOException e) {
					logDebug(e);
				}
			}
		} catch (IOException e) {
			throwOperationErrorException(e);
		}
	}

	@Override
	protected void onInitialize() throws UnifyException {
		super.onInitialize();

		try {
			final Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
			workingTempFolder = Files.createDirectories(tempDir.resolve(TEMP_SUBFOLDER));
		} catch (IOException e) {
			throwOperationErrorException(e);
		}
	}

	private static class TempFile {

		private final String tempFileId;

		private final Path tempFile;

		private final Date createDate;

		private long length;
		
		public TempFile(String tempFileId, Path tempFile, Date createDate) {
			this.tempFileId = tempFileId;
			this.tempFile = tempFile;
			this.createDate = createDate;
		}

		public String getTempFileId() {
			return tempFileId;
		}

		public Path getTempFile() {
			return tempFile;
		}

		public boolean isExpired(Date expiryDate) {
			return createDate.before(expiryDate);
		}

		public long getLength() {
			return length;
		}

		public void setLength(long length) {
			this.length = length;
		}
	}
}
