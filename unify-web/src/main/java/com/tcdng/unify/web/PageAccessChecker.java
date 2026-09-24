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

import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Page access checker component.
 * 
 * @author The Code Department
 * @since 4.1
 */
public interface PageAccessChecker extends UnifyComponent {

	/**
	 * Check if role has access to page path.
	 * 
	 * @param roleCode the role code
	 * @param pagePath the page path
	 * @return true is accessible otherwise false
	 * @throws UnifyException if an error occurs
	 */
	boolean isPageAccessible(String roleCode, String pagePath) throws UnifyException;

	/**
	 * Invalidate current role code path access list.
	 * 
	 * @param roleCode the role code
	 * @throws UnifyException if an error occurs
	 */
	void invalidateRole(String roleCode) throws UnifyException;

}
