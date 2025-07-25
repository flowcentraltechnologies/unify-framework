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
package com.tcdng.unify.core.system.entities;

import java.util.Date;

import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.database.AbstractEntityPolicy;

/**
 * Sequence block entity policy.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component("sequenceblock-policy")
public class ClusterSequenceBlockPolicy extends AbstractEntityPolicy {

    @Override
    public Object preCreate(Entity record, Date now) throws UnifyException {
        ((ClusterSequenceBlock) record).setVersionNo(1L);
        return record.getId();
    }

    @Override
    public void preUpdate(Entity record, Date now) throws UnifyException {
        ClusterSequenceBlock clusterSequenceBlock = (ClusterSequenceBlock) record;
        clusterSequenceBlock.setVersionNo(clusterSequenceBlock.getVersionNo() + 1L);
    }

    @Override
    public void onUpdateError(Entity record) {
        ClusterSequenceBlock clusterSequenceBlock = (ClusterSequenceBlock) record;
        clusterSequenceBlock.setVersionNo(clusterSequenceBlock.getVersionNo() - 1L);
    }

    @Override
    public boolean isSetNow() {
        return false;
    }
}
