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
package com.tcdng.unify.web.ui;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.upl.UplComponent;
import com.tcdng.unify.web.ui.widget.Page;
import com.tcdng.unify.web.ui.widget.ResponseWriter;

/**
 * Component used to generate a page controller response.
 * 
 * @author The Code Department
 * @since 4.1
 */
public interface PageControllerResponse extends UplComponent {

	/**
	 * Generates response.
	 * 
	 * @param writer the response writer
	 * @param page   the page
	 * @throws UnifyException if an error occurs
	 */
	void generate(ResponseWriter writer, Page page) throws UnifyException;

	/**
	 * Returns true if whole document path.
	 * 
	 * @throws UnifyException if an error occurs
	 */
	boolean isDocumentPathResponse() throws UnifyException;
	
	/**
	 * Gets the page document path.
	 * @return the document path
	 * @throws UnifyException if an error occurs
	 */
	String getDocumentPath() throws UnifyException;
}
