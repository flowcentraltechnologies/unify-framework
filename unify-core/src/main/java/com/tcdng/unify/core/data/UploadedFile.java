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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UnifyOperationException;
import com.tcdng.unify.core.util.FileUtils;

/**
 * Data object that represents an uploaded file.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class UploadedFile {

	private String filename;

	private Date creationDate;

	private Date modificationDate;

	private String tempFileId;

	private byte[] detect;
	
	public UploadedFile(String filename, Date creationDate, Date modificationDate, InputStream in)
			throws UnifyException {
		this.detect = new byte[4];
		this.filename = filename;
		this.creationDate = creationDate;
		this.modificationDate = modificationDate;
		this.tempFileId = FileUtils.writeAllToTemporaryFile(in, detect);
	}

	public String getFilename() {
		return filename;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Date getModificationDate() {
		return modificationDate;
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
		return FileUtils.getTemporaryFileSizeInBytes(getTempFileId());
	}
	
	/**
	 * Writes this upload file to output stream.
	 * 
	 * @param out the output stream
	 * @throws UnifyException if an error occurs
	 */
	public long writeAll(OutputStream out) throws UnifyException {
		return FileUtils.readAllFromTemporaryFile(getTempFileId(), out);
	}

	/**
	 * Gets this upload file into byte array. (Not recommended for large files)
	 * 
	 * @return the file bytes
	 * @throws UnifyException if an error occurs
	 */
	public byte[] getData() throws UnifyException {
		ByteArrayOutputStream baos = null;
		FileUtils.readAllFromTemporaryFile(getTempFileId(), baos = new ByteArrayOutputStream());
		return baos.toByteArray();
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
	}

	private String getTempFileId() throws UnifyException {
		if (tempFileId == null) {
			throw new UnifyOperationException("Uploaded file is already invalidated.");
		}

		return tempFileId;
	}
}
