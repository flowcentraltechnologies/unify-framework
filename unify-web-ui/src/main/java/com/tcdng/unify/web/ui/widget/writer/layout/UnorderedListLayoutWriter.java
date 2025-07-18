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
package com.tcdng.unify.web.ui.widget.writer.layout;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.web.ui.widget.Container;
import com.tcdng.unify.web.ui.widget.Layout;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.layout.UnorderedListLayout;
import com.tcdng.unify.web.ui.widget.writer.AbstractLayoutWriter;

/**
 * Unordered layout writer.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Writes(UnorderedListLayout.class)
@Component("unorderedlistlayout-writer")
public class UnorderedListLayoutWriter extends AbstractLayoutWriter {

    @Override
    public void writeStructureAndContent(ResponseWriter writer, Layout layout, Container container)
            throws UnifyException {
        writer.write("<ul");
        writeTagStyleClass(writer, layout.getStyleClass());
        writeTagStyle(writer, layout.getStyle());
        writer.write(">");
        boolean isAlternate = container.isAlternate();
        for (String longName : container.getLayoutWidgetLongNames()) {
            Widget widget = container.getWidgetByLongName(longName);
            widget.setAlternateMode(isAlternate);
            if (widget.isVisible()) {
                writer.write("<li>");
                writer.writeStructureAndContent(widget);
                writer.write("</li>");
            } else if (widget.isHidden()) {
                writer.writeStructureAndContent(widget);
            }
        }
        writer.write("</ul>");
    }

}
