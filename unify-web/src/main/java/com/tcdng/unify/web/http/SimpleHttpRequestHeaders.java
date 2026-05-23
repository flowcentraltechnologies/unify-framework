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
package com.tcdng.unify.web.http;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

/**
 * Simple request header.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class SimpleHttpRequestHeaders implements HttpRequestHeaders {

	private Map<String, String> headers;

	public SimpleHttpRequestHeaders(Map<String, String> headers) {
		if (headers == null) {
			throw new IllegalArgumentException("Argument 'headers' can not be null;");
		}

		this.headers = headers;
	}

	@Override
	public String getHeader(String name) {
		return headers.get(name);
	}

	@Override
	public Enumeration<String> getNames() {
		return Collections.enumeration(headers.keySet());
	}

}
