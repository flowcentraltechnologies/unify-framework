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
package com.tcdng.unify.core.database.dynamic.sql;

import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.UnifyCoreErrorConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Singleton;
import com.tcdng.unify.core.database.DataSourceDialect;
import com.tcdng.unify.core.database.sql.AbstractSqlDataSource;

/**
 * Dynamic SQL data source implementation.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Singleton(false)
@Component(ApplicationComponents.APPLICATION_DYNAMICSQLDATASOURCE)
public class DynamicSqlDataSourceImpl extends AbstractSqlDataSource implements DynamicSqlDataSource {

    private String preferredName;
    
    private boolean configured;
    
    @Override
    public void configure(DynamicSqlDataSourceConfig dataSourceConfig) throws UnifyException {
        if (isConfigured()) {
            throw new UnifyException(UnifyCoreErrorConstants.DYNAMIC_DATASOURCE_ALREADY_CONFIGURED,
                    dataSourceConfig.getName());
        }

        preferredName = dataSourceConfig.getPreferredName();
        configured = true;
        setDialect((DataSourceDialect) getComponent(dataSourceConfig.getDialect()));
        setDriver(dataSourceConfig.getDriver());
        setConnectionUrl(dataSourceConfig.getConnectionUrl());
        setAppSchema(dataSourceConfig.getDbSchema());
        setUsername(dataSourceConfig.getDbUsername());
        setPassword(dataSourceConfig.getDbPassword());
        setMaxConnections(dataSourceConfig.getMaxConnection());
        setShutdownOnTerminate(dataSourceConfig.isShutdownOnTerminate());
        doInitConnectionPool();
    }

    @Override
    public String getPreferredName() {
        return preferredName;
    }

    @Override
    public boolean isConfigured() throws UnifyException {
        return configured;
    }

	@Override
	public final boolean isManaged() throws UnifyException {
		return true;
	}
}
