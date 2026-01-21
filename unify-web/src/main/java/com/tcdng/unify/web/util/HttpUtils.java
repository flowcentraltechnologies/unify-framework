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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UnifyOperationException;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.http.HttpPart;
import com.tcdng.unify.web.http.HttpRequestHeaderConstants;
import com.tcdng.unify.web.http.HttpRequestHeaders;

/**
 * HTTP utilities.
 * 
 * @author The Code Department
 * @since 4.1
 */
public final class HttpUtils {

	private static final String CONTENT_DISPOSITION = "content-disposition";
	private static final String DISPOSITION_FILENAME = "filename";
	private static final String DISPOSITION_CREATIONDATE = "creation-date";
	private static final String DISPOSITION_MODIFICATIONDATE = "modification-date";

	private static final String DATETIME_FORMAT = "ddMMyyyy HH:mm:ss";
	
    private HttpUtils() {
        
    }

	public static String getUploadHeader(ContentDisposition contentDisposition) {
		final SimpleDateFormat format = new SimpleDateFormat(DATETIME_FORMAT);
		return contentDisposition.getFileName() + ";" + format.format(contentDisposition.getCreationDate()) + ";"
				+ format.format(contentDisposition.getModificationDate());
	}
    
	public static ContentDisposition getUnifyContentDisposition(HttpRequestHeaders headers)
			throws UnifyException {
		ContentDisposition contentDisposition = null;
		final String upload = headers.getHeader(HttpRequestHeaderConstants.X_UNIFY_UPLOAD);
		if (!StringUtils.isBlank(upload)) {
			try {
				final SimpleDateFormat format = new SimpleDateFormat(DATETIME_FORMAT);
				final String[] parts = upload.split(";");
				final String fileName = parts[0];
				final Date creationDate = format.parse(parts[1]);
				final Date modificationDate = format.parse(parts[2]);
				contentDisposition = new ContentDisposition(fileName, creationDate, modificationDate);
			} catch (ParseException e) {
				throw new UnifyOperationException(e);
			}
		}

		return contentDisposition;
	}
    
	public static ContentDisposition getContentDisposition(HttpPart part) throws UnifyException {
		String fileName = null;
		Date creationDate = null;
		Date modificationDate = null;
		for (String disposition : part.getHeader(CONTENT_DISPOSITION).split(";")) {
			if (disposition.trim().startsWith(DISPOSITION_FILENAME)) {
				fileName = disposition.substring(disposition.indexOf('=') + 1).trim().replace("\"", "");
				continue;
			}

			if (disposition.trim().startsWith(DISPOSITION_CREATIONDATE)) {
				creationDate = CalendarUtils
						.parseRfc822Date(disposition.substring(disposition.indexOf('=') + 1).trim());
				continue;
			}

			if (disposition.trim().startsWith(DISPOSITION_MODIFICATIONDATE)) {
				modificationDate = CalendarUtils
						.parseRfc822Date(disposition.substring(disposition.indexOf('=') + 1).trim());
				continue;
			}
		}

		return new ContentDisposition(fileName, creationDate, modificationDate);
	}
}
