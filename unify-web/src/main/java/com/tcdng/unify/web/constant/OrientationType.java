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

package com.tcdng.unify.web.constant;

import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Orientation type.
 * 
 * @author The Code Department
 * @since 4.1
 */
public enum OrientationType implements EnumConst {

    TOP_LEFT("TL"),
    TOP_RIGHT("TR"),
    BOTTOM_LEFT("BL"),
    BOTTOM_RIGHT("BR");

    private final String code;

    private OrientationType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String defaultCode() {
        return TOP_LEFT.code;
    }

    public static OrientationType fromCode(String code) {
        return EnumUtils.fromCode(OrientationType.class, code);
    }

    public static OrientationType fromName(String name) {
        return EnumUtils.fromName(OrientationType.class, name);
    }
}
