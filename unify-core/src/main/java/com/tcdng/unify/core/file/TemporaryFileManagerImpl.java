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

import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.business.AbstractBusinessService;
import com.tcdng.unify.core.data.FactoryMap;
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

	private final FactoryMap<String, TempFile> tempfiles;

	private Path workingTempFolder;

	public TemporaryFileManagerImpl() {
		this.tempfiles = new FactoryMap<String, TempFile>() {

			@Override
			protected TempFile create(String tempFileId, Object... params) throws Exception {
				return new TempFile(tempFileId, Files.createTempFile(workingTempFolder, "worktmp-", ".tmp"));
			}

		};
	}

	@Override
	public boolean isTemporaryFileExists(String tempFileId) throws UnifyException {
		return tempfiles.isKey(tempFileId);
	}

	@Override
	public String createTemporaryFile() throws UnifyException {
		return tempfiles.get(RandomUtils.generateUUIDInBase64()).getTempFileId();
	}

	@Override
	public String writeAllToTemporaryFile(InputStream in) throws UnifyException {
		final String tempFileId = createTemporaryFile();
		writeAllToTemporaryFile(tempFileId, in);
		return tempFileId;
	}

	@Override
	public boolean writeAllToTemporaryFile(String tempFileId, InputStream in) throws UnifyException {
		if (tempfiles.isKey(tempFileId)) {
			final TempFile tempFile = tempfiles.get(tempFileId);
			try (OutputStream out = Files.newOutputStream(tempFile.getTempFile(), StandardOpenOption.WRITE)) {
				IOUtils.writeAll(out, in);
			} catch (UnifyException e) {
				throw e;
			} catch (Exception e) {
				throwOperationErrorException(e);
			}

			return true;
		}

		return false;
	}

	@Override
	public boolean readAllFromTemporaryFile(String tempFileId, OutputStream out) throws UnifyException {
		if (tempfiles.isKey(tempFileId)) {
			final TempFile tempFile = tempfiles.get(tempFileId);
			try (InputStream in = Files.newInputStream(tempFile.getTempFile(), StandardOpenOption.READ)) {
				IOUtils.writeAll(out, in);
			} catch (UnifyException e) {
				throw e;
			} catch (Exception e) {
				throwOperationErrorException(e);
			}

			return true;
		}

		return false;
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

		public TempFile(String tempFileId, Path tempFile) {
			this.tempFileId = tempFileId;
			this.tempFile = tempFile;
		}

		public String getTempFileId() {
			return tempFileId;
		}

		public Path getTempFile() {
			return tempFile;
		}

	}
}
