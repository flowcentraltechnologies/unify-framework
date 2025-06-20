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

package com.tcdng.unify.core.constant;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Network security type list.
 * 
 * @author The Code Department
 * @since 4.1
 */
@StaticList(name = "networksecuritytypelist", description="$m{staticlist.networksecuritytypelist}")
public enum NetworkSecurityType implements EnumConst {

    SSL("S"), TLS("T");

    private final String code;

    private NetworkSecurityType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String defaultCode() {
        return SSL.code;
    }

    public static NetworkSecurityType fromCode(String code) {
        return EnumUtils.fromCode(NetworkSecurityType.class, code);
    }

    public static NetworkSecurityType fromName(String name) {
        return EnumUtils.fromName(NetworkSecurityType.class, name);
    }
}
