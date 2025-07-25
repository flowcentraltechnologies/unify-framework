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

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.MimeType;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.data.ErrorPart;
import com.tcdng.unify.web.data.Response;

/**
 * Convenient base class for JSON CRUD controllers.
 * 
 * @author The Code Department
 * @since 4.1
 */
public abstract class AbstractJsonCRUDController extends AbstractHttpCRUDController {

	public AbstractJsonCRUDController() {
		super(MimeType.APPLICATION_JSON.template());
	}

	@Override
	protected final Response getErrorResponse(int status, String errorText, String errorMsg) throws UnifyException {
		return new Response(status, DataUtils.asJsonString(new ErrorPart(errorText, errorMsg)));
	}

}
