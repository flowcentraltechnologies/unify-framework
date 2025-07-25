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
package com.tcdng.unify.web.constant;

/**
 * HTTP response constants.
 * 
 * @author The Code Department
 * @version 1.0
 */
public interface HttpResponseConstants {

	int OK = 200;

	int CREATED = 201;

	int BAD_REQUEST = 400;

	int UNAUTHORIZED = 401;

	int NOT_FOUND = 404;

	int METHOD_NOT_ALLOWED = 405;

	int NOT_ACCEPTABLE = 406;

	int INTERNAL_SERVER_ERROR = 500;
}
