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

    MINUTE("MI", 0),
    HOUR("HR", 0),
    DAY("DY", 0),
    WEEK("WK", 0),
    MONTH("MN", 0),
    YEAR("YR", 0),
    MINUTE_OF_HOUR("MH", 60),
    HOUR_OF_DAY("HD", 24),
    DAY_OF_WEEK("DW", 7),
    DAY_OF_MONTH("DM", 31),
    DAY_OF_YEAR("DR", 366),
    WEEK_OF_YEAR("WR", 52),
    MONTH_OF_YEAR("MR", 12),
    YEAR_OF_DECA_MILLENIUM("YM", -1);

    private final String code;

    private final int fillLength;
    
    private TimeSeriesType(String code, int fillLength) {
        this.code = code;
        this.fillLength = fillLength;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String defaultCode() {
        return DAY.code;
    }

    public boolean zeroBased() {
        return MINUTE_OF_HOUR.equals(this) || HOUR_OF_DAY.equals(this);
    }

    public boolean numericMerged() {
        return fillLength != 0;
    }

    public boolean fill() {
        return fillLength > 0;
    }

    public int fillLength() {
        return fillLength;
    }

    public static TimeSeriesType fromCode(String code) {
        return EnumUtils.fromCode(TimeSeriesType.class, code);
    }

    public static TimeSeriesType fromName(String name) {
        return EnumUtils.fromName(TimeSeriesType.class, name);
    }
}
