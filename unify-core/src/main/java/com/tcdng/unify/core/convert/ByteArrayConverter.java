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

import com.tcdng.unify.convert.converters.AbstractConverter;
import com.tcdng.unify.convert.converters.ConverterFormatter;
import com.tcdng.unify.core.data.UploadedFile;

/**
 * Byte array converter.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class ByteArrayConverter extends AbstractConverter<byte[]> {

    @Override
    protected byte[] doConvert(Object value, ConverterFormatter<?> formatter) throws Exception {
        if (value instanceof byte[]) {
            return (byte[]) value;
        }

        if (value instanceof UploadedFile) {
            return ((UploadedFile) value).getData();
        }

        if (value instanceof UploadedFile[]) {
            return ((UploadedFile[]) value)[0].getData();
        }
        return null;
    }

}
