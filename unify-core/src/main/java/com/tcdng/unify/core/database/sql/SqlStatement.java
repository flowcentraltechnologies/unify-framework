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
package com.tcdng.unify.core.database.sql;

import java.util.Collections;
import java.util.List;

/**
 * SQL statement information.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class SqlStatement {

	private SqlEntityInfo sqlEntityInfo;

	private SqlStatementType type;

	private String sql;

	private List<SqlParameter> parameterInfoList;

	private List<SqlResult> resultInfoList;

	private boolean merge;

	private boolean lenient;

	public SqlStatement(SqlEntityInfo sqlEntityInfo, SqlStatementType type, String sql) {
		this.sqlEntityInfo = sqlEntityInfo;
		this.type = type;
		this.sql = sql;
		this.parameterInfoList = Collections.emptyList();
		this.resultInfoList = Collections.emptyList();
	}

	public SqlStatement(SqlEntityInfo sqlEntityInfo, SqlStatementType type, String sql,
			final List<SqlParameter> parameterInfoList) {
		this.sqlEntityInfo = sqlEntityInfo;
		this.type = type;
		this.sql = sql;
		this.parameterInfoList = parameterInfoList;
		this.resultInfoList = Collections.emptyList();
	}

	public SqlStatement(SqlStatementType type, String sql, final List<SqlParameter> parameterInfoList) {
		this.type = type;
		this.sql = sql;
		this.parameterInfoList = parameterInfoList;
		this.resultInfoList = Collections.emptyList();
	}

	public SqlStatement(SqlEntityInfo sqlEntityInfo, SqlStatementType type, String sql,
			final List<SqlParameter> parameterInfoList, final List<SqlResult> resultInfoList, boolean lenient) {
		this.sqlEntityInfo = sqlEntityInfo;
		this.type = type;
		this.sql = sql;
		this.parameterInfoList = parameterInfoList;
		this.resultInfoList = resultInfoList;
		this.lenient = lenient;
	}

	public SqlEntityInfo getSqlEntityInfo() {
		return sqlEntityInfo;
	}

	public SqlStatementType getType() {
		return type;
	}

	public String getSql() {
		return sql;
	}

	public List<SqlParameter> getParameterInfoList() {
		return parameterInfoList;
	}

	public List<SqlResult> getResultInfoList() {
		return resultInfoList;
	}

	public boolean isWithSqlEntityInfo() {
		return sqlEntityInfo != null;
	}

	public boolean isMerge() {
		return merge;
	}

	public void setMerge(boolean merge) {
		this.merge = merge;
	}

    public boolean isLenient() {
		return lenient;
	}

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[sql = ").append(sql);
        if (parameterInfoList != null && !parameterInfoList.isEmpty()) {
            sb.append(":");
            boolean isAppendSymbol = false;
            for (SqlParameter sqlParameter : parameterInfoList) {
                if (isAppendSymbol) {
                    sb.append(", ");
                } else {
                    isAppendSymbol = true;
                }

                sb.append("{").append(sqlParameter.toString()).append("}");
            }
        }

        sb.append("]");
        return sb.toString();
    }

}
