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
package com.tcdng.unify.core.database;

import com.tcdng.unify.common.constants.EnumConst;

/**
 * Static reference query.
 * 
 * @author The Code Department
 * @version 1.0
 */
public class StaticReferenceQuery extends Query<StaticReference> {

    private Class<? extends EnumConst> enumConstType;

    public StaticReferenceQuery(Class<? extends EnumConst> enumConstType) {
        super(StaticReference.class);
        this.enumConstType = enumConstType;
    }

    public Class<? extends EnumConst> getEnumConstType() {
        return enumConstType;
    }

    public StaticReferenceQuery code(String code) {
        return (StaticReferenceQuery) this.addEquals("code", code);
    }

}
