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
package com.tcdng.unify.core.convert;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;

import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.convert.converters.AbstractConverter;
import com.tcdng.unify.convert.converters.ConverterFormatter;
import com.tcdng.unify.convert.util.ConverterUtils;
import com.tcdng.unify.core.data.Money;
import com.tcdng.unify.core.format.Formatter;
import com.tcdng.unify.core.upl.UplElementReferences;
import com.tcdng.unify.core.util.StringUtils;

/**
 * A value to string converter.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class StringConverter extends AbstractConverter<String> {

    @SuppressWarnings("unchecked")
    @Override
    protected String doConvert(Object value, ConverterFormatter<?> formatter) throws Exception {
        if (value != null) {
            if (value instanceof String) {
                if (formatter != null && formatter.getDataType().isAssignableFrom(String.class)) {
                    return ((Formatter<Object>) formatter).format(value);
                }

                return (String) value;
            }

            if (value instanceof byte[]) {
            	return Base64.encodeBase64String((byte[]) value);
            }

            if (value instanceof EnumConst) {
                return ((EnumConst) value).code();
            }

            if (value instanceof UplElementReferences) {
                UplElementReferences uer = (UplElementReferences) value;
                StringBuilder sb = new StringBuilder();
                boolean isAppendSymbol = false;
                for (String longName : uer.getLongNames()) {
                    if (isAppendSymbol) {
                        sb.append(',');
                    } else {
                        isAppendSymbol = true;
                    }
                    sb.append(longName);
                }
                return sb.toString();
            }

            if (value instanceof Money) {
                StringBuilder sb = new StringBuilder();
                Money money = (Money) value;
                if (money.getCurrencyCode() != null) {
                    sb.append(money.getCurrencyCode()).append(" ");
                }

                if (money.getAmount() != null) {
                    sb.append(((Formatter<BigDecimal>) formatter).format(money.getAmount()));
                }

                return sb.toString();
            }

            if (formatter == null) {
                if (value instanceof String[]) {
                    return StringUtils.buildCommaSeparatedString((String[]) value, false);
                }
                
                if (value instanceof Date) {
                    formatter = ConverterUtils.getDefaultDateTimeFormatter();
                }
                
                if (formatter == null) {
                    return String.valueOf(value);
                }
            }

            value = ConverterUtils.convert(((Formatter<Object>) formatter).getDataType(), value);
            return ((Formatter<Object>) formatter).format(value);
        }
        return null;
    }
}
