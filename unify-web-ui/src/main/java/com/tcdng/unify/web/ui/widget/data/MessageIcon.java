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
package com.tcdng.unify.web.ui.widget.data;

/**
 * Message icons.
 * 
 * @author The Code Department
 * @since 4.1
 */
public enum MessageIcon {

    INFO(0), WARNING(1), ERROR(2), QUESTION(3);

    private final int iconIndex;

    private MessageIcon(int iconIndex) {
        this.iconIndex = iconIndex;
    }

    public int getIconIndex() {
        return iconIndex;
    }

    public static MessageIcon getIconByInt(int iconIndex) {
        switch (iconIndex) {
            case 3:
                return MessageIcon.QUESTION;
            case 2:
                return MessageIcon.ERROR;
            case 1:
                return MessageIcon.WARNING;
            case 0:
            default:
                return MessageIcon.INFO;
        }
    }
}
