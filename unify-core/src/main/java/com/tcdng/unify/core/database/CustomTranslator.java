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
package com.tcdng.unify.core.database;

import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.sql.SqlEntityInfo;
import com.tcdng.unify.core.database.sql.SqlFieldInfo;

/**
 * Custom translator component.
 * 
 * @author The Code Department
 * @since 4.1
 */
public interface CustomTranslator extends UnifyComponent {

	/**
	 * Gets associated native translator.
	 * 
	 * @return the associated translator otherwise null
	 * @throws UnifyException if an error occurs
	 */
	NativeTranslator getNativeTranslator() throws UnifyException;

	/**
	 * Translate custom expression
	 * 
	 * @param sb             the target appender
	 * @param sqlEntityInfo  the entity information
	 * @param toSqlFieldInfo the to field
	 * @param opSql          the SQL op
	 * @param expression     the custom expression
	 * @throws UnifyException if an error occurs
	 */
	void translateExpression(final StringBuilder sb, SqlEntityInfo sqlEntityInfo, SqlFieldInfo toSqlFieldInfo,
			String opSql, String expression) throws UnifyException;

}
