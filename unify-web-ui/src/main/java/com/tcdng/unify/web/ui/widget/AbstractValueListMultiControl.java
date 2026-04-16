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
import java.util.Comparator;
import java.util.List;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.BeanValueListStore;
import com.tcdng.unify.core.data.ValueStore;

/**
 * Serves as a base class for multi-controls that require value list stores.
 * 
 * @author The Code Department
 * @since 4.1
 */
public abstract class AbstractValueListMultiControl<T, U> extends AbstractMultiControl {

    private List<U> oldItemList;

    private List<T> valueList;

    private ValueStore valueStore;
    
    public AbstractValueListMultiControl() {
        valueList = Collections.emptyList();
    }

    public final ValueStore getValueStoreAt(int index) throws UnifyException {
    	getValueList();
    	
    	if (valueStore == null) {
    		synchronized(this) {
    	    	if (valueStore == null) {
    	    		valueStore = new BeanValueListStore(valueList);
    	    	}    			
    		}
    	}
    	
    	return valueStore.setDataIndex(index);
    }

    public final T getValueItem(int index) throws UnifyException {
		getValueList();
		return index >= 0 && index < valueList.size() ? valueList.get(index) : null;
	}
    
    public final int getValueListSize() throws UnifyException {
    	return getValueList().size();
    }
    
    protected final void moveValueItem(int indexTo, int indexFrom) throws UnifyException {
    	T val = getValueList().remove(indexFrom);
    	valueList.add(indexTo, val);
    }
    
    protected final void swapValueItems(int indexA, int indexB) throws UnifyException {
        Collections.swap(getValueList(), indexA, indexB);
    }
    
	protected final void sortValueItems(Comparator<T> comparator) throws UnifyException {
		Collections.sort(getValueList(), comparator);
	}

    protected final T removeValueItem(int index) throws UnifyException {
    	return getValueList().remove(index);
    }
    
    private List<T> getValueList() throws UnifyException {
        List<U> itemList = getItemList();
        if (oldItemList != itemList || (itemList != null && itemList.size() != valueList.size())) {
            if (itemList != null && !itemList.isEmpty()) {
                int size = itemList.size();
                valueList = new ArrayList<T>(itemList.size());
                for (int i = 0; i < size; i++) {
                    valueList.add(newValue(itemList.get(i), i));
                }
            } else {
                valueList = Collections.emptyList();
            }

            oldItemList = itemList;
            onCreateValueList(valueList);
            valueStore = null;
        }
        
        return valueList;
    }

    protected U getItem(int index) {
        if (oldItemList != null && index < oldItemList.size()) {
            return oldItemList.get(index);
        }
        
        return null;
    }
    
    protected void invalidateValueList() {
        oldItemList = null;
        valueList = null;
    }

    protected abstract List<U> getItemList() throws UnifyException;

    protected abstract T newValue(U item, int index) throws UnifyException;

    protected abstract void onCreateValueList(List<T> valueList) throws UnifyException;
}
