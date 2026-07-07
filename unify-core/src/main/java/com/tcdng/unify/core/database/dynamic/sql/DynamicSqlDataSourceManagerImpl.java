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
package com.tcdng.unify.core.database.dynamic.sql;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.UnifyCoreErrorConstants;
import com.tcdng.unify.core.UnifyCorePropertyConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.ForceConstraints;
import com.tcdng.unify.core.constant.PrintFormat;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.database.DataSourceEntityContext;
import com.tcdng.unify.core.database.DataSourceEntityListProvider;
import com.tcdng.unify.core.database.DataSourceManagerContext;
import com.tcdng.unify.core.database.DataSourceManagerOptions;
import com.tcdng.unify.core.database.NativeQuery;
import com.tcdng.unify.core.database.dynamic.DynamicDataSourceConfig;
import com.tcdng.unify.core.database.dynamic.DynamicDataSourceDef;
import com.tcdng.unify.core.database.dynamic.DynamicDataSourceDefinitionProvider;
import com.tcdng.unify.core.database.sql.AbstractSqlDataSourceManager;
import com.tcdng.unify.core.database.sql.SqlColumnInfo;
import com.tcdng.unify.core.database.sql.SqlDataSource;
import com.tcdng.unify.core.database.sql.SqlTableInfo;
import com.tcdng.unify.core.database.sql.SqlTableType;
import com.tcdng.unify.core.util.SqlUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Default implementation of dynamic SQL data source manager.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component(ApplicationComponents.APPLICATION_DYNAMICSQLDATASOURCEMANAGER)
public class DynamicSqlDataSourceManagerImpl extends AbstractSqlDataSourceManager
		implements DynamicSqlDataSourceManager {

	@Configurable(ApplicationComponents.APPLICATION_DATASOURCE_ENTITYLIST_PROVIDER)
	private DataSourceEntityListProvider entityListProvider;

	@Configurable
	private DynamicDataSourceDefinitionProvider definitionProvider;

	private final FactoryMap<String, DataSourceEntry> dynamicSqlDataSourceMap;

	public DynamicSqlDataSourceManagerImpl() {
		dynamicSqlDataSourceMap = new FactoryMap<String, DataSourceEntry>(true) {

			@Override
			protected boolean stale(String configName, DataSourceEntry entry) throws Exception {
				DynamicDataSourceDef dataSourceConnectionDef = definitionProvider.provide(configName);
				if (dataSourceConnectionDef == null
						|| entry.getConfig().getVersionNo() < dataSourceConnectionDef.getVersionNo()) {
					try {
						entry.getDynamicSqlDataSource().terminate();
					} catch (Exception e) {
						logError(e);
					}
					return true;
				}

				return false;
			}

			@Override
			protected DataSourceEntry create(String configName, Object... params) throws Exception {
				final DynamicDataSourceDef dynamicDataSourceDef = definitionProvider.provide(configName);
				final DynamicDataSourceConfig config = SqlUtils.getDynamicDataSourceConfig(dynamicDataSourceDef);

				final DynamicSqlDataSource dynamicSqlDataSource = (DynamicSqlDataSource) getComponent(
						ApplicationComponents.APPLICATION_DYNAMICSQLDATASOURCE);
				dynamicSqlDataSource.configure(config);

				final DataSourceEntityContext entityCtx = entityListProvider
						.getDataSourceEntityContext(Arrays.asList(dynamicDataSourceDef.getPreferredName()));
				entityCtx.addDataSourceAlias(dynamicDataSourceDef.getPreferredName(), dynamicDataSourceDef.getName());
				final DataSourceManagerContext ctx = new DataSourceManagerContext(entityCtx,
						new DataSourceManagerOptions(PrintFormat.NONE,
								ForceConstraints.fromBoolean(!getContainerSetting(boolean.class,
										UnifyCorePropertyConstants.APPLICATION_FOREIGNKEY_EASE, false))));
				initDataSource(ctx, dynamicDataSourceDef.getName(), dynamicSqlDataSource);
				if (dynamicDataSourceDef.isManaged()) {
					manageDataSource(ctx, dynamicDataSourceDef.getName(), dynamicSqlDataSource);
				}

				return new DataSourceEntry(dynamicSqlDataSource, config);
			}
		};
	}

	@Override
	public boolean testConfiguration(String dataSourceConfigName) throws UnifyException {
		return ((DynamicSqlDataSource) getDynamicSqlDataSource(dataSourceConfigName)).testConnection();
	}

	@Override
	public int testNativeQuery(String dataSourceConfigName, NativeQuery query) throws UnifyException {
		return ((DynamicSqlDataSource) getDynamicSqlDataSource(dataSourceConfigName)).testNativeQuery(query);
	}

	@Override
	public int testNativeQuery(String dataSourceConfigName, String nativeSql) throws UnifyException {
		return ((DynamicSqlDataSource) getDynamicSqlDataSource(dataSourceConfigName)).testNativeQuery(nativeSql);
	}

	@Override
	public int testNativeUpdate(String dataSourceConfigName, String updateSql) throws UnifyException {
		return ((DynamicSqlDataSource) getDynamicSqlDataSource(dataSourceConfigName)).testNativeUpdate(updateSql);
	}

	@Override
	public int getDataSourceCount() throws UnifyException {
		return dynamicSqlDataSourceMap.size();
	}

	@Override
	public List<String> getSchemas(String dataSourceConfigName) throws UnifyException {
		return getDynamicSqlDataSource(dataSourceConfigName).getSchemaList();
	}

	@Override
	public List<SqlTableInfo> getTables(String dataSourceConfigName, SqlTableType sqlTableType) throws UnifyException {
		final DataSourceEntry entry = dynamicSqlDataSourceMap.get(dataSourceConfigName);
		return entry.getDynamicSqlDataSource().getTableList(entry.getConfig().getSchema(), sqlTableType);
	}

	@Override
	public List<SqlColumnInfo> getColumns(String dataSourceConfigName, String tableName) throws UnifyException {
		final DataSourceEntry entry = dynamicSqlDataSourceMap.get(dataSourceConfigName);
		return entry.getDynamicSqlDataSource().getColumnList(entry.getConfig().getSchema(), tableName);
	}

	@Override
	public List<Object[]> getRows(String dataSourceConfigName, NativeQuery query) throws UnifyException {
		return getDynamicSqlDataSource(dataSourceConfigName).getRows(query);
	}

	@Override
	public SqlDataSource getDataSource(String dataSourceConfigName) throws UnifyException {
		return getDynamicSqlDataSource(dataSourceConfigName);
	}

	@Override
	public Connection getConnection(String dataSourceConfigName) throws UnifyException {
		return getDynamicSqlDataSource(dataSourceConfigName).getConnection();
	}

	@Override
	public boolean restoreConnection(String dataSourceConfigName, Connection connection) throws UnifyException {
		return getDynamicSqlDataSource(dataSourceConfigName).restoreConnection(connection);
	}

	@Override
	public void terminateConfiguration(String dataSourceConfigName) throws UnifyException {
		SqlDataSource sqlDataSource = getDynamicSqlDataSource(dataSourceConfigName);
		try {
			sqlDataSource.terminate();
		} finally {
			dynamicSqlDataSourceMap.remove(dataSourceConfigName);
		}
	}

	@Override
	public void terminateAll() throws UnifyException {
		for (String dataSourceConfigName : new ArrayList<String>(dynamicSqlDataSourceMap.keySet())) {
			terminateConfiguration(dataSourceConfigName);
		}
	}

	@Override
	protected SqlDataSource getSqlDataSource(String dataSourceName) throws UnifyException {
		return getDynamicSqlDataSource(dataSourceName);
	}

	@Override
	protected void onInitialize() throws UnifyException {

	}

	@Override
	protected void onTerminate() throws UnifyException {
		terminateAll();
	}

	private class DataSourceEntry {

		private final DynamicSqlDataSource dynamicSqlDataSource;

		private final DynamicDataSourceConfig config;

		public DataSourceEntry(DynamicSqlDataSource dynamicSqlDataSource, DynamicDataSourceConfig config) {
			this.dynamicSqlDataSource = dynamicSqlDataSource;
			this.config = config;
		}

		public DynamicSqlDataSource getDynamicSqlDataSource() {
			return dynamicSqlDataSource;
		}

		public DynamicDataSourceConfig getConfig() {
			return config;
		}

	}

	private SqlDataSource getDynamicSqlDataSource(String dataSourceConfigName) throws UnifyException {
		final List<String> configurations = StringUtils.charToListSplit(dataSourceConfigName, ',');
		for (String configName : configurations) {
			if (isComponent(configName)) {
				return getComponent(SqlDataSource.class, configName);
			}
		}

		for (String configName : configurations) {
			if (dynamicSqlDataSourceMap.isKey(configName) || definitionProvider.exists(configName)) {
				return dynamicSqlDataSourceMap.get(configName).getDynamicSqlDataSource();
			}
		}

		throw new UnifyException(UnifyCoreErrorConstants.DYNAMIC_DATASOURCE_IS_UNKNOWN, dataSourceConfigName);
	}

}
