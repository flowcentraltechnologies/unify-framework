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
package com.tcdng.unify.web.ui.widget.writer;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.ui.widget.Container;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.Layout;
import com.tcdng.unify.web.ui.widget.PushType;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;

/**
 * Abstract base class for UI container writers.
 * 
 * @author The Code Department
 * @since 4.1
 */
public abstract class AbstractContainerWriter extends AbstractWidgetWriter implements ContainerWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        Container container = (Container) widget;
        writer.write("<div");
        writeTagAttributes(writer, container);
        writer.write(">");
        writeLayoutContent(writer, container);
        writer.write("</div>");
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers) throws UnifyException {
        Container container = (Container) widget;
        super.doWriteBehavior(writer, container, handlers);
        writeContainedWidgetsBehavior(writer, container);
    }

    protected void writeLayoutContent(ResponseWriter writer, Container container) throws UnifyException {
        Layout layout = container.getLayout();
        if (container.isUseLayoutIfPresent() && layout != null) {
            writer.writeStructureAndContent(layout, container);
        } else {
            boolean isSpace = container.isSpace();
            boolean isAlternate = container.isAlternate();
            for (String longName : container.getLayoutWidgetLongNames()) {
                Widget widget = container.getWidgetByLongName(longName);
                widget.setAlternateMode(isAlternate);
                if (widget.isVisible()) {
                    writer.writeStructureAndContent(widget);
                    if (isSpace) {
                        writer.writeHtmlFixedSpace();
                    }
                } else if (widget.isHidden()) {
                    writer.writeStructureAndContent(widget);
                }
            }
        }
    }

    protected void writeContainedWidgetsBehavior(ResponseWriter writer, Container container) throws UnifyException {
        boolean isAlternate = container.isAlternate();
        for (String longName : container.getLayoutWidgetLongNames()) {
            Widget widget = container.getWidgetByLongName(longName);
            if (widget.isVisible() || widget.isHidden() || widget.isBehaviorAlways()) {
                widget.setAlternateMode(isAlternate);
                writer.writeBehavior(widget);
            }
        }
    }

    protected void writeHiddenPush(ResponseWriter writer, Widget widget, PushType type) throws UnifyException {
        writeHidden(writer, widget.getId(), type.getPrefix());
    }

    protected void writeHiddenPush(ResponseWriter writer, String id, PushType type) throws UnifyException {
        writeHidden(writer, id, type.getPrefix());
    }

    protected void writeHidden(ResponseWriter writer, String id, Object value) throws UnifyException {
        writer.write("<input type=\"hidden\"");
        writeTagId(writer, id);
        writeTagValue(writer, value);
        writer.write("/>");
    }
}
