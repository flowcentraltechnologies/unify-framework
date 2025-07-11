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
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import javax.sql.rowset.serial.SerialClob;

import com.tcdng.unify.core.database.sql.AbstractSqlDataTypePolicy;

/**
 * CLOB data type SQL policy.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class ClobPolicy extends AbstractSqlDataTypePolicy {

    @Override
    public void appendTypeSql(StringBuilder sb, int length, int precision, int scale) {
        sb.append(" CLOB");
    }

    @Override
    public void appendDefaultSql(StringBuilder sb, Class<?> type, String defaultVal) {

    }

    @Override
    public void executeSetPreparedStatement(Object pstmt, int index, Object data, long utcOffset) throws Exception {
        if (data == null || ((String) data).length() == 0) {
            ((PreparedStatement) pstmt).setNull(index, Types.CLOB);
        } else {
            ((PreparedStatement) pstmt).setClob(index, new SerialClob(((String) data).toCharArray()));
        }
    }

    @Override
    public Object executeGetResult(Object rs, Class<?> type, String column, long utcOffset) throws Exception {
        Clob clob = ((ResultSet) rs).getClob(column);
        if (clob != null) {
            return clob.getSubString(1, (int) clob.length());
        }
        return null;
    }

    @Override
    public Object executeGetResult(Object rs, Class<?> type, int index, long utcOffset) throws Exception {
        Clob clob = ((ResultSet) rs).getClob(index);
        if (clob != null) {
            return clob.getSubString(1, (int) clob.length());
        }
        return null;
    }

    @Override
    public void executeRegisterOutParameter(Object cstmt, int index) throws Exception {
        ((CallableStatement) cstmt).registerOutParameter(index, Types.CLOB);
    }

    @Override
    public Object executeGetOutput(Object cstmt, Class<?> type, int index, long utcOffset) throws Exception {
        Clob clob = ((CallableStatement) cstmt).getClob(index);
        if (clob != null) {
            return clob.getSubString(1, (int) clob.length());
        }
        return null;
    }

    @Override
    public String getAltDefault(Class<?> fieldType) {
        return null;
    }

    @Override
	public String getTypeName() {
		return "CLOB";
	}

    @Override
    public int getSqlType() {
        return Types.CLOB;
    }

    @Override
    public boolean isFixedLength() {
        return true;
    }

}
