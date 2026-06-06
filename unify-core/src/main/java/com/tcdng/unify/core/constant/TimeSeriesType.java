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
 * Time series type constants.
 * 
 * @author The Code Department
 * @version 1.0
 */
@StaticList(name = "timeseriestypelist", description="$m{staticlist.timeseriestypelist}")
public enum TimeSeriesType implements EnumConst {

    MINUTE("MI", false),
    HOUR("HR", false),
    DAY("DY", false),
    WEEK("WK", false),
    MONTH("MN", false),
    YEAR("YR", false),
    MINUTE_OF_HOUR("MH", true),
    HOUR_OF_DAY("HD", true),
    DAY_OF_WEEK("DW", true),
    DAY_OF_MONTH("DM", true),
    DAY_OF_YEAR("DR", true),
    WEEK_OF_YEAR("WR", true),
    MONTH_OF_YEAR("MR", true),
    YEAR_OF_DECA_MILLENIUM("YM", true);

    private final String code;

    private final boolean numericMerged;
    
    private TimeSeriesType(String code, boolean numericMerged) {
        this.code = code;
        this.numericMerged = numericMerged;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String defaultCode() {
        return DAY.code;
    }

    public boolean numericMerged() {
        return numericMerged;
    }

    public static TimeSeriesType fromCode(String code) {
        return EnumUtils.fromCode(TimeSeriesType.class, code);
    }

    public static TimeSeriesType fromName(String name) {
        return EnumUtils.fromName(TimeSeriesType.class, name);
    }
}
