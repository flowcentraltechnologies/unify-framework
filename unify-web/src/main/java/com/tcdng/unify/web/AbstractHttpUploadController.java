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
package com.tcdng.unify.web;

import java.io.InputStream;
import java.io.StringWriter;

import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.stream.JsonObjectStreamer;
import com.tcdng.unify.web.http.HttpRequestHeaders;
import com.tcdng.unify.web.http.HttpUploadRequest;
import com.tcdng.unify.web.util.ContentDisposition;
import com.tcdng.unify.web.util.HttpUtils;

/**
 * Convenient base class for HTTP upload controllers.
 * 
 * @author The Code Department
 * @since 4.1
 */
public abstract class AbstractHttpUploadController extends AbstractUnifyComponent implements HttpUploadController {

	@Configurable
	private JsonObjectStreamer jsonObjectStreamer;

	@Override
	public String upload(HttpUploadRequest httpUploadRequest) throws UnifyException {
		final HttpRequestHeaders headers = httpUploadRequest.getHeaders();
		final ContentDisposition contentDisposition = HttpUtils.getUnifyContentDisposition(headers);
		return handleUpload(headers, contentDisposition, httpUploadRequest.getIn());
	}

	@Override
	protected void onInitialize() throws UnifyException {

	}

	@Override
	protected void onTerminate() throws UnifyException {

	}

	protected abstract String handleUpload(HttpRequestHeaders headers, ContentDisposition contentDisposition,
			InputStream in) throws UnifyException;

	protected final <T> T getObjectFromRequestJson(Class<T> jsonType, String json) throws UnifyException {
		return jsonObjectStreamer.unmarshal(jsonType, json);
	}

	protected final String getResponseJsonFromObject(Object obj) throws UnifyException {
		if (obj != null) {
			StringWriter sw = new StringWriter();
			jsonObjectStreamer.marshal(obj, sw);
			return sw.toString();
		}

		return null;
	}

}
