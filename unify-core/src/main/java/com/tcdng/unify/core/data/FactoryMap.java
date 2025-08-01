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

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UnifyOperationException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * An abstract generic factory map. A factory map is a key-to-value map with no
 * put method. It is populated internally on invocation of its
 * {@link #get(Object, Object...)} method.
 * 
 * @author The Code Department
 * @since 4.1
 */
public abstract class FactoryMap<T, U> {

	private final Object accessKey = new Object();

	private Map<T, U> map;

	private boolean checkStale;

	public FactoryMap() {
		this(false);
	}

	protected FactoryMap(boolean checkStale) {
		map = new ConcurrentHashMap<T, U>();
		this.checkStale = checkStale;
	}

	/**
	 * Finds a value by supplied key.
	 * 
	 * @param key the value key
	 * @return the value identified by the supplied key
	 * @throws UnifyException if an error occurs
	 */
	public U find(T key) throws UnifyException {
		if (key == null) {
			throw new IllegalArgumentException("Parameter key can not be null!");
		}

		return map.get(key);
	}

	/**
	 * Gets a value by supplied key. Functions like {@link #get(Object, Object...)}
	 * with no parameters.
	 * 
	 * @param key the value key
	 * @return the value identified by the supplied key
	 * @throws UnifyException if an error occurs
	 */
	public U get(T key) throws UnifyException {
		if (key == null) {
			throw new IllegalArgumentException("Parameter key can not be null!");
		}

		return get(key, DataUtils.ZEROLEN_OBJECT_ARRAY);
	}

	/**
	 * Gets a value by supplied key. The methods checks for the value that maps to
	 * the supplied key and returns it. If no key-value mapping is found, the
	 * protected {@link #create(Object, Object...)} method is called to create a
	 * value for the supplied key. The new key-value mapping is stored and created
	 * value is returned.
	 * 
	 * @param key    the value key
	 * @param params optional value creation parameters
	 * @return the value identified by the supplied key
	 * @throws UnifyException if an error occurs
	 */
	public U get(T key, Object... params) throws UnifyException {
		if (key == null) {
			throw new IllegalArgumentException("Parameter key can not be null!");
		}

		try {
			U value = map.get(key);
			if (value != null) {
				if (checkStale && stale(key, value)) {
					remove(key);
					value = null;
				}
			}

			if (value == null) {
				synchronized (accessKey) {
					value = map.get(key);
					if (value == null) {
						value = create(key, params);
						if (value != null && onCreate(value)) {
							put(key, value);
						}
					}
				}
			}
			return value;
		} catch (UnifyException e) {
			throw e;
		} catch (Exception e) {
			throw new UnifyOperationException(e, getClass().getSimpleName());
		}
	}

	/**
	 * Clears all the key-value mappings.
	 */
	public void clear() {
		map.clear();
	}

	/**
	 * Removes an element from the map by key.
	 * 
	 * @param key the key of the value to remove
	 * @return the removed value, otherwise null
	 * @throws UnifyException if an error occurs
	 */
	public U remove(T key) throws UnifyException {
		if (key == null) {
			throw new IllegalArgumentException("Parameter key can not be null!");
		}

		U val = map.remove(key);
		try {
			onRemove(val);
		} catch (Exception e) {
			throw new UnifyOperationException(e, getClass().getSimpleName());
		}
		return val;
	}

	/**
	 * Returns a set of all the keys in this map.
	 * 
	 * @return a set of keys
	 */
	public Set<T> keySet() {
		return map.keySet();
	}

	public Set<Map.Entry<T, U>> entrySet() {
		return map.entrySet();
	}

	/**
	 * Returns a collection of all the values in this map.
	 * 
	 * @return the collection of values
	 */
	public Collection<U> values() {
		return map.values();
	}

	/**
	 * Returns the size of this map.
	 */
	public int size() {
		return map.size();
	}

	/**
	 * Returns true if map is empty.
	 */
	public boolean isEmpty() {
		return map.isEmpty();
	}

	/**
	 * Returns true if factory map contains entry that matches supplied key.
	 * 
	 * @param key the key to match
	 */
	public boolean isKey(T key) {
		return map.containsKey(key);
	}

	/**
	 * Creates a value for specified key using supplied parameters.
	 * 
	 * @param key    the value key
	 * @param params optional parameters
	 * @return object that would be mapped to supplied key
	 * @throws Exception
	 */
	protected abstract U create(T key, Object... params) throws Exception;

	/**
	 * Presets a key value.
	 * 
	 * @param key   the key
	 * @param value the value to set
	 * @throws Exception if an error occurs
	 */
	protected void preset(T key, U value) throws Exception {
		put(key, value);
	}

	/**
	 * Returns true is value is stale.
	 * 
	 * @param key   the value's key
	 * @param value the value to check
	 * @throws Exception if an error occurs
	 */
	protected boolean stale(T key, U value) throws Exception {
		return false;
	}

	/**
	 * Puts a factory value.
	 * 
	 * @param key   the value's key
	 * @param value the value to put
	 * @return the replaced value otherwise null
	 * @throws Exception if an error occurs
	 */
	protected U put(T key, U value) throws Exception {
		onPut(value);
		return map.put(key, value);
	}

	/**
	 * Executes after creation of object.
	 * 
	 * @param value the value to test
	 * @return a true if value is to be retained
	 * @throws Exception if an error occurs
	 */
	protected boolean onCreate(U value) throws Exception {
		return true;
	}

	/**
	 * Executes after put of object.
	 * 
	 * @param value the value to test
	 * @return a true if value is to be retained
	 * @throws Exception if an error occurs
	 */
	protected void onPut(U value) throws Exception {

	}

	/**
	 * Perform method on removed item.
	 * 
	 * @param value the value removed
	 * @throws Exception if an error occurs
	 */
	protected void onRemove(U value) throws Exception {

	}

}
