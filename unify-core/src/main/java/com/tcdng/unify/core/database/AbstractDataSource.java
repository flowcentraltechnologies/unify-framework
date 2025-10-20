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
package com.tcdng.unify.core.database;

import java.util.List;

import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Abstract base data source component that with typical configurable data
 * source properties.
 * 
 * @author The Code Department
 * @since 4.1
 */
public abstract class AbstractDataSource extends AbstractUnifyComponent implements DataSource {

	@Configurable
	private DataSourceDialect dialect;

    @Configurable
    private String translator;

	@Configurable("false")
	private boolean allObjectsInLowercase;

	@Configurable("false")
	private boolean supportUnifyViews;

	@Configurable("false")
	private boolean managed;

	@Configurable("false")
	private boolean readOnly;

	@Configurable("false")
	private boolean initDelayed;

	@Configurable
	private List<String> entityList;
	
	private NativeTranslator nativeTranslator;
	
	public final void setDialect(DataSourceDialect dialect) throws UnifyException {
		this.dialect = dialect;
		if (dialect != null) {
			dialect.setDataSourceName(getEntityMatchingName());
			dialect.setAllObjectsInLowerCase(allObjectsInLowercase);
			dialect.setSupportUnifyViews(supportUnifyViews);
		}
	}

	@Override
	public boolean isManaged() throws UnifyException {
		return managed;
	}

	@Override
	public final boolean isReadOnly() {
		return readOnly;
	}

	@Override
	public final boolean isInitDelayed() throws UnifyException {
		return initDelayed;
	}

	@Override
	public DataSourceDialect getDialect() throws UnifyException {
		if (nativeTranslator != null) {
			dialect.setNativeTranslator(nativeTranslator);
		}
		
		return dialect;
	}

	@Override
	protected void onInitialize() throws UnifyException {
		if (ApplicationComponents.APPLICATION_DATASOURCE.equals(getName())) {
			supportUnifyViews = true;
			managed = true;
		}
		
		if (dialect != null) {
			dialect.setDataSourceName(getEntityMatchingName());
			dialect.setAllObjectsInLowerCase(allObjectsInLowercase);
			dialect.setSupportUnifyViews(supportUnifyViews);
		}
		
		if (!StringUtils.isBlank(translator)) {
			nativeTranslator = getComponent(NativeTranslator.class, translator);
		}
	}

	@Override
	protected void onTerminate() throws UnifyException {

	}

	private String getEntityMatchingName() {
		String name = getPreferredName();
		if (StringUtils.isBlank(name)) {
			name = getName();
		}

		return name;
	}
}
