/*
 * Copyright 2014 The Code Department
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

/**
 * Aggregate object.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class Aggregate<T> {

	private String fieldName;

	private int count;

	private T value;

	public Aggregate(String fieldName, int count, T value) {
		this.fieldName = fieldName;
		this.count = count;
		this.value = value;
	}

	public String getFieldName() {
		return fieldName;
	}

	public int getCount() {
		return count;
	}

	public T getValue() {
		return value;
	}
}
