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
import com.tcdng.unify.web.ui.widget.AbstractListControl;

/**
 * A list that allows multiple selection.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component("ui-multiselect")
public class MultiSelect extends AbstractListControl {

    @Override
    public boolean isContainerDisabled() throws UnifyException {
        return super.isContainerDisabled() || !isContainerEditable();
    }

    @Override
    public boolean isMultiple() {
        return true;
    }

    @Override
    public boolean isSupportReadOnly() {
        return false;
    }

    public String getFramePanelId() throws UnifyException {
        return getPrefixedId("frm_");
    }

    public String getListPanelId() throws UnifyException {
        return getPrefixedId("lst_");
    }

}
