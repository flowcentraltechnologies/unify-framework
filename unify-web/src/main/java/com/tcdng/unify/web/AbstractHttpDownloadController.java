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

import java.io.OutputStream;
import java.io.StringWriter;

import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.stream.JsonObjectStreamer;
import com.tcdng.unify.web.http.HttpDownloadRequest;
import com.tcdng.unify.web.http.HttpRequestHeaders;

/**
 * Convenient base class for HTTP download controllers.
 * 
 * @author The Code Department
 * @since 4.1
 */
public abstract class AbstractHttpDownloadController extends AbstractUnifyComponent implements HttpDownloadController {

	@Configurable
	private JsonObjectStreamer jsonObjectStreamer;

	@Override
	public void download(HttpDownloadRequest httpDownloadRequest) throws UnifyException {
		handleDownload(httpDownloadRequest.getHeaders(), httpDownloadRequest.getOut());
	}

	@Override
	protected void onInitialize() throws UnifyException {

	}

	@Override
	protected void onTerminate() throws UnifyException {

	}

	protected abstract void handleDownload(HttpRequestHeaders headers, OutputStream out) throws UnifyException;

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
