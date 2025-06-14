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
package com.tcdng.unify.web.ui.widget.control;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.web.ui.widget.AbstractMultiControl;

/**
 * Title bar with basic controls.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component("ui-titlebar")
@UplAttributes({ @UplAttribute(name = "draggable", type = boolean.class, defaultVal = "false") })
public class TitleBar extends AbstractMultiControl {

    @Override
    public String getStyle() throws UnifyException {
        if (getUplAttribute(boolean.class, "draggable")) {
            if (getUplAttribute(String.class, "style") != null) {
                return getUplAttribute(String.class, "style") + "cursor:move;";
            }

            return "cursor:move;";
        }
        return super.getStyle();
    }

    @Override
    public boolean isLayoutCaption() {
        return false;
    }

    @Override
    protected void doOnPageConstruct() throws UnifyException {
        
    }

}
