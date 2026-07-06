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
package com.tcdng.unify.core.database.dynamic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.business.MockService;
import com.tcdng.unify.core.database.sql.SqlDialectNameConstants;

/**
 * Test dynamic data source definition provider.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component("test-dynamicdatasourcedefinitionprovider")
public class TestDynamicDataSourceDefinitionProviderImpl extends AbstractUnifyComponent
		implements DynamicDataSourceDefinitionProvider {

	private Map<String, DynamicDataSourceDef> map;

	public TestDynamicDataSourceDefinitionProviderImpl() {
		Map<String, DynamicDataSourceDef> _map = new HashMap<String, DynamicDataSourceDef>();
		_map.put(MockService.CREDITCHECK_DATASOURCECONFIG,
				new DynamicDataSourceDef(null, MockService.CREDITCHECK_DATASOURCECONFIG,
						"Test credit datasource configuration", SqlDialectNameConstants.HSQLDB, "locallhost", null,
						"dyntest", null, null, null, null, 4, true, 0L, 1L));
		this.map = Collections.unmodifiableMap(_map);
	}

	@Override
	public DynamicDataSourceDef provide(String dataSourceName) throws UnifyException {
		return map.get(dataSourceName);
	}

	@Override
	public boolean exists(String dataSourceName) throws UnifyException {
		return map.containsKey(dataSourceName);
	}

	@Override
	protected void onInitialize() throws UnifyException {

	}

	@Override
	protected void onTerminate() throws UnifyException {

	}

}
