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

import java.io.StringWriter;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.MimeType;
import com.tcdng.unify.core.stream.JsonObjectStreamer;
import com.tcdng.unify.core.util.json.JsonUtils;

/**
 * Abstract plain JSON controller.
 * 
 * @author The Code Department
 * @since 4.1
 */
public abstract class AbstractPlainJsonController extends AbstractPlainController {

	@Configurable
	private JsonObjectStreamer jsonObjectStreamer;

	@Override
	public void doProcess(ClientRequest request, ClientResponse response) throws UnifyException {
		response.setContentType(MimeType.APPLICATION_JSON.template());
		String jsonResponse = null;

		try {
			final String actionName = request.getRequestPathParts().getControllerPathParts().getActionName();
			logDebug("Processing plain JSON request with action [{0}]...", actionName);
			jsonResponse = doExecute(actionName, request.getText());
		} catch (Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("{ \"serverError\":");
			JsonUtils.write(sb, e.getMessage());
			sb.append("}");
			jsonResponse = sb.toString();
		}

		if (jsonResponse != null) {
			try {
				response.getWriter().write(jsonResponse);
				response.getWriter().flush();
			} catch (UnifyException e) {
				throw e;
			}
		}
	}

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

	protected abstract String doExecute(String actionName, String jsonRequest) throws UnifyException;
}
