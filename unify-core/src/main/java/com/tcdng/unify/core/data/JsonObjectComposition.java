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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tcdng.unify.common.constants.StandardFormatType;
import com.tcdng.unify.convert.FormatContext;

/**
 * JSON object composition.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class JsonObjectComposition {

	private FormatContext formatContext;

	private String name;

	private StandardFormatType dateFormatter;

	private StandardFormatType dateTimeFormatter;

	private List<JsonFieldComposition> fields;

	private Map<String, JsonFieldComposition> map;
	
	public JsonObjectComposition(String name, List<JsonFieldComposition> fields, StandardFormatType dateFormatter,
			StandardFormatType dateTimeFormatter) {
		this.formatContext = new FormatContext();
		this.name = name;
		this.dateFormatter = dateFormatter;
		this.dateTimeFormatter = dateTimeFormatter;
		this.fields = fields;
		this.map = new HashMap<String, JsonFieldComposition>();
		for (JsonFieldComposition comp: fields) {
			this.map.put(comp.getName(), comp);
		}
	}

	public JsonObjectComposition(String name, List<JsonFieldComposition> fields) {
		this.formatContext = new FormatContext();
		this.name = name;
		this.fields = fields;
	}

	public FormatContext getFormatContext() {
		return formatContext;
	}

	public String getName() {
		return name;
	}

	public JsonFieldComposition getComposition(String name) {
		return map.get(name);
	}
	
	public StandardFormatType getDateFormatter() {
		return dateFormatter;
	}

	public boolean isWithDateFormatter() {
		return dateFormatter != null;
	}

	public StandardFormatType getDateTimeFormatter() {
		return dateTimeFormatter;
	}

	public boolean isWithDateTimeFormatter() {
		return dateTimeFormatter != null;
	}

	public List<JsonFieldComposition> getFields() {
		return fields;
	}

}
