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
package com.tcdng.unify.core.ui;

import java.util.List;

import com.tcdng.unify.core.util.DataUtils;

/**
 * Menu item data object.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class MenuItem {

    private String caption;

    private String privilege;

    private String actionPath;

    private String originPath;

    private List<MenuItem> menuItemList;

    private boolean hidden;

    public MenuItem(String caption, String privilege, String actionPath, String originPath, List<MenuItem> menuItemList,
            boolean hidden) {
        this.caption = caption;
        this.privilege = privilege;
        this.actionPath = actionPath;
        this.originPath = originPath;
        this.menuItemList = DataUtils.unmodifiableList(menuItemList);
        this.hidden = hidden;
    }

    public String getCaption() {
        return caption;
    }

    public String getPrivilege() {
        return privilege;
    }

    public String getActionPath() {
        return actionPath;
    }

    public String getOriginPath() {
        return originPath;
    }

    public List<MenuItem> getMenuItemList() {
        return menuItemList;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isMain() {
        return false;
    }
}
