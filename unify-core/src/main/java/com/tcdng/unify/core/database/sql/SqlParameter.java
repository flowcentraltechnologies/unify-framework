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

import java.util.Collection;

import com.tcdng.unify.core.util.StringUtils;

/**
 * SQL parameter object.
 * 
 * @author The Code Department
 * @since 4.1
 */
@SuppressWarnings("unchecked")
public class SqlParameter {

    private SqlDataTypePolicy sqlDataTypePolicy;

    private Object value;

    private boolean isInput;

    private boolean isOutput;

    private boolean isMultiple;

    public SqlParameter(SqlDataTypePolicy sqlDataTypePolicy) {
        this(sqlDataTypePolicy, null, false);
    }

    public SqlParameter(SqlDataTypePolicy sqlDataTypePolicy, Object value) {
        this(sqlDataTypePolicy, value, false);
    }

    public SqlParameter(SqlDataTypePolicy sqlDataTypePolicy, Object value, boolean isMultiple) {
        this.sqlDataTypePolicy = sqlDataTypePolicy;
        this.value = value;
        this.isMultiple = isMultiple;
    }

    public SqlParameter(SqlDataTypePolicy sqlDataTypePolicy, boolean isInput,
            boolean isOutput) {
        this.sqlDataTypePolicy = sqlDataTypePolicy;
        this.isInput = isInput;
        this.isOutput = isOutput;
    }

    public SqlDataTypePolicy getSqlTypePolicy() {
        return this.sqlDataTypePolicy;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public boolean isMultiple() {
        return isMultiple;
    }

    public boolean isInput() {
        return isInput;
    }

    public boolean isOutput() {
        return isOutput;
    }

    @Override
    public String toString() {
        if (this.isMultiple) {
            return StringUtils.buildCommaSeparatedString((Collection<Object>) this.value, false, true);
        }

        return String.valueOf(this.value);
    }
}
