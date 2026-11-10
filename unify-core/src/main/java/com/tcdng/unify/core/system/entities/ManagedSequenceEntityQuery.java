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
package com.tcdng.unify.core.system.entities;

import com.tcdng.unify.core.criterion.CompoundRestriction;

/**
 * Convenient query class for managed sequence entities.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class ManagedSequenceEntityQuery<T extends AbstractManagedSequenceEntity> extends SequenceEntityQuery<T> {

	public ManagedSequenceEntityQuery(Class<T> entityClass) {
		super(entityClass);
	}

	public ManagedSequenceEntityQuery(Class<T> entityClass, boolean applyAppQueryLimit) {
		super(entityClass, applyAppQueryLimit);
	}

	public ManagedSequenceEntityQuery(Class<T> entityClass, CompoundRestriction restrictions,
			boolean applyAppQueryLimit) {
		super(entityClass, restrictions, applyAppQueryLimit);
	}

}
