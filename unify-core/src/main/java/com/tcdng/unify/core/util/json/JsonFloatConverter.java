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
package com.tcdng.unify.core.util.json;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;

/**
 * JSON float value converter.
 * 
 * @author Lateef
 * @since 4.1
 */
public class JsonFloatConverter implements JsonValueConverter<Float> {
    @Override
    public Float read(Class<Float> clazz, JsonValue jsonValue) throws Exception {
        if (jsonValue.isNull()) {
            return null;
        }
        
        return jsonValue.asFloat();
    }

    @Override
    public JsonValue write(Object value) throws Exception {
        return Json.value((Float) value);
    }
}