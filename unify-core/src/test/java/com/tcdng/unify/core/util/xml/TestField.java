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

package com.tcdng.unify.core.util.xml;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.util.xml.adapter.DataTypeXmlAdapter;

/**
 * Data object for tests.
 * 
 * @author The Code Department
 * @since 4.1
 */
@JacksonXmlRootElement(localName = "test-field")
@JsonPropertyOrder({ "name", "type" })
public class TestField {

	@JacksonXmlProperty
    private String name;

    @JsonSerialize(using = DataTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = DataTypeXmlAdapter.Deserializer.class)
	@JacksonXmlProperty
    private DataType type;

    public TestField(String name, DataType type) {
        this.name = name;
        this.type = type;
    }

    public TestField() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }
}
