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

package com.tcdng.unify.core.criterion;

/**
 * An aggregate function.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class AggregateFunction {

    private AggregateType type;
    
    private String fieldName;

    public AggregateFunction(AggregateType type, String fieldName) {
        this.type = type;
        this.fieldName = fieldName;
    }

    public AggregateType getType() {
        return type;
    }

	public String getFieldName() {
		return fieldName;
	}
    
}
