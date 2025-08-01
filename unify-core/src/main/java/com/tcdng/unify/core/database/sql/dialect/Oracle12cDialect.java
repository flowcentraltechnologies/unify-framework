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
package com.tcdng.unify.core.database.sql.dialect;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.database.sql.SqlDialectNameConstants;

/**
 * Oracle 12c SQL dialect.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component(name = SqlDialectNameConstants.ORACLE_12C, description = "$m{sqldialect.oracle12cdb}")
public class Oracle12cDialect extends OracleDialect {

    @Override
    protected boolean appendLimitOffsetInfixClause(StringBuilder sql, int offset, int limit) throws UnifyException {
        return false;
    }

    @Override
    protected boolean appendWhereLimitOffsetSuffixClause(StringBuilder sql, int offset, int limit, boolean append)
            throws UnifyException {
        return false;
    }

    @Override
    protected boolean appendLimitOffsetSuffixClause(StringBuilder sql, int offset, int limit, boolean append)
            throws UnifyException {
        boolean isAppend = false;
        if (offset > 0) {
            sql.append(" OFFSET ").append(offset).append(" ROWS");
            isAppend = true;
        }

        if (limit > 0) {
            if (isAppend) {
                sql.append(" FETCH NEXT ");
            } else {
                sql.append(" FETCH FIRST ");
            }

            sql.append(limit).append(" ROWS ONLY");
            isAppend = true;
        }

        return isAppend;
    }
}
