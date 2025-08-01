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
package com.tcdng.unify.common.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.util.EnumUtils;

/**
 * Workflow item version type enumeration.
 * 
 * @author The Code Department
 * @since 4.1
 */
@StaticList(name = "wfitemversiontypelist", description = "$m{staticlist.wfitemversiontypelist}")
public enum WfItemVersionType implements EnumConst {

    ORIGINAL(
            "ORN"),
    DRAFT(
            "DFT");

    private final String code;

    private WfItemVersionType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return ORIGINAL.code;
    }

    public static WfItemVersionType fromCode(String code) {
        return EnumUtils.fromCode(WfItemVersionType.class, code);
    }

    public static WfItemVersionType fromName(String name) {
        return EnumUtils.fromName(WfItemVersionType.class, name);
    }

}
