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
package com.tcdng.unify.core.database.sql.data.policy;

import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import com.tcdng.unify.core.data.UploadedFile;
import com.tcdng.unify.core.database.sql.AbstractSqlDataTypePolicy;

/**
 * Uploaded file data type SQL policy.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class UploadedFilePolicy extends AbstractSqlDataTypePolicy {
    @Override
    public void appendTypeSql(StringBuilder sb, int length, int precision, int scale) {
        sb.append(" BLOB");
    }

    @Override
    public void appendDefaultSql(StringBuilder sb, Class<?> type, String defaultVal) {

    }

    @Override
    public void executeSetPreparedStatement(Object pstmt, int index, Object data, long utcOffset) throws Exception {
        PreparedStatement ps = (PreparedStatement) pstmt;
        if (data == null) {
            ps.setNull(index, Types.BLOB);
        } else {
            ps.setBlob(index, ((UploadedFile) data).getIn());
        }
    }

    @Override
    public Object executeGetResult(Object rs, Class<?> type, String column, long utcOffset) throws Exception {
        Blob blob = ((ResultSet) rs).getBlob(column);
        if (blob != null) {
            return UploadedFile.createUsingTempFile(blob.getBinaryStream());
        }
        
        return null;
    }

    @Override
    public Object executeGetResult(Object rs, Class<?> type, int index, long utcOffset) throws Exception {
        Blob blob = ((ResultSet) rs).getBlob(index);
        if (blob != null) {
            return UploadedFile.createUsingTempFile(blob.getBinaryStream());
        }
        return null;
    }

    @Override
    public void executeRegisterOutParameter(Object cstmt, int index) throws Exception {
        ((CallableStatement) cstmt).registerOutParameter(index, Types.BLOB);
    }

    @Override
    public Object executeGetOutput(Object cstmt, Class<?> type, int index, long utcOffset) throws Exception {
        Blob blob = ((CallableStatement) cstmt).getBlob(index);
        if (blob != null) {
            return UploadedFile.createUsingTempFile(blob.getBinaryStream());
        }
        return null;
    }

    @Override
    public String getAltDefault(Class<?> fieldType) {
        return null;
    }

    @Override
    public String getTypeName() {
        return "BLOB";
    }

    @Override
    public int getSqlType() {
        return Types.BLOB;
    }

    @Override
    public boolean isFixedLength() {
        return true;
    }
}
