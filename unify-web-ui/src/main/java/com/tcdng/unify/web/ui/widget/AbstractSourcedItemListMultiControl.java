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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Serves as a base class for sourced item list multi-controls.
 * 
 * @author The Code Department
 * @since 4.1.16
 */
public abstract class AbstractSourcedItemListMultiControl<T, U> extends AbstractItemListMultiControl<T> {

	private List<U> oldSourceList;
	
	private List<T> oldItemList;

	@Override
	public void updateInternalState() throws UnifyException {
		final List<U> sourceList = getSourceList();
		if (oldItemList == null || !DataUtils.equals(oldSourceList, sourceList)) {
			oldSourceList = sourceList != null ? new ArrayList<U>(sourceList) : sourceList;
			if (!DataUtils.isBlank(oldSourceList)) {
				oldItemList = new ArrayList<T>();
				int i = 0;
				for (U source: oldSourceList) {
					oldItemList.add(createItem(source, i++));
				}
			} else {
				oldItemList = Collections.emptyList();
			}
			
			onCreateItemList(oldItemList);
		}
		
		super.updateInternalState();
	}

    public void clear() throws UnifyException {
    	oldSourceList = null;;
    }

	@Override
	protected List<T> getItemList() throws UnifyException {
		if (oldItemList == null) {
			updateInternalState();
		}
		
		return oldItemList;
	}

	protected abstract void onCreateItemList(List<T> oldItemList) throws UnifyException;

	protected abstract T createItem(U sourceObj, int index) throws UnifyException;
	
	protected abstract List<U> getSourceList() throws UnifyException;
	
}
