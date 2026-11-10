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

import java.util.Collection;

import com.tcdng.unify.core.criterion.CompoundRestriction;
import com.tcdng.unify.core.database.Query;

/**
 * Convenient query class for sequence entities.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class SequenceEntityQuery<T extends SequenceEntity> extends Query<T> {

	public SequenceEntityQuery(Class<T> entityClass) {
		super(entityClass);
	}

	public SequenceEntityQuery(Class<T> entityClass, boolean applyAppQueryLimit) {
		super(entityClass, applyAppQueryLimit);
	}

	public SequenceEntityQuery(Class<T> entityClass, CompoundRestriction restrictions, boolean applyAppQueryLimit) {
		super(entityClass, restrictions, applyAppQueryLimit);
	}

	public SequenceEntityQuery<T> id(Long id) {
		return (SequenceEntityQuery<T>) addEquals("id", id);
	}

	public SequenceEntityQuery<T> idNot(Long id) {
		return (SequenceEntityQuery<T>) addNotEquals("id", id);
	}

	public SequenceEntityQuery<T> idIn(Collection<Long> ids) {
		return (SequenceEntityQuery<T>) addAmongst("id", ids);
	}

	public SequenceEntityQuery<T> idNotIn(Collection<Long> ids) {
		return (SequenceEntityQuery<T>) addNotAmongst("id", ids);
	}

	public SequenceEntityQuery<T> reserved() {
		return (SequenceEntityQuery<T>) addLessThan("id", 0L);
	}

	public SequenceEntityQuery<T> notReserved() {
		return (SequenceEntityQuery<T>) addGreaterThan("id", 0L);
	}

	public SequenceEntityQuery<T> orderById() {
		return (SequenceEntityQuery<T>) addOrder("id");
	}

	public SequenceEntityQuery<T> selectId() {
		return (SequenceEntityQuery<T>) addSelect("id");
	}
}
