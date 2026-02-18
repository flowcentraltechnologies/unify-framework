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
package com.tcdng.unify.core.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UnifyOperationException;
import com.tcdng.unify.core.constant.FileAttachmentType;
import com.tcdng.unify.core.data.FileAttachmentInfo;
import com.tcdng.unify.core.data.IOInfo;
import com.tcdng.unify.core.data.UploadedFile;
import com.tcdng.unify.core.file.TemporaryFileManager;
import com.tcdng.unify.core.util.ImageUtils.ImageType;

/**
 * File utilities.
 * 
 * @author The Code Department
 * @version 1.0
 */
public final class FileUtils {

	private static TemporaryFileManager tempFileManager;
	
	private FileUtils() {

	}

	public static void init(TemporaryFileManager temporaryFileManager) {
		if (tempFileManager == null) {
			tempFileManager = temporaryFileManager;
		}
	}
	
	/**
	 * Checks if temporary file exists.
	 * 
	 * @param tempFileId the temporary file's ID
	 * @return true if temporary file exists otherwise false
	 * @throws UnifyException if an error occurs
	 */
	public static boolean isTemporaryFileExists(String tempFileId) throws UnifyException {
		return temp().isTemporaryFileExists(tempFileId);
	}

	/**
	 * Gets temporary file size.
	 * 
	 * @param tempFileId the temporary file's ID
	 * @return the size if temporary file exists otherwise zero
	 * @throws UnifyException if an error occurs
	 */
	public static long getTemporaryFileSizeInBytes(String tempFileId) throws UnifyException {
		return temp().getTemporaryFileSizeInBytes(tempFileId);
	}

	/**
	 * Creates a temporary file.
	 * 
	 * @return the temporary file ID
	 * @throws UnifyException if an error occurs
	 */
	public static String createTemporaryFile() throws UnifyException {
		return temp().createTemporaryFile();
	}

	/**
	 * Opens temporary file
	 * 
	 * @param tempFileId the temporary file ID
	 * @return the output stream
	 * @throws UnifyException if an error occurs
	 */
	public static OutputStream openTemporaryFile(String tempFileId) throws UnifyException {
		return temp().openTemporaryFile(tempFileId);
	}

	/**
	 * Writes all of input stream to a new temporary file.
	 * 
	 * @param in the input stream
	 * @return the temporary IO information
	 * @throws UnifyException if an error occurs
	 */
	public static IOInfo writeAllToTemporaryFile(InputStream in) throws UnifyException {
		return temp().writeAllToTemporaryFile(in);
	}

	/**
	 * Writes all of input stream to a new temporary file.
	 * 
	 * @param in     the input stream
	 * @param detect the files first 4 bytes
	 * @return the temporary IO information
	 * @throws UnifyException if an error occurs
	 */
	public static IOInfo writeAllToTemporaryFile(InputStream in, byte[] detect) throws UnifyException {
		return temp().writeAllToTemporaryFile(in, detect);
	}

	/**
	 * Writes all of input stream to temporary file.
	 * 
	 * @param tempFileId the temporary file's ID
	 * @param in         the input stream
	 * @return the length read in bytes otherwise zero
	 * @throws UnifyException if an error occurs
	 */
	public static long writeAllToTemporaryFile(String tempFileId, InputStream in) throws UnifyException {
		return temp().writeAllToTemporaryFile(tempFileId, in);
	}

	/**
	 * Reads all of a temporary file to supplied output stream.
	 * 
	 * @param tempFileId the temporary file's ID
	 * @param out        the output stream
	 * @return the length read in bytes otherwise zero
	 * @throws UnifyException if an error occurs
	 */
	public static long readAllFromTemporaryFile(String tempFileId, OutputStream out) throws UnifyException {
		return temp().readAllFromTemporaryFile(tempFileId, out);
	}

	/**
	 * Delete a temporary file.
	 * 
	 * @return true if temporary file exists and deleted otherwise false
	 * @throws UnifyException if an error occurs
	 */
	public static boolean deleteTemporaryFile(String tempFileId) throws UnifyException {
		return temp().deleteTemporaryFile(tempFileId);
	}
	
	/**
	 * Detects file attachment type from supplied file attachment information.
	 * 
	 * @param fileAttachmentInfo the file attachment information
	 * @return the file attachment type
	 */
	public static FileAttachmentType detectFileAttachmentType(FileAttachmentInfo fileAttachmentInfo) {
		FileAttachmentType type = FileUtils.detectFileAttachmentType(fileAttachmentInfo.getFilename());
		return type.isWildCard() ? FileUtils.detectFileAttachmentType(fileAttachmentInfo.getAttachment()) : type;
	}

	/**
	 * Detects file attachment type from supplied file name.
	 * 
	 * @param fileName the file name
	 * @return the file attachment type
	 */
	public static FileAttachmentType detectFileAttachmentType(String fileName) {
		return FileAttachmentType.detectFromFileName(fileName);
	}
	/**
	 * Detects file attachment type from supplied file
	 * 
	 * @param file the file to check
	 * @return the file attachment type
	 */
	public static FileAttachmentType detectFileAttachmentType(UploadedFile file) {
		return FileUtils.detectFileAttachmentType(file.getDetect());
	}
	
	/**
	 * Detects file attachment type from supplied file
	 * 
	 * @param file the file to check
	 * @return the file attachment type
	 */
	public static FileAttachmentType detectFileAttachmentType(byte[] file) {
		if (file != null) {
			// Image
			final ImageType imageType = ImageUtils.detectImageType(file);
			if (imageType != null) {
				switch (imageType) {
				case BMP:
					return FileAttachmentType.IMAGE_BMP;
				case GIF:
					return FileAttachmentType.IMAGE_GIF;
				case JPEG:
					return FileAttachmentType.IMAGE_JPG;
				case PNG:
					return FileAttachmentType.IMAGE_PNG;
				default:
					break;
				}
			}

			// PDF
			if ((file.length > 4) && (file[0] == (byte) 0x25) && (file[1] == (byte) 0x50) && (file[2] == (byte) 0x44)
					&& (file[3] == (byte) 0x46)) {
				return FileAttachmentType.PDF;
			}
		}

		return null;
	}
	
	private static final Set<String> THEMABLES = Collections
			.unmodifiableSet(new HashSet<String>(Arrays.asList(".css",".jpg", ".jpeg", ".png", ".gif", ".bmp")));
	
	public static String detectPresentAndGetThemeFileName(final String fileName, final String theme,
			final String workingPath) {
		if (fileName != null) {
			int lastIndex = fileName.lastIndexOf('.');
			if (lastIndex >= 0) {
				final String ext = fileName.substring(lastIndex);
				if (StringUtils.isNotBlank(theme) && THEMABLES.contains(ext.toLowerCase())) {
					final String _fileName = fileName.substring(0, lastIndex) + "-" + theme + ext;
					if (IOUtils.isResourceFileInstance(_fileName, workingPath)) {
						return _fileName;
					}
				}
				
				if (IOUtils.isResourceFileInstance(fileName, workingPath)) {
					return fileName;
				}
				
				final String _fallFileName = fileName.substring(0, lastIndex) + "-fallback" + ext;
				if (IOUtils.isResourceFileInstance(_fallFileName, workingPath)) {
					return _fallFileName;
				}
			}
		}

		return fileName;
	}
	
	private static TemporaryFileManager temp() throws UnifyException {
		if (tempFileManager == null) {
			throw new UnifyOperationException("FileUtils not properly initialized with a temporary file manager.");
		}

		return tempFileManager;
	}

}
