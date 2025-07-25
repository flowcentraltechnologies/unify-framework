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
package com.tcdng.unify.core.upl.artifacts;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.upl.AbstractUplComponent;
import com.tcdng.unify.core.upl.UplElementReferences;

/**
 * Complex test UPL component.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component("test-uplelementb")
@UplAttributes({ @UplAttribute(name = "title", type = String.class),
        @UplAttribute(name = "user", type = TestElementA.class),
        @UplAttribute(name = "manyUser", type = TestElementA[].class), @UplAttribute(name = "count", type = long.class),
        @UplAttribute(name = "components", type = UplElementReferences.class) })
public class TestElementB extends AbstractUplComponent {

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }
}
