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

/**
 * Enumeration of SQL query type. Used when generating SQL WHERE clauses to
 * indicate sub-elements to include.
 * 
 * @author The Code Department
 * @since 4.1
 */
public enum SqlQueryType {

    SELECT(false, true, true), UPDATE(true, true, true), DELETE(true, true, true);

    private final boolean update;

    private final boolean order;

    private final boolean limit;

    private SqlQueryType(boolean update, boolean order, boolean limit) {
        this.update = update;
        this.order = order;
        this.limit = limit;
    }

    public boolean isUpdate() {
        return update;
    }

    public boolean includeOrder() {
        return order;
    }

    public boolean includeLimit() {
        return limit;
    }
}
