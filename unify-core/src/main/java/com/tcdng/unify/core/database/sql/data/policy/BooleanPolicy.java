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
package com.tcdng.unify.core.database.sql.data.policy;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import com.tcdng.unify.core.database.sql.AbstractSqlDataTypePolicy;
import com.tcdng.unify.core.util.SqlUtils;

/**
 * Boolean data type SQL policy.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class BooleanPolicy extends AbstractSqlDataTypePolicy {

    @Override
    public void appendTypeSql(StringBuilder sb, int length, int precision, int scale) {
        sb.append(" CHAR(").append(length).append(")");
    }

    @Override
    public void executeSetPreparedStatement(Object pstmt, int index, Object data, long utcOffset) throws Exception {
        if (data == null) {
            ((PreparedStatement) pstmt).setNull(index, Types.CHAR);
        } else {
            ((PreparedStatement) pstmt).setString(index, SqlUtils.getString((Boolean) data));
        }
    }

    @Override
    public Object executeGetResult(Object rs, Class<?> type, String column, long utcOffset) throws Exception {
        return SqlUtils.getBoolean(((ResultSet) rs).getString(column));
    }

    @Override
    public Object executeGetResult(Object rs, Class<?> type, int index, long utcOffset) throws Exception {
        return SqlUtils.getBoolean(((ResultSet) rs).getString(index));
    }

    @Override
    public void executeRegisterOutParameter(Object cstmt, int index) throws Exception {
        ((CallableStatement) cstmt).registerOutParameter(index, Types.CHAR);
    }

    @Override
    public Object executeGetOutput(Object cstmt, Class<?> type, int index, long utcOffset) throws Exception {
        return SqlUtils.getBoolean(((CallableStatement) cstmt).getString(index));
    }

    @Override
    public String getAltDefault(Class<?> fieldType) {
        return "'N'";
    }

    @Override
	public String getTypeName() {
		return "CHAR";
	}

    @Override
    public int getSqlType() {
        return Types.CHAR;
    }

    @Override
    public boolean isFixedLength() {
        return true;
    }

}
