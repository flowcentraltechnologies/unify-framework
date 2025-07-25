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
import com.tcdng.unify.core.stream.XmlObjectStreamer;

/**
 * Abstract plain XML controller.
 * 
 * @author The Code Department
 * @since 4.1
 */
public abstract class AbstractPlainXmlController extends AbstractPlainController {

	@Configurable
	private XmlObjectStreamer xmlObjectStreamer;

	@Override
	public void doProcess(ClientRequest request, ClientResponse response) throws UnifyException {
		response.setContentType(MimeType.APPLICATION_XML.template());
		String xmlResponse = null;
		try {
			final String actionName = request.getRequestPathParts().getControllerPathParts().getActionName();
			logDebug("Processing plain XML request with action [{0}]...", actionName);
			xmlResponse = doExecute(actionName, request.getText());
		} catch (Exception e) {
			xmlResponse = "<serverError>" + e.getMessage() + "</serverError>";
		}
		
		if (xmlResponse != null) {
			try {
				response.getWriter().write(xmlResponse);
				response.getWriter().flush();
			} catch (UnifyException e) {
				throw e;
			}
		}
	}

	protected final <T> T getObjectFromRequestXml(Class<T> xmlType, String xml) throws UnifyException {
		return xmlObjectStreamer.unmarshal(xmlType, xml);
	}

	protected final String getResponseXmlFromObject(Object obj) throws UnifyException {
		if (obj != null) {
			StringWriter sw = new StringWriter();
			xmlObjectStreamer.marshal(obj, sw);
			return sw.toString();
		}

		return null;
	}

	protected abstract String doExecute(String actionName, String xmlRequest) throws UnifyException;
}
