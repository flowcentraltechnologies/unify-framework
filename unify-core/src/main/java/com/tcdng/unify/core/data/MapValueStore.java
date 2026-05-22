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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.tcdng.unify.convert.util.ConverterUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.format.Formatter;

/**
 * A map value store.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class MapValueStore extends AbstractSingleObjectValueStore<Map<String, Object>> {

	private final Map<String, DataType> dataTypes;

	public MapValueStore(Map<String, Object> map) {
		this(map, null, -1);
	}

	public MapValueStore(Map<String, DataType> dataTypes, Map<String, Object> map) {
		this(dataTypes, map, null, -1);
	}

	@Override
	public final boolean isMap() {
		return true;
	}

	public MapValueStore(Map<String, Object> map, String dataMarker, int dataIndex) {
		this(null, map, dataMarker, dataIndex);
	}

	public MapValueStore(Map<String, DataType> dataTypes, Map<String, Object> map, String dataMarker, int dataIndex) {
		super(map, dataMarker, dataIndex);
		this.dataTypes = Collections
				.unmodifiableMap(dataTypes == null ? Collections.emptyMap() : new HashMap<String, DataType>(dataTypes));
	}

	@Override
	public boolean isGettable(String name) throws UnifyException {
		return isTempValue(name) || (storage != null && storage.containsKey(name));
	}

	@Override
	public boolean isSettable(String name) throws UnifyException {
		return storage != null && storage.containsKey(name);
	}

	@Override
	protected final Object doRetrieve(String property) throws UnifyException {
		Object val = getTempValue(property);
		return val == null ? storage.get(property) : val;
	}

	@Override
	protected final void doStore(String property, Object value, Formatter<?> formatter) throws UnifyException {
		if (isSettable(property)) {
			DataType dataType = dataTypes.get(property);
			if (dataType != null) {
				try {
					value = ConverterUtils.convert(dataType.javaClass(), value);
				} catch (Exception e) {
				}
			}

			storage.put(property, value);
		}
	}
}
