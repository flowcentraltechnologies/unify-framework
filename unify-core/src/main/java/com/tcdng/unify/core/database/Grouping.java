/*
 * Copyright (c) 2018-2026 The Code Department.
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
package com.tcdng.unify.core.database;

import java.util.Date;

import com.tcdng.unify.core.criterion.GroupingFunction;

/**
 * A grouping.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class Grouping {

	private GroupingFunction func;
	
	private Object grouping;

	public Grouping(GroupingFunction func, String grouping) {
		this.func = func;
		this.grouping = grouping;
	}

	public Grouping(GroupingFunction func, Date grouping) {
		this.func = func;
		this.grouping = grouping;
	}

	public String getFieldName() {
		return func.getFieldName();
	}

	public String getFieldLabel() {
		return func.getFieldLabel();
	}
	
	public boolean isWithFieldLabel() {
		return func.isWithFieldLabel();
	}

	public String getAsString() {
		return String.valueOf(grouping);
	}

	public Date getAsDate() {
		return (Date) grouping;
	}

	public boolean isWithGrouping() {
		return grouping != null;
	}

	public boolean isString() {
		return grouping == null || String.class.equals(grouping.getClass());
	}

	public boolean isDate() {
		return grouping != null && Date.class.equals(grouping.getClass());
	}

}