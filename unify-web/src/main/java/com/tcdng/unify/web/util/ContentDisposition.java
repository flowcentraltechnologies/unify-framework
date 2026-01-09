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
package com.tcdng.unify.web.util;

import java.util.Date;

/**
 * Content disposition
 * 
 * @author The Code Department
 * @since 4.1
 */
public class ContentDisposition {

	private String fileName;

	private Date creationDate;

	private Date modificationDate;

	public ContentDisposition(String fileName, Date creationDate, Date modificationDate) {
		this.fileName = fileName;
		this.creationDate = creationDate;
		this.modificationDate = modificationDate;
	}

	public String getFileName() {
		return fileName;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public boolean isFileName() {
		return fileName != null;
	}
}