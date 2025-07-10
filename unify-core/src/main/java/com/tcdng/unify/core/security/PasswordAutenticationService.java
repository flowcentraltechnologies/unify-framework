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
package com.tcdng.unify.core.security;

import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * A password authentication service component.
 * 
 * @author The Code Department
 * @since 4.1
 */
public interface PasswordAutenticationService extends UnifyComponent {

	/**
	 * Authenticates a user with supplied password.
	 * 
	 * @param userName  the user name to authenticate
	 * @param userEmail the user email
	 * @param password  the password
	 * @return a true is returned if supplied credentials are successfully
	 *         authenticated
	 * @throws UnifyException if an error occurs
	 */
	boolean authenticate(String userName, String userEmail, String password) throws UnifyException;

	/**
	 * Gets the user profile
	 * 
	 * @param userName the user name the user name to authenticate
	 * @return the user profile
	 * @throws UnifyException if an error occurs
	 */
	UserProfile getUserProfile(String userName) throws UnifyException;
}
