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
package com.tcdng.unify.web.ui.widget;

import java.util.List;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.BeanValueListStore;
import com.tcdng.unify.core.data.ValueStore;

/**
 * Serves as a base class for item list multi-controls.
 * 
 * @author The Code Department
 * @since 4.1
 */
public abstract class AbstractItemListMultiControl<T> extends AbstractMultiControl {

	private ValueStore valueStore;

	@Override
	public final void updateInternalState() throws UnifyException {
		final List<T> list = getItemList();
		if (valueStore == null || valueStore.isChanged(list)) {
			valueStore = new BeanValueListStore(list);
		}
	}

	public final ValueStore getItemValueStore() throws UnifyException {
		return valueStore;
	}

	public final ValueStore getItemValueStoreAt(int index) throws UnifyException {
		return valueStore.setDataIndex(index);
	}

	@SuppressWarnings("unchecked")
	public final T getItemAt() throws UnifyException {
		return (T) valueStore.getValueObjectAtDataIndex();
	}

	@SuppressWarnings("unchecked")
	public final T getItemAt(int index) throws UnifyException {
		return (T) valueStore.setDataIndex(index).getValueObjectAtDataIndex();
	}

	public final int getItemCount() throws UnifyException {
		return valueStore.size();
	}

	protected abstract List<T> getItemList() throws UnifyException;

}
