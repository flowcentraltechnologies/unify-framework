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
package com.tcdng.unify.web.http;

/**
 * HTTP request header constants.
 * 
 * @author The Code Department
 * @since 4.1
 */
public interface HttpRequestHeaderConstants {
	
	String AUTHORIZATION = "Authorization";
	
	String HOST = "Host";
	
	String ORIGIN = "Origin";
	
	String REFERER = "Referer";
	
	String PROXY_CLIENT_IP = "Proxy-Client-IP";
	
	String USER_AGENT = "User-Agent";
	
	String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
	
	String X_FORWARDED_FOR = "X-FORWARDED-FOR";
	
	String UNIFY_PID= "Unify-Pid";
}
