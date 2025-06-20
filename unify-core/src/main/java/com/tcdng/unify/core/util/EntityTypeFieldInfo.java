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

package com.tcdng.unify.core.util;

import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.constant.DynamicEntityFieldType;

/**
 * Entity type field information.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class EntityTypeFieldInfo {

	private DynamicEntityFieldType type;

	private DataType dataType;

	private String parentEntityName;

	private String childEntityName;

	private String name;

	private String jsonName;

	private String column;

	private String sample;

	private boolean array;
	
	public EntityTypeFieldInfo(DynamicEntityFieldType type, DataType dataType, String parentEntityName, String name,
			String jsonName, String column, String sample, boolean array) {
		this.type = type;
		this.dataType = dataType;
		this.parentEntityName = parentEntityName;
		this.name = name;
		this.jsonName = jsonName;
		this.column = column;
		this.sample = sample;
		this.array = array;
	}

	public EntityTypeFieldInfo(DynamicEntityFieldType type, String childEntityName, String name, String jsonName) {
		this.type = type;
		this.childEntityName = childEntityName;
		this.name = name;
		this.jsonName = jsonName;
		this.array = false;
	}

	public DynamicEntityFieldType getType() {
		return type;
	}

	public DataType getDataType() {
		return dataType;
	}

	public String getParentEntityName() {
		return parentEntityName;
	}

	public String getChildEntityName() {
		return childEntityName;
	}

	public String getName() {
		return name;
	}

	public String getJsonName() {
		return jsonName;
	}

	public String getColumn() {
		return column;
	}

	public String getSample() {
		return sample;
	}

	public boolean isArray() {
		return array;
	}

	public String toString() {
		return StringUtils.toXmlString(this);
	}
}
