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
package com.tcdng.unify.core.criterion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.list.AbstractZeroParamsListCommand;
import com.tcdng.unify.core.list.ZeroParams;
import com.tcdng.unify.core.util.FilterUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Date field condition list command.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component("datefieldconditionlist")
public class DateFieldConditionListCommand extends AbstractZeroParamsListCommand {

    private final FactoryMap<Locale, List<Listable>> listMap = new FactoryMap<Locale, List<Listable>>()
        {

            @Override
            protected List<Listable> create(Locale locale, Object... arg1) throws Exception {
                List<Listable> list = new ArrayList<Listable>();
                for (FilterConditionType condType : FilterUtils.getSupportedFilterConditionTypes(Date.class,
                        FilterConditionListType.IMMEDIATE_FIELD)) {
                    list.add(getListable(locale, condType));
                }
                return list;
            }

            private Listable getListable(Locale locale, FilterConditionType filterConditionType) throws UnifyException {
                return new ListData(filterConditionType.code(),
                        getMessage(locale, StringUtils.dotify("date", filterConditionType.labelKey())));
            }
        };

    @Override
    public List<? extends Listable> execute(Locale locale, ZeroParams zeroParams) throws UnifyException {
        return listMap.get(locale);
    }

}
