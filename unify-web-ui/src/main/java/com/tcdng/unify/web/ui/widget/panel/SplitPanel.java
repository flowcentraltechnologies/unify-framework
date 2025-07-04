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
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.web.ui.widget.AbstractPanel;

/**
 * A split panel containing two (only two) widgets each in a resizable window.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component("ui-splitpanel")
@UplAttributes({ @UplAttribute(name = "minorMin", type = int.class), @UplAttribute(name = "minorMax", type = int.class),
        @UplAttribute(name = "minorDefault", type = int.class, defaultVal = "150"),
        @UplAttribute(name = "vertical", type = boolean.class, defaultVal = "true") })
public class SplitPanel extends AbstractPanel {

    public String getSplitCtrlId() throws UnifyException {
        return getPrefixedId("ctrl");
    }

    public String getMinorWinId() throws UnifyException {
        return getPrefixedId("win");
    }

    public String getMinorPaneId() throws UnifyException {
        return getPrefixedId("minp");
    }

    public String getMajorPaneId() throws UnifyException {
        return getPrefixedId("majp");
    }

    public int getMinorWinMax() throws UnifyException {
        return getUplAttribute(int.class, "minorMax");
    }

    public int getMinorWinMin() throws UnifyException {
        return getUplAttribute(int.class, "minorMin");
    }

    public boolean isVertical() throws UnifyException {
        return getUplAttribute(boolean.class, "vertical");
    }

    public int getMinorWidth() throws UnifyException {
        int minorWidth = getUplAttribute(int.class, "minorDefault");
        if (minorWidth < getMinorWinMin()) {
            minorWidth = getMinorWinMin();
        } else if (minorWidth > getMinorWinMax()) {
            minorWidth = getMinorWinMax();
        }
        return minorWidth;
    }
}
