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
 * Dynamic datasource definition.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class DynamicDataSourceDef {

	private String name;

	private String preferredName;

	private String description;

	private String dialect;

	private String host;

	private String port;

	private String database;

	private String service;

	private String schema;

	private String userName;

	private String password;

	private int maxConnection;

	private boolean managed;

	private boolean inMemory;

	private Long id;

	private long versionNo;

	public DynamicDataSourceDef(String name, String preferredName, String description, String dialect, String host,
			String port, String database, String service, String schema, String userName, String password,
			int maxConnection, boolean managed, boolean inMemory, Long id, long versionNo) {
		this.name = name;
		this.preferredName = preferredName;
		this.description = description;
		this.dialect = dialect;
		this.host = host;
		this.port = port;
		this.database = database;
		this.service = service;
		this.schema = schema;
		this.userName = userName;
		this.password = password;
		this.maxConnection = maxConnection;
		this.managed = managed;
		this.inMemory = inMemory;
		this.id = id;
		this.versionNo = versionNo;
	}

	public String getPreferredName() {
		return preferredName;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getDialect() {
		return dialect;
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

	public int getMaxConnection() {
		return maxConnection;
	}

	public boolean isInMemory() {
		return inMemory;
	}

	public boolean isManaged() {
		return managed;
	}

	public Long getId() {
		return id;
	}

	public long getVersionNo() {
		return versionNo;
	}

}
