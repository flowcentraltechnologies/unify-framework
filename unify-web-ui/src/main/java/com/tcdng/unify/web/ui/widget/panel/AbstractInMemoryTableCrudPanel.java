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
package com.tcdng.unify.web.ui.widget.panel;

import java.util.List;

import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.ReflectUtils;

/**
 * Abstract in-memory table CRUD panel.
 * 
 * @author The Code Department
 * @since 4.1
 */
@UplBinding("web/panels/upl/inmemorytablecrudpanel.upl")
public abstract class AbstractInMemoryTableCrudPanel<T extends Entity> extends AbstractTableCrudPanel<T> {

    private boolean populateListOnly;

    public AbstractInMemoryTableCrudPanel(Class<T> entityClass, String title, boolean populateListOnly) {
        super(entityClass, title);
        this.populateListOnly = populateListOnly;
    }

    @Override
    protected List<T> doFindRecords() throws UnifyException {
        return getCrudData().getRecordList();
    }

    @Override
    protected T doPrepareCreateRecord() throws UnifyException {
        return ReflectUtils.newInstance(getCrudData().getEntityClass());
    }

    @Override
    protected void doCreateRecord() throws UnifyException {
        T record = getCrudData().getRecord();
        if (populateListOnly) {
            getGenericService().populateListOnly(record);
        }

        getCrudData().getRecordList().add(record);
    }

    @Override
    protected void doUpdateRecord() throws UnifyException {
        T record = getCrudData().getRecord();
        if (populateListOnly) {
            getGenericService().populateListOnly(record);
        }
    }

    @Override
    protected void doDeleteRecord() throws UnifyException {
        T record = getCrudData().getRecord();
        getCrudData().getRecordList().remove(record);
    }
}