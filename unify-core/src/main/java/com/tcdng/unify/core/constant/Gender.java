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
import com.tcdng.unify.core.annotation.Tooling;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Gender constants.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Tooling(description="Gender")
@StaticList(name = "genderlist", description="$m{staticlist.genderlist}")
public enum Gender implements EnumConst {

    MALE("M"), FEMALE("F"), OTHER("O");

    private final String code;

    private Gender(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return OTHER.code;
    }

    public static Gender fromCode(String code) {
        return EnumUtils.fromCode(Gender.class, code);
    }

    public static Gender fromName(String name) {
        return EnumUtils.fromName(Gender.class, name);
    }
}
