/*
 * Copyright (c) 2018-2026 The Code Department.
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
 * ClusterJob status constants.
 * 
 * @author The Code Department
 * @version 1.0
 */
@StaticList(name = "jobstatuslist", description="$m{staticlist.jobstatuslist}")
public enum JobStatus implements EnumConst {

    PENDING("PND"),
    RUNNING("RNG"),
    COMPLETED("CMP"),
    ERROR("ERR"),
    DEAD("DED");

    private final String code;

    private JobStatus(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String defaultCode() {
        return PENDING.code;
    }

    public static JobStatus fromCode(String code) {
        return EnumUtils.fromCode(JobStatus.class, code);
    }

    public static JobStatus fromName(String name) {
        return EnumUtils.fromName(JobStatus.class, name);
    }
}
