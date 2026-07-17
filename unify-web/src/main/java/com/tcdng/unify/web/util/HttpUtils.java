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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.core.util.json.JsonUtils;
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

	private static final String CONTENT_DISPOSITION = "Content-Disposition";
	
    private HttpUtils() {
        
    }

	public static final String getJsonErrorResponse(Exception e) {
		StringBuilder sb = new StringBuilder();
		sb.append("{ \"serverError\":");
		JsonUtils.write(sb, e.getMessage());
		sb.append("}");
		return sb.toString();
	}

	public static String getUnifyContentDisposition(ContentDisposition contentDisposition) {
		return contentDisposition.getType() + "]" + contentDisposition.getFileName() + "]"
				+ String.valueOf(contentDisposition.getFileSize());
	}
    
	public static Optional<ContentDisposition> getUnifyContentDisposition(HttpRequestHeaders headers)
			throws UnifyException {
		final String upload = headers.getHeader(HttpRequestHeaderConstants.X_UNIFY_DISPOSITION);
		if (!StringUtils.isBlank(upload)) {
			final String[] parts = upload.split("]");
			if (parts.length == 3) {
				return Optional.of(new ContentDisposition(parts[0], parts[1], Long.parseLong(parts[2])));
			}
		}

		return Optional.empty();
	}
    

	public static Optional<ContentDisposition> getHttpFileContentDisposition(HttpPart part) throws UnifyException {
		final String disposition = part.getHeader(CONTENT_DISPOSITION);
		if (disposition != null) {
			String[] parts = StringUtils.charSplitQuoted(disposition, ';');
			final String type = parts[0].trim();
			String extended = null;
			String plain = null;

			for (int i = 1; i < parts.length; i++) {
				final String entry = parts[i].trim();
				int index = entry.indexOf('=');
				if (index > 0) {
					String name = entry.substring(0, index).trim().toLowerCase();
					String val = StringUtils.unquote(entry.substring(index + 1).trim());

					if (name.equals("filename*")) {
						extended = decodeExtendedVal(val);
					} else if (name.equals("filename")) {
						plain = val;
					}
				}
			}

			String filename = !StringUtils.isBlank(extended) ? extended : plain;
			if (!StringUtils.isBlank(filename)) {
				return Optional.of(new ContentDisposition(type, filename));
			}
		}

		return Optional.empty();
	}
	
	private static String decodeExtendedVal(String val) {
		int first = val.indexOf('\'');
		int second = first >= 0 ? val.indexOf('\'', first + 1) : -1;
		if (first < 0 || second < 0) {
			return val;
		}

		String charset = val.substring(0, first);
		String encoded = val.substring(second + 1);
		try {
			return URLDecoder.decode(encoded, charset.isBlank() ? StandardCharsets.UTF_8.name() : charset);
		} catch (UnsupportedEncodingException e) {
			return encoded;
		}
	}
}
