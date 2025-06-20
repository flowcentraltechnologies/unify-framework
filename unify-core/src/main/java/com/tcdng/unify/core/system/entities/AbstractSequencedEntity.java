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

import com.tcdng.unify.core.annotation.Id;
import com.tcdng.unify.core.annotation.Policy;
import com.tcdng.unify.core.annotation.Tooling;
import com.tcdng.unify.core.database.AbstractEntity;

/**
 * Abstract class for entity instances with IDs based on sequence number.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Tooling(name = "plainSequenced", description = "Plain Sequenced")
@Policy("sequencedentity-policy")
public abstract class AbstractSequencedEntity extends AbstractEntity implements SequencedEntity {

    @Id
    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
	public void setPreferredId(Object id) {
        this.id = (Long) id;
	}

	@Override
    public boolean isReserved() {
        return id != null && id.compareTo(Long.valueOf(0L)) < 0;
    }

    public boolean isIdBlank() {
        return id == null;
    }
}
