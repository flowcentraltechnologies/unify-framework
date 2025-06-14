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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.ApplicationAliasConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.database.NativeQuery;
import com.tcdng.unify.core.database.sql.SqlDistinctRowListConfig;
import com.tcdng.unify.core.database.sql.SqlDistinctRowListConfigManager;
import com.tcdng.unify.core.util.DataUtils;

/**
 * SQL distinct row list command.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component("sqldistinctrowlist")
public class DynamicSqlDistinctRowListCommand extends AbstractDynamicSqlDataSourceListCommand {

    @Override
    public List<? extends Listable> execute(Locale locale, DynamicSqlParams params) throws UnifyException {
        if (params.getConfigName() != null) {
            SqlDistinctRowListConfigManager manager = (SqlDistinctRowListConfigManager) this
                    .getComponent(ApplicationAliasConstants.SQL_DISTINCTROWLIST_CONFIG_MANAGER);
            SqlDistinctRowListConfig config = manager.getSqlDistinctRowListConfig(params.getConfigName());

            String tableName = config.getTable();
            NativeQuery query = NativeQuery.newBuilder().schemaName(config.getSchema()).tableName(tableName)
                    .addColumn(tableName, config.getKeyColumn()).addColumn(tableName, config.getDescColumn())
                    .distinct(true).build();
            List<Object[]> rows = getDsManager().getRows(config.getDataSource(), query);

            List<ListData> resultList = new ArrayList<ListData>();
            for (Object[] item : rows) {
                resultList.add(new ListData(String.valueOf(item[0]), String.valueOf(item[1])));
            }

            DataUtils.sortAscending(resultList, ListData.class, "listDescription");
            return resultList;
        }

        return Collections.emptyList();
    }

}
