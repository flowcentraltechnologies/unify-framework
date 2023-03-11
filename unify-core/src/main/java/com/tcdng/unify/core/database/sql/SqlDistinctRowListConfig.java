/*
 * Copyright 2018-2023 The Code Department.
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

/**
 * Represents a distinct row list configuration.
 * 
 * @author The Code Department
 * @since 1.0
 */
public class SqlDistinctRowListConfig {

    private String dataSource;

    private String schema;

    private String table;

    private String keyColumn;

    private String descColumn;

    public SqlDistinctRowListConfig(String dataSource, String schema, String table, String keyColumn,
            String descColumn) {
        this.dataSource = dataSource;
        this.schema = schema;
        this.table = table;
        this.keyColumn = keyColumn;
        this.descColumn = descColumn;
    }

    public String getDataSource() {
        return dataSource;
    }

    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }

    public String getKeyColumn() {
        return keyColumn;
    }

    public String getDescColumn() {
        return descColumn;
    }
}
