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
package com.tcdng.unify.common.data;

import java.util.Arrays;
import java.util.List;

/**
 * Unify container property.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class UnifyContainerProperty {

	private String property;
	
	private List<String> valueList;
	
	private boolean important;

	public UnifyContainerProperty(String property, List<String> valueList) {
		this.property = property;
		this.valueList = valueList;
	}

	public UnifyContainerProperty(String property, List<String> valueList, boolean important) {
		this.property = property;
		this.valueList = valueList;
		this.important = important;
	}

	public UnifyContainerProperty(String property, String value) {
		this.property = property;
		this.valueList = Arrays.asList(value);
	}

	public UnifyContainerProperty(String property, String value, boolean important) {
		this.property = property;
		this.valueList = Arrays.asList(value);
		this.important = important;
	}

	public String getProperty() {
		return property;
	}

	public List<String> getValueList() {
		return valueList;
	}

	public boolean isImportant() {
		return important;
	}
		
}
