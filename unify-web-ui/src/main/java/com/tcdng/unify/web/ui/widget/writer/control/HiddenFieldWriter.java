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
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.control.HiddenField;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Hidden field writer.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Writes(HiddenField.class)
@Component("hiddenfield-writer")
public class HiddenFieldWriter extends AbstractControlWriter {

	@Override
	protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
		HiddenField hiddenField = (HiddenField) widget;
		writer.write("<input type=\"hidden\"");
		writeTagAttributes(writer, hiddenField);
		String value = hiddenField.getStringValue();
		if (value != null) {
			writer.write(" value=\"");
			writer.writeWithHtmlEscape(value);
			writer.write("\"");
		}
		writer.write("/>");
	}

	@Override
	protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
			throws UnifyException {
		super.doWriteBehavior(writer, widget, handlers);
		writeValueAccessor(writer, widget);
	}

}
