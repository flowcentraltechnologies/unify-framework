/*
 * Copyright (c) 2018-2026 The Code Department.
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

import com.tcdng.unify.core.constant.FileAttachmentType;

/**
 * Uploaded file preview.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class UploadedFilePreview {

	private FileAttachmentType type;

	private String fileName;

	private String fileTitle;

	private String fileSize;

	private long fileLength;

	private UploadedFile downloadFile;

	public UploadedFilePreview(FileAttachmentType type, String fileName, String fileTitle, String fileSize,
			long fileLength, UploadedFile downloadFile) {
		this.type = type;
		this.fileName = fileName;
		this.fileTitle = fileTitle;
		this.fileSize = fileSize;
		this.fileLength = fileLength;
		this.downloadFile = downloadFile;
	}

	public FileAttachmentType getType() {
		return type;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileTitle() {
		return fileTitle;
	}

	public String getFileSize() {
		return fileSize;
	}

	public long getFileLength() {
		return fileLength;
	}

	public UploadedFile getDownloadFile() {
		return downloadFile;
	}

}
