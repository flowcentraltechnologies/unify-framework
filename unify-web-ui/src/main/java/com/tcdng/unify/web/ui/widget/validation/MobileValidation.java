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
package com.tcdng.unify.web.ui.widget.validation;

import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.web.ui.widget.AbstractRegexPageValidation;

/**
 * Mobile page validation.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component("ui-mobilevalidation")
public class MobileValidation extends AbstractRegexPageValidation {

    public MobileValidation() {
        super("mobile", "mobile.regex", "validation.invalidphone");
    }
}
