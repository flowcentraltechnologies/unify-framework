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
package com.tcdng.unify.core.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UnifyOperationException;
import com.tcdng.unify.core.util.FileUtils;
import com.tcdng.unify.core.util.IOUtils;

/**
 * Data object that represents an uploaded file.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class UploadedFile {

	public static final UploadedFile BLANK = new UploadedFile();
	
	private String filename;

	private Date creationDate;

	private Date modificationDate;

	private String tempFileId;

	private long fileSize;

	private byte[] detect;
	
	private final boolean usesTempFile;
	
	private InputStream in;
	
	private OutputStream out;
	
	public static UploadedFile create(String filename, byte[] in) throws UnifyException {
		final Date now = new Date();
		return new UploadedFile(filename, now, now, new ByteArrayInputStream(in), false);
	}
	
	public static UploadedFile create(String filename, InputStream in) throws UnifyException {
		final Date now = new Date();
		return new UploadedFile(filename, now, now, in, false);
	}
	
	public static UploadedFile create(String filename) throws UnifyException {
		final Date now = new Date();
		return new UploadedFile(filename, now, now, null, true);
	}
	
	public static UploadedFile createUsingTempFile(String filename, Date creationDate, Date modificationDate,
			InputStream in) throws UnifyException {
		return new UploadedFile(filename, creationDate, modificationDate, in, true);
	}
	
	private UploadedFile(String filename, Date creationDate, Date modificationDate, InputStream in,
			boolean usesTempFile) throws UnifyException {
		this.detect = new byte[4];
		this.filename = filename;
		this.creationDate = creationDate;
		this.modificationDate = modificationDate;
		this.usesTempFile = usesTempFile;
		if (usesTempFile) {
			if (in != null) {
				final IOInfo ioInfo = FileUtils.writeAllToTemporaryFile(in, detect);
				this.tempFileId = ioInfo.getFileId();
				this.fileSize = ioInfo.getFileLength();
			} else {
				this.tempFileId = FileUtils.createTemporaryFile();
			}
		} else {
			this.in = in;
		}
	}

	private UploadedFile() {
		this.usesTempFile = false;
	}
	
	public OutputStream getOut() throws UnifyException {
		if (usesTempFile && tempFileId != null && out == null) {
			out = FileUtils.openTemporaryFile(tempFileId);
		}
		
		return out;
	}
	
	public void closeOut() throws UnifyException {
		IOUtils.close(out);
	}
	
	public String getFilename() {
		return filename;
	}

	public long getFileSize() {
		return fileSize;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public boolean isUsesTempFile() {
		return usesTempFile;
	}

	public boolean isPresent() {
		return (usesTempFile && tempFileId != null) || (!usesTempFile && in != null);
	}
	
	public byte[] getDetect() {
		final byte[] _detect = new byte[4];
		System.arraycopy(detect, 0, _detect, 0, 4);
		return _detect;
	}

	/**
	 * Gets the size of this upload file.
	 * 
	 * @return the size in bytes
	 * @throws UnifyException if an error occurs
	 */
	public long size() throws UnifyException {
		return fileSize;
	}
	
	/**
	 * Writes this upload file to output stream.
	 * 
	 * @param out the output stream
	 * @throws UnifyException if an error occurs
	 */
	public long writeAll(OutputStream out) throws UnifyException {
		if (usesTempFile) {
			return FileUtils.readAllFromTemporaryFile(getTempFileId(), out);
		}

		try {
			return IOUtils.writeAll(out, getIn());
		} finally {
			invalidate();
		}
	}

	/**
	 * Writes this upload file to output stream.
	 * 
	 * @param out the output stream
	 * @throws UnifyException if an error occurs
	 */
	public long writeAllAndInvalidate(OutputStream out) throws UnifyException {
		try {
			return writeAll(out);
		} finally {
			invalidate();
		}
	}

	/**
	 * Gets this upload file into byte array. (Not recommended for large files)
	 * 
	 * @return the file bytes
	 * @throws UnifyException if an error occurs
	 */
	public byte[] getData() throws UnifyException {
		if (usesTempFile) {
			ByteArrayOutputStream baos = null;
			FileUtils.readAllFromTemporaryFile(getTempFileId(), baos = new ByteArrayOutputStream());
			return baos.toByteArray();
		}

		try {
			return IOUtils.readAll(getIn());
		} finally {
			invalidate();
		}
	}

	/**
	 * Gets this upload file into byte array. (Not recommended for large files)
	 * 
	 * @return the file bytes
	 * @throws UnifyException if an error occurs
	 */
	public byte[] getDataAndInvalidate() throws UnifyException {
		try {
			return getData();
		} finally {
			invalidate();
		}
	}

	/**
	 * Invalidates this uploaded file
	 * 
	 * @throws UnifyException if an error occurs
	 */
	public void invalidate() throws UnifyException {
		if (tempFileId != null) {
			FileUtils.deleteTemporaryFile(tempFileId);
			tempFileId = null;
		}

		if (in != null) {
			in = null;
		}

		if (out != null) {
			IOUtils.close(out);
			out = null;
		}
	}

	private String getTempFileId() throws UnifyException {
		if (tempFileId == null) {
			throw new UnifyOperationException("Uploaded file is already invalidated.");
		}

		return tempFileId;
	}

	public InputStream getIn() throws UnifyException {
		if (in == null) {
			throw new UnifyOperationException("Uploaded file is already invalidated.");
		}

		InputStream _in = in;
		in = null;
		return _in;
	}
}
