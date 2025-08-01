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
package com.tcdng.unify.core.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ListNumberData;

/**
 * Zero to nine list command.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component("zerotoninelist")
public class ZeroToNineListCommand extends AbstractZeroParamsListCommand {

    private static final List<Listable> ZERO_TO_NINE;

    static {
        List<Listable> list = new ArrayList<Listable>();
        for (int i = 0; i < 10; i++) {
            list.add(new ListNumberData(i));
        }

        ZERO_TO_NINE = Collections.unmodifiableList(list);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, ZeroParams params) throws UnifyException {
        return ZERO_TO_NINE;
    }
}
