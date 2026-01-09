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

import java.io.InputStream;
import java.io.OutputStream;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.business.BusinessService;

/**
 * Temporary file manager.
 * 
 * @author The Code Department
 * @since 4.1
 */
public interface TemporaryFileManager extends BusinessService {

	/**
	 * Checks if temporary file exists.
	 * 
	 * @param tempFileId the temporary file's ID
	 * @return true if temporary file exists otherwise false
	 * @throws UnifyException if an error occurs
	 */
	boolean isTemporaryFileExists(String tempFileId) throws UnifyException;

	/**
	 * Creates a temporary file.
	 * 
	 * @return the temporary file ID
	 * @throws UnifyException if an error occurs
	 */
	String createTemporaryFile() throws UnifyException;

	/**
	 * Writes all of input stream to a new temporary file.
	 * 
	 * @param in the input stream
	 * @return the temporary file ID
	 * @throws UnifyException if an error occurs
	 */
	String writeAllToTemporaryFile(InputStream in) throws UnifyException;

	/**
	 * Writes all of input stream to temporary file.
	 * 
	 * @param tempFileId the temporary file's ID
	 * @param in         the input stream
	 * @return true if temporary file exists and written to otherwise false
	 * @throws UnifyException if an error occurs
	 */
	boolean writeAllToTemporaryFile(String tempFileId, InputStream in) throws UnifyException;

	/**
	 * Reads all of a temporary file to supplied output stream.
	 * 
	 * @param tempFileId the temporary file's ID
	 * @param out        the output stream
	 * @return true if temporary file exists and read from otherwise false
	 * @throws UnifyException if an error occurs
	 */
	boolean readAllFromTemporaryFile(String tempFileId, OutputStream out) throws UnifyException;

	/**
	 * Delete a temporary file.
	 * 
	 * @return true if temporary file exists and deleted otherwise false
	 * @throws UnifyException if an error occurs
	 */
	boolean deleteTemporaryFile(String tempFileId) throws UnifyException;
}
