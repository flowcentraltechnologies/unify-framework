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
package com.tcdng.unify.core.database.sql.criterion.policy;

import java.util.List;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.ExpressionField;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.criterion.RestrictionField;
import com.tcdng.unify.core.criterion.SingleParamRestriction;
import com.tcdng.unify.core.database.CustomTranslator;
import com.tcdng.unify.core.database.NativeTranslator;
import com.tcdng.unify.core.database.sql.AbstractSqlCriteriaPolicy;
import com.tcdng.unify.core.database.sql.SqlDataSourceDialectPolicies;
import com.tcdng.unify.core.database.sql.SqlEntityInfo;
import com.tcdng.unify.core.database.sql.SqlFieldInfo;
import com.tcdng.unify.core.database.sql.SqlParameter;
import com.tcdng.unify.core.transform.Transformer;

/**
 * Base single parameter operator policy.
 * 
 * @author The Code Department
 * @since 4.1
 */
public abstract class SingleParameterPolicy extends AbstractSqlCriteriaPolicy {

	final private boolean caseInsensitive;

	public SingleParameterPolicy(String opSql, SqlDataSourceDialectPolicies rootPolicies) {
		this(opSql, rootPolicies, false);
	}

	public SingleParameterPolicy(String opSql, SqlDataSourceDialectPolicies rootPolicies, boolean caseInsensitive) {
		super(opSql, rootPolicies);
		this.caseInsensitive = caseInsensitive;
	}

	@Override
	public void translate(CustomTranslator translator, StringBuilder sql, SqlEntityInfo sqlEntityInfo,
			Restriction restriction) throws UnifyException {
		SingleParamRestriction svc = (SingleParamRestriction) restriction;
		final Object val = svc.getParam();
		if (val instanceof ExpressionField) {
			if (translator == null) {
				throw new IllegalArgumentException("Custom translator is required for expression parameters.");
			}

			SqlFieldInfo toSqlFieldInfo = sqlEntityInfo.getListFieldInfo(svc.getFieldName());
			translator.translateExpression(sql, sqlEntityInfo, toSqlFieldInfo, opSql,
					((ExpressionField) val).getExpression());
			return;
		}

		String columnName = svc.getFieldName();
		if (sqlEntityInfo != null) {
			columnName = sqlEntityInfo.getListFieldInfo(svc.getFieldName()).getPreferredColumnName();
		}

		final String tableName = sqlEntityInfo.getTableAlias();
		if (val instanceof RestrictionField) {
			if (caseInsensitive) {
				Object param = resolveParam(tableName,
						sqlEntityInfo.getListFieldInfo(((RestrictionField) val).getName()));
				param = param != null ? ((String) param).toLowerCase() : null;
				sql.append("LOWER(").append(tableName).append('.').append(columnName).append(")").append(opSql)
						.append(param);
			} else {
				sql.append(tableName).append('.').append(columnName).append(opSql).append(
						resolveParam(tableName, sqlEntityInfo.getListFieldInfo(((RestrictionField) val).getName())));
			}

			return;
		}

		translate(translator != null ? translator.getNativeTranslator() : null, sql, tableName, columnName, val, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void generatePreparedStatementCriteria(CustomTranslator translator, StringBuilder sql,
			List<SqlParameter> parameterInfoList, SqlEntityInfo sqlEntityInfo, Restriction restriction)
			throws UnifyException {
		SingleParamRestriction svc = (SingleParamRestriction) restriction;
		SqlFieldInfo sqlFieldInfo = sqlEntityInfo.getListFieldInfo(svc.getFieldName());
		Object val = svc.getParam();
		if (val instanceof ExpressionField) {
			if (translator == null) {
				throw new IllegalArgumentException("Custom translator is required for expression parameters.");
			}

			SqlFieldInfo toSqlFieldInfo = sqlEntityInfo.getListFieldInfo(svc.getFieldName());
			translator.translateExpression(sql, sqlEntityInfo, toSqlFieldInfo, opSql,
					((ExpressionField) val).getExpression());
			return;
		}

		if (val instanceof RestrictionField) {
			if (caseInsensitive) {
				Object param = resolveParam(null, sqlEntityInfo.getListFieldInfo(((RestrictionField) val).getName()));
				param = param != null ? ((String) param).toLowerCase() : null;
				sql.append("LOWER(").append(sqlFieldInfo.getPreferredColumnName()).append(")").append(opSql)
						.append(param);
			} else {
				sql.append(sqlFieldInfo.getPreferredColumnName()).append(opSql)
						.append(resolveParam(null, sqlEntityInfo.getListFieldInfo(((RestrictionField) val).getName())));
			}

			return;
		}

		if (caseInsensitive) {
			sql.append("LOWER(").append(sqlFieldInfo.getPreferredColumnName()).append(")").append(opSql).append("?");
		} else {
			sql.append(sqlFieldInfo.getPreferredColumnName()).append(opSql).append("?");
		}

		if (sqlFieldInfo.isTransformed()) {
			val = ((Transformer<Object, Object>) sqlFieldInfo.getTransformer()).forwardTransform(val);
		}

		Object postOp = convertType(sqlFieldInfo, resolveParam(null, val));
		if (caseInsensitive) {
			postOp = postOp != null ? ((String) postOp).toLowerCase() : null;
		}

		parameterInfoList.add(
				new SqlParameter(getSqlTypePolicy(sqlFieldInfo.getColumnType(), sqlFieldInfo.getLength()), postOp));
	}

	@Override
	protected void doTranslate(NativeTranslator translator, StringBuilder sql, String tableName, String columnName,
			Object param1, Object param2) throws UnifyException {
		if (caseInsensitive) {
			String postOp = getNativeSqlParam(translator, param1);
			postOp = postOp != null ? postOp.toLowerCase() : null;
			sql.append("LOWER(").append(tableName).append('.').append(columnName).append(")").append(opSql)
					.append(postOp);
		} else {
			sql.append(tableName).append('.').append(columnName).append(opSql)
					.append(getNativeSqlParam(translator, param1));
		}
	}
}
