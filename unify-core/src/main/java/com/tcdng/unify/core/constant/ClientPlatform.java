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

import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Client platform constants.
 * 
 * @author The Code Department
 * @since 4.1
 */
public enum ClientPlatform implements EnumConst {

    DESKTOP("desktop"), MOBILE("mobile"), DEFAULT("desktop");

    private final String code;

    private ClientPlatform(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return DEFAULT.code;
    }

    public static ClientPlatform fromCode(String code) {
        return EnumUtils.fromCode(ClientPlatform.class, code);
    }

    public static ClientPlatform fromName(String name) {
        return EnumUtils.fromName(ClientPlatform.class, name);
    }
}
