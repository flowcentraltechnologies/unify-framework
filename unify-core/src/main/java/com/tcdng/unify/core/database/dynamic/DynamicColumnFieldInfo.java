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

package com.tcdng.unify.core.database.dynamic;

import com.tcdng.unify.core.annotation.DynamicFieldType;
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.constant.DynamicEntityFieldType;

/**
 * Dynamic table column field information.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class DynamicColumnFieldInfo extends DynamicFieldInfo {

	private String transformer;

	private String defaultVal;

	private int length;

	private int precision;

	private int scale;

	private boolean nullable;

	private boolean array;

	public DynamicColumnFieldInfo(DynamicFieldType type, String enumClassName, String columnName, String fieldName,
			String mapped, boolean nullable, boolean descriptive, boolean array, boolean tenantId) {
		super(type, DynamicEntityFieldType.FIELD, DataType.STRING, columnName, fieldName, mapped, enumClassName,
				descriptive, tenantId);
		this.nullable = nullable;
		this.array = array;
	}

	public DynamicColumnFieldInfo(DynamicFieldType type, DataType dataType, String columnName, String fieldName,
			String mapped, String transformer, String defaultVal, int length, int precision, int scale,
			boolean nullable, boolean descriptive, boolean array, boolean tenantId) {
		super(type, DynamicEntityFieldType.FIELD, dataType, columnName, fieldName, mapped, null, descriptive, tenantId);
		this.transformer = transformer;
		this.defaultVal = defaultVal;
		this.length = length;
		this.precision = precision;
		this.scale = scale;
		this.nullable = nullable;
		this.array = array;
	}

	public String getTransformer() {
		return transformer;
	}

	public String getDefaultVal() {
		return defaultVal;
	}

	public int getLength() {
		return length;
	}

	public int getPrecision() {
		return precision;
	}

	public int getScale() {
		return scale;
	}

	public boolean isNullable() {
		return nullable;
	}

	public boolean isArray() {
		return array;
	}

}
