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
package com.tcdng.unify.core.database;

import java.util.Date;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Policy class for test versioned entity.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component("testversionedentity-policy")
public class TestVersionedEntityPolicy<T extends AbstractTestVersionedTableEntity> extends TestEntityPolicy<T> {

    @Override
    public Object preCreate(T entity, Date now) throws UnifyException {
        Object id = super.preCreate(entity, now);
        ((AbstractTestVersionedTableEntity) entity).setVersion(1L);
        return id;
    }

    @Override
    public void preUpdate(T entity, Date now) throws UnifyException {
        super.preUpdate(entity, now);
        ((AbstractTestVersionedTableEntity) entity)
                .setVersion(((AbstractTestVersionedTableEntity) entity).getVersion() + 1L);
    }
}
