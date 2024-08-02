/*
 * Copyright 2018-2024 The Code Department.
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
package com.tcdng.unify.core.util.xml.adapter;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Comma-separated string attribute XML adapter.
 * 
 * @author The Code Department
 * @since 1.0
 */
public class CsvXmlAdapter {
    
    public static class Serializer extends JsonSerializer<String[]> {

		@Override
		public void serialize(String[] value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			gen.writeString(StringUtils.buildCommaSeparatedString(value));
		}
    	
    }
    
    public static class Deserializer extends JsonDeserializer<String[]> {

		@Override
		public String[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			String val = p.getText();
		    String[] items = StringUtils.commaSplit(val);
		    if (items != null) {
		        for(int i = 0; i < items.length; i++) {
		            items[i] = items[i].trim();
		        }
		    }
		    
		    return items;
		}
    	
    }

}
