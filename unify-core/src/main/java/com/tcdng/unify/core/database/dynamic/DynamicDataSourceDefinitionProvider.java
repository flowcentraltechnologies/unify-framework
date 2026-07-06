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
package com.tcdng.unify.core.database.dynamic;

import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Dynamic data source definition provider.
 * 
 * @author The Code Department
 * @since 4.1
 */
public interface DynamicDataSourceDefinitionProvider extends UnifyComponent {

	/**
	 * Provides dynamic data source definition.
	 * 
	 * @param dataSourceName the definition name
	 * @return the connection definition
	 * @throws UnifyException if an error occurs
	 */
	DynamicDataSourceDef provide(String dataSourceName) throws UnifyException;

	/**
	 * Checks if data source definition exists
	 * 
	 * @param dataSourceName the definition name
	 * @return true if exists otherwise false
	 * @throws UnifyException if an error occurs
	 */
	boolean exists(String dataSourceName) throws UnifyException;

}
