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
package com.tcdng.unify.convert.converters;

/**
 * A value to integer converter.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class IntegerConverter extends AbstractConverter<Integer> {

	@Override
	protected Integer doConvert(Object value, ConverterFormatter<?> formatter) throws Exception {
		if (value instanceof Number) {
			return Integer.valueOf(((Number) value).intValue());
		}
		if (value instanceof String) {
			String string = ((String) value).trim();
			if (!string.isEmpty()) {
				if (formatter == null || formatter.isArrayFormat()) {
					return string.indexOf('.') >= 0 ? (int) Double.parseDouble(string) : Integer.decode(string);
				}
				return doConvert(formatter.parse(string), null);
			}
		}
		return null;
	}
}
