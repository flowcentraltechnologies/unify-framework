/*
 * Copyright 2018-2020 The Code Department.
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

package com.tcdng.unify.core.data;

import com.tcdng.unify.common.util.ParamToken;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Test parameter generator B
 * 
 * @author Lateef Ojulari
 * @since 4.1
 */
@Component(name = "test-gen-b")
public class TestAbstractParamGeneratorB extends AbstractParamGenerator {

    @Override
    public Object generate(ValueStoreReader itemReader, ValueStoreReader parentReader, ParamToken key)
            throws UnifyException {
        return parentReader.read("address2") + " - " + itemReader.read("name");
    }

}
