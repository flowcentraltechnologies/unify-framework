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
package com.tcdng.unify.web.ui.widget.writer.control;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.web.ui.widget.AbstractMultiControl.ChildWidgetInfo;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.control.TitleBar;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Title bar writer.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Writes(TitleBar.class)
@Component("titlebar-writer")
public class TitleBarWriter extends AbstractControlWriter {

	@Override
	protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
		TitleBar titleBar = (TitleBar) widget;
		writer.write("<div");
		writeTagAttributes(writer, titleBar);
		writer.write(">");
		writer.write("<div class=\"").write(getUserColorStyleClass("base")).write("\">");
		writer.write("<div class=\"tblabel\">");
		writeCaption(writer, titleBar);
		writer.write("</div>");
		writer.write("<div class=\"tbcontrols\">");
		for (ChildWidgetInfo childWidgetInfo : titleBar.getChildWidgetInfos()) {
			if (childWidgetInfo.isExternal() && childWidgetInfo.isPrivilegeVisible()) {
				writer.writeStructureAndContent(childWidgetInfo.getWidget());
			}
		}
		writer.write("</div>");
		writer.write("<div style=\"clear:both;\"></div>");
		writer.write("</div>");
		writer.write("</div>");
	}

	@Override
	protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
			throws UnifyException {
		super.doWriteBehavior(writer, widget, handlers);
		TitleBar titleBar = (TitleBar) widget;
		if (titleBar.getUplAttribute(boolean.class, "draggable")) {
			// Append drag and drop JS
			writer.beginFunction("ux.rigDragAndDropPopup");
			writer.writeParam("pId", titleBar.getId());
			writer.endFunction();
		}

		// Append external controls behavior
		ValueStore valueStore = titleBar.getValueStore();
		for (ChildWidgetInfo childWidgetInfo : titleBar.getChildWidgetInfos()) {
			if (childWidgetInfo.isExternal() && childWidgetInfo.isPrivilegeVisible()) {
				Widget chWidget = childWidgetInfo.getWidget();
				ValueStore origValueStore = chWidget.getValueStore();
				chWidget.setValueStore(valueStore);
				writer.writeBehavior(childWidgetInfo.getWidget());
				chWidget.setValueStore(origValueStore);
			}
		}
	}
}
