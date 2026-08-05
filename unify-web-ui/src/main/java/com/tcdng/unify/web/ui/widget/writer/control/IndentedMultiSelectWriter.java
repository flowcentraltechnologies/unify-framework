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
import com.tcdng.unify.core.data.IndentedSelectInfo;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.widget.Control;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.control.IndentedMultiSelect;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Indented multi-select writer.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Writes(IndentedMultiSelect.class)
@Component("indentedmultiselect-writer")
public class IndentedMultiSelectWriter extends AbstractControlWriter {

	@Override
	protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
		final IndentedMultiSelect multiSelect = (IndentedMultiSelect) widget;
		writer.write("<div");
		writeTagAttributes(writer, multiSelect);
		writer.write(">");

		final int len = multiSelect.getItemCount();
		final int columns = multiSelect.getColumns();
		final String id = multiSelect.getId();
		if (len > 0) {
			Control selectCtrl = multiSelect.getSelectCtrl();
			selectCtrl.setEditable(true);
			selectCtrl.setDisabled(false);
			for (int i = 0; i < len; i++) {
				writer.write("<div class=\"inrow\">");

				ValueStore valueStore = multiSelect.getItemValueStoreAt(i);
				IndentedSelectInfo info = multiSelect.getItemAt();

				final int depth = info.getDepth();
				for (int j = 0; j < depth; j++) {
					writer.write("<span class=\"intab\"></span>");
				}

				if (info.isRow()) {
					writer.write("<span style=\"display:inline-block;\"><div style=\"display:table;\">");
					boolean rows = true;
					while (rows && i < len) {
						writer.write("<div style=\"display:table-row;\">");
						int j = 0;
						while (i < len && j < columns) {
							valueStore = multiSelect.getItemValueStoreAt(i);
							info = multiSelect.getItemAt();
							if (info.getDepth() == depth && info.isRow()) {
								selectCtrl.setValueStore(valueStore);
								writer.write("<div style=\"display:table-cell;\">");
								writeItem(writer, id, selectCtrl, info);
								writer.write("</div>");
								j++;
								i++;
							} else {
								rows = false;
								i--;
								break;
							}
						}

						while (j < columns) {
							writer.write("<div style=\"display:table-cell;\"></div>");
							j++;
						}
						
						writer.write("</div>");
					}
					
					writer.write("</div></span>");
				} else {
					selectCtrl.setValueStore(valueStore);
					writeItem(writer, id, selectCtrl, info);
				}

				writer.write("</div>");
			}
		}

		writer.write("</div>");
	}

	@Override
	protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
			throws UnifyException {
		super.doWriteBehavior(writer, widget, handlers);
		IndentedMultiSelect multiSelect = (IndentedMultiSelect) widget;

		final int len = multiSelect.getItemCount();
		if (len > 0) {
			Control selectCtrl = multiSelect.getSelectCtrl();
			selectCtrl.setGroupId(null);

			String[] ids = new String[len];
			int[] depths = new int[len];
			for (int i = 0; i < len; i++) {
				final ValueStore valueStore = multiSelect.getItemValueStoreAt(i);
				selectCtrl.setValueStore(valueStore);
				writer.writeBehavior(selectCtrl);

				ids[i] = selectCtrl.getId();
				depths[i] = multiSelect.getItemAt().getDepth();
			}

			writer.beginFunction("ux.rigIndentedSelect");
			writer.writeParam("pId", multiSelect.getId());
			writer.writeParam("pContId", multiSelect.getContainerId());
			writer.writeCommandURLParam("pCmdURL");
			writer.writeParam("pWave", multiSelect.isWave());
			writer.writeParam("pSel", ids);
			writer.writeParam("pDep", depths);
			writer.endFunction();
		}
	}

	private void writeItem(ResponseWriter writer, String id, Control selectCtrl, IndentedSelectInfo info)
			throws UnifyException {
		writer.write("<span class=\"insel\">");
		writer.writeStructureAndContent(selectCtrl);
		writer.write("</span>");

		writer.write("<span class=\"incon\">");
		writer.write("<span class=\"incap\">");
		writer.writeWithHtmlEscape(info.getCaption());
		writer.write("</span>");
		if (!StringUtils.isBlank(info.getDescription())) {
			writer.write("<span class=\"indes\">");
			writer.writeWithHtmlEscape(info.getDescription());
			writer.write("</span>");
		}
		writer.write("</span>");

		addPageAlias(id, selectCtrl);
	}
}
