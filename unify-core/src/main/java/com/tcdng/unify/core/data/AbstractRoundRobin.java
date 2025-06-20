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
package com.tcdng.unify.core.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for round-robin.
 * 
 * @author The Code Department
 * @since 4.1
 */
public abstract class AbstractRoundRobin<T> {

	private List<T> availableObjects;

    private int position;

    public AbstractRoundRobin(List<T> availableObjects) {
        this.availableObjects = new ArrayList<>(availableObjects);
    }

    public int size() {
    	return availableObjects.size();
    }

    public int position() {
    	return position;
    }
    
    public synchronized T next() {
    	if (availableObjects.isEmpty()) {
    		throw new RuntimeException("No objects in round-robin list.");
    	}
    	
    	T obj = availableObjects.get(position++);
    	if (position == availableObjects.size()) {
    		position = 0;
    	}
    	
    	return obj;
    }
}
