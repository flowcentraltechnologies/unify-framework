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

import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Update;

/**
 * Abstract convenience class for entity policy.
 * 
 * @author The Code Department
 * @since 4.1
 */
public abstract class AbstractEntityPolicy<T extends Entity> extends AbstractUnifyComponent implements EntityPolicy<T> {

	private boolean setNow;

	public AbstractEntityPolicy() {
		this(false);
	}

	public AbstractEntityPolicy(boolean setNow) {
		this.setNow = setNow;
	}

	@Override
	public boolean isSetNow() {
		return setNow;
	}

	@Override
	public Object preCreate(T entity, Date now) throws UnifyException {
		return null;
	}

	@Override
	public void preUpdate(T entity, Date now) throws UnifyException {

	}

	@Override
	public void preUpdate(Update update, Date now) throws UnifyException {

	}

	@Override
	public void preDelete(T entity, Date now) throws UnifyException {

	}

	@Override
	public void preQuery(Query<T> query) throws UnifyException {

	}

	@Override
	public void postCreate(T entity, Date now) throws UnifyException {

	}

	@Override
	public void postUpdate(T entity, Date now) throws UnifyException {

	}

	@Override
	public void postUpdate(Update update, Date now) throws UnifyException {

	}

	@Override
	public void postDelete(T entity, Date now) throws UnifyException {

	}

	@Override
	public void onCreateError(T entity) {

	}

	@Override
	public void onUpdateError(T entity) {

	}

	@Override
	public void onDeleteError(T entity) {

	}

	@Override
	protected void onInitialize() throws UnifyException {

	}

	@Override
	protected void onTerminate() throws UnifyException {

	}
}
