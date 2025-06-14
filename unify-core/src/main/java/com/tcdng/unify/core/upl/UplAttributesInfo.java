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
package com.tcdng.unify.core.upl;

import java.util.Map;
import java.util.Set;

/**
 * UPL component UPL attributes information.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class UplAttributesInfo {
    private Map<String, UplAttributeInfo> uplAttributeInfoMap;

    public UplAttributesInfo(String componentName, Map<String, UplAttributeInfo> uplAttributeInfoMap) {
        this.uplAttributeInfoMap = uplAttributeInfoMap;
    }

    public Set<String> getAttributes() {
        return uplAttributeInfoMap.keySet();
    }

    public boolean isAttribute(String name) {
        return uplAttributeInfoMap.containsKey(name);
    }

    public UplAttributeInfo getUplAttributeInfo(String attributeName) {
        return uplAttributeInfoMap.get(attributeName);
    }

    public int size() {
        return uplAttributeInfoMap.size();
    }
}
