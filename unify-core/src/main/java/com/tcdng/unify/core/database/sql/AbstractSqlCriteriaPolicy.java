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

import com.tcdng.unify.common.annotation.ColumnType;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.criterion.RestrictionType;
import com.tcdng.unify.core.database.NativeTranslator;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Base abstract SQL criteria policy.
 * 
 * @author The Code Department
 * @since 4.1
 */
public abstract class AbstractSqlCriteriaPolicy implements SqlCriteriaPolicy {

    protected SqlDataSourceDialectPolicies rootPolicies;

    protected String opSql;

    public AbstractSqlCriteriaPolicy(String opSql, SqlDataSourceDialectPolicies rootPolicies) {
        this.rootPolicies = rootPolicies;
        this.opSql = opSql;
    }

    @Override
    public void translate(NativeTranslator translator, StringBuilder sql, String tableName, String columnName, Object param1, Object param2)
            throws UnifyException {
        doTranslate(translator, sql, tableName, columnName, param1 != null ? resolveParam(tableName, param1) : null,
                param2 != null ? resolveParam(tableName, param2) : null);
    }

    /**
     * Returns the SQL restriction policy for supplied restriction.
     * 
     * @param restriction
     *            the restriction object
     * @return the criteria policy
     * @throws UnifyException
     *             if an error occurs
     */
    protected SqlCriteriaPolicy getOperatorPolicy(Restriction restriction) throws UnifyException {
        return rootPolicies.getSqlCriteriaPolicy(restriction.getConditionType().restrictionType());
    }

    /**
     * Returns the SQL criteria policy for supplied restriction type.
     * 
     * @param type
     *            the restriction type
     * @return the criteria policy
     * @throws UnifyException
     *             if an error occurs
     */
    protected SqlCriteriaPolicy getOperatorPolicy(RestrictionType type) throws UnifyException {
        return rootPolicies.getSqlCriteriaPolicy(type);
    }

    /**
     * Returns SQL data type policy for column type.
     * 
     * @param columnType
     *            the column type
     *  @param length the column length
     * @throws UnifyException
     *             if column type is not supported
     */
    protected SqlDataTypePolicy getSqlTypePolicy(ColumnType columnType, int length) throws UnifyException {
        return rootPolicies.getSqlTypePolicy(columnType, length);
    }

    /**
     * Converts a value to its native SQL string equivalent.
     * 
     * @param translator native translator (optional)
     * @param val
     *            the value to convert
     * @return the converted value
     * @throws UnifyException
     *             if an error occurs
     */
    protected String getNativeSqlParam(NativeTranslator translator, Object val) throws UnifyException {
        if (val instanceof SqlViewColumnInfo) {
            SqlViewColumnInfo sqlViewColumnInfo = (SqlViewColumnInfo) val;
            return sqlViewColumnInfo.getTableAlias() + "." + sqlViewColumnInfo.getColumnName();
        }

        String translation = null;
        if (val != null && translator != null) {
        	translation = translator.translateToNativeSqlParam(val);
        }
        
        return translation != null ? translation : rootPolicies.translateToNativeSqlParam(val);
    }

    /**
     * Returns the minimum clause values.
     */
    protected int maximumClauseValues() {
        return rootPolicies.getMaxClauseValues();
    }

    /**
     * Converts value to field type if necessary.
     * 
     * @param sqlFieldInfo
     *            the field information
     * @param postOp
     *            the value
     * @return the converted value
     * @throws UnifyException
     *             if an error occurs
     */
    protected Object convertType(SqlFieldInfo sqlFieldInfo, Object postOp) throws UnifyException {
        if (postOp != null && !postOp.getClass().equals(sqlFieldInfo.getFieldType())) {
            return DataUtils.convert(sqlFieldInfo.getFieldType(), postOp);
        }

        return postOp;
    }

    /**
     * Resolves parameter.
     * 
     * @param tableName
     *            the table name
     * @param param
     *            the parameter to resolve
     * @return the resolved parameter
     * @throws UnifyException
     *             if an error occurs
     */
    protected Object resolveParam(String tableName, Object param) throws UnifyException {
        if (param instanceof SqlFieldInfo) {
            if (tableName == null) {
                return ((SqlFieldInfo) param).getPreferredColumnName();
            }

            return tableName + "." + ((SqlFieldInfo) param).getPreferredColumnName();
        }

        return param instanceof EnumConst? ((EnumConst) param).code(): param;
    }

	protected abstract void doTranslate(NativeTranslator translator, StringBuilder sql, String tableName,
			String columnName, Object param1, Object param2) throws UnifyException;

    protected SqlDataSourceDialectPolicies getRootPolicies() {
        return rootPolicies;
    }
}
