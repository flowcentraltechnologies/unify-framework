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

/**
 * Dynamic data source configuration.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class DynamicDataSourceConfig {

	private String preferredName;

	private String name;

	private String dialect;

	private String driver;

	private String host;

	private String port;

	private String database;

	private String service;

	private String schema;

	private String userName;

	private String password;

	private String jdbcUrl;

	private boolean inMemory;

	private int maxConnection;

	private long versionNo;

	public DynamicDataSourceConfig(String preferredName, String name, String dialect, String driver, String host,
			String port, String database, String service, String schema, String userName, String password,
			String jdbcUrl, boolean inMemory, int maxConnection, long versionNo) {
		this.preferredName = preferredName;
		this.name = name;
		this.dialect = dialect;
		this.driver = driver;
		this.host = host;
		this.port = port;
		this.database = database;
		this.service = service;
		this.schema = schema;
		this.userName = userName;
		this.password = password;
		this.jdbcUrl = jdbcUrl;
		this.inMemory = inMemory;
		this.maxConnection = maxConnection;
		this.versionNo = versionNo;
	}

	public String getPreferredName() {
		return preferredName;
	}

	public String getName() {
		return name;
	}

	public String getDialect() {
		return dialect;
	}

	public String getDriver() {
		return driver;
	}

	public String getHost() {
		return host;
	}

	public String getPort() {
		return port;
	}

	public String getDatabase() {
		return database;
	}

	public String getService() {
		return service;
	}

	public String getSchema() {
		return schema;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public boolean isInMemory() {
		return inMemory;
	}

	public int getMaxConnection() {
		return maxConnection;
	}

	public long getVersionNo() {
		return versionNo;
	}

}
