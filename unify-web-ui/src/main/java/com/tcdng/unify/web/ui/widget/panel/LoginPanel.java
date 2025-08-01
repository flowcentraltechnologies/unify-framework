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
package com.tcdng.unify.web.ui.widget.panel;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.AbstractPanel;
import com.tcdng.unify.web.ui.widget.Widget;

/**
 * A basic login panel.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component("ui-loginpanel")
@UplBinding("web/panels/upl/loginpanel.upl")
public class LoginPanel extends AbstractPanel {

    @Action
    @Override
    public void switchState() throws UnifyException {
        super.switchState();
        Widget messageLabel = getWidgetByShortName("loginMsg");
        messageLabel.setVisible(messageLabel.getValue(String.class) != null);
    }
}
