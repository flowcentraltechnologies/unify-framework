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

import java.util.List;

import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.control.TextOptions;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Text options writer.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Writes(TextOptions.class)
@Component("textoptions-writer")
public class TextOptionsWriter extends AbstractControlWriter {

	@Override
	protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
		final TextOptions textOptions = (TextOptions) widget;
		writer.write("<div");
		writeTagAttributes(writer, textOptions);
		writer.write(">");

		if (textOptions.isActive()) {
			writer.write("<div id=\"").write(textOptions.getFramePanelId())
					.write("\" class=\"toborder\" style=\"overflow-y:auto;overflow-x:hidden;\" tabindex=\"-1\">");
			writer.write("<div id=\"").write(textOptions.getListPanelId()).write("\" class=\"tolist\">");
			final List<? extends Listable> listableList = textOptions.getListables();
			final int len = listableList.size();
			final String[] keys = new String[len];
			for (int i = 0; i < len; i++) {
				final Listable listable = listableList.get(i);
				keys[i] = listable.getListKey();

				writer.write("<a");
				writeTagId(writer, textOptions.getNamingIndexedId(i));
				writer.write(">");
				writer.write("<span>");
				writer.writeWithHtmlEscape(listable.getListDescription());
				writer.write("</span>");
				writer.write("</a>");
			}

			textOptions.getWriteWork().set("keys", keys);
			writer.write("</div>");
			writer.write("</div>");
		}

		writer.write("</div>");
	}

	@Override
	protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
			throws UnifyException {
		super.doWriteBehavior(writer, widget, handlers);

		final TextOptions textOptions = (TextOptions) widget;
		if (textOptions.isActive()) {
			writer.beginFunction("ux.rigTextOptions");
			writer.writeParam("pId", textOptions.getId());
			writer.writeParam("pAlias", textOptions.getAlias());
			writer.writeParam("pFrmId", textOptions.getFramePanelId());
			writer.writeParam("pLstId", textOptions.getListPanelId());
			writer.writeParam("pKeys", textOptions.getWriteWork().get(String[].class, "keys"));
			writer.endFunction();
		}
	}

}
