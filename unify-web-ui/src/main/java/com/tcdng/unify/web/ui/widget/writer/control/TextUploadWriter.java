/*
 * Copyright (c) 2018-2026 The Code Department.
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
import com.tcdng.unify.web.ui.widget.control.TextUpload;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Text upload writer.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Writes(TextUpload.class)
@Component("textupload-writer")
public class TextUploadWriter extends AbstractControlWriter {

	@Override
	protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
		TextUpload textUpload = (TextUpload) widget;
		writer.write("<div");
		writeTagAttributes(writer, textUpload);
		writer.write(">");

		// Area
		writer.write("<div>");
		writer.write("<textarea id=\"").write(textUpload.getId()).write("_area\" class=\"tuarea\"");
		int rows = textUpload.getRows();
		if (rows > 0) {
			writer.write(" rows=\"").write(rows).write("\"");
		}

		if (textUpload.isWordWrap()) {
			writer.write(" wrap=\"virtual\"");
		} else {
			writer.write(" wrap=\"off\"");
		}
		
		writer.write(" spellcheck=\"false\"");
		writer.write(" autocomplete=\"nef\"");
		
		writer.write(">");
		
		String val = textUpload.getStringValue();
		if (val != null) {
			writer.writeWithHtmlEscape(val);
		}
		
		writer.write("</textarea>");
		writer.write("</div>");

		// Controls
		if (textUpload.isEditable() && !textUpload.isDisabled()) {
			writer.write("<div>");
			writer.writeStructureAndContent(textUpload.getUploadControl());
			writer.writeStructureAndContent(textUpload.getClearControl());
			writer.write("</div>");
		}

		writer.writeStructureAndContent(textUpload.getFileCtrl());
		writer.write("</div>");
	}

	@Override
	protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
			throws UnifyException {
		super.doWriteBehavior(writer, widget, handlers);

		// Append rigging
		TextUpload textUpload = (TextUpload) widget;
		writer.beginFunction("ux.rigTextUpload");
		writer.writeParam("pId", textUpload.getId());
		writer.writeCommandURLParam("pCmdURL");
		writer.writeParam("pContId", textUpload.getContainerId());
		writer.writeParam("pUplId", textUpload.getUploadControl().getId());
		writer.writeParam("pClrId", textUpload.getClearControl().getId());
		writer.writeParam("pFilId", textUpload.getFileCtrl().getId());
		writer.writeParam("pEditable", textUpload.isEditable() && !textUpload.isDisabled());
		writer.endFunction();
	}

}
