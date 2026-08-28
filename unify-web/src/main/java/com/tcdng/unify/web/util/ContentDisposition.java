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
package com.tcdng.unify.web.util;

/**
 * Content disposition
 * 
 * @author The Code Department
 * @since 4.1
 */
public class ContentDisposition {

	private String type;

	private String fileName;

	private long fileSize;

	public ContentDisposition(String type, String fileName, long fileSize) {
		this.type = type;
		this.fileName = fileName;
		this.fileSize = fileSize;
	}

	public ContentDisposition(String type, String fileName) {
		this.type = type;
		this.fileName = fileName;
	}
	
	public ContentDisposition(ContentDisposition cd, long fileSize) {
		this.type = cd.type;
		this.fileName = cd.fileName;
		this.fileSize = fileSize;
	}


	public String getType() {
		return type;
	}

	public String getFileName() {
		return fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public boolean isAttachment() {
		return "attachment".equals(type);
	}

	public boolean isInline() {
		return "inline".equals(type);
	}

	@Override
	public String toString() {
		return "ContentDisposition [type=" + type + ", fileName=" + fileName + ", fileSize=" + fileSize + "]";
	}
}