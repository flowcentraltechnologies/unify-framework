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
package com.tcdng.unify.core.format;

import java.text.ParseException;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Default long (accounting) formatter implementation.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component(name = "longaccountingformat", description = "$m{format.long.accounting}")
public class LongAccountingFormatterImpl extends AbstractNumberFormatter<Long> implements LongFormatter {

    public LongAccountingFormatterImpl() {
        super(Long.class, NumberType.INTEGER_ACCOUNTING);
    }

    @Override
    protected Long doParse(String string) throws UnifyException {
        try {
			string = ensureParsable(string);
            return Long.valueOf(getNumberFormat().parse(string).longValue());
        } catch (ParseException e) {
            throwOperationErrorException(e);
        }
        return null;
    }

    @Override
    public void setScale(int scale) {

    }
}
