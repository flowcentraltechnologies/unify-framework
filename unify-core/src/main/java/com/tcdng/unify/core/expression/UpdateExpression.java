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
package com.tcdng.unify.core.expression;

import com.tcdng.unify.core.UnifyException;

/**
 * Used to specify fields to update with values.
 * 
 * @author The Code Department
 * @since 4.1
 */
public interface UpdateExpression {

	/**
	 * Appends SQL SET expression to supplied string builder.
	 * 
	 * @param sb         the string builder
	 * @param columnName the column name
	 * @throws UnifyException if an error occurs
	 */
	void appendSQLSetExpression(StringBuilder sb, String columnName) throws UnifyException;

	/**
	 * Gets the expression value.
	 * 
	 * @return the value
	 */
	Object getValue();
	
	/**
	 * Indicates is expression is custom.
	 * 
	 * @return true if custom otherwise false
	 */
	boolean isCustom();
}
