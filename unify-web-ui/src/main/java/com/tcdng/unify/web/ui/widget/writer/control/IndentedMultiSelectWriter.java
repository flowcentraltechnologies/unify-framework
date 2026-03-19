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

import java.util.List;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.data.IndentedSelectInfo;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.DataUtils;
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
		IndentedMultiSelect multiSelect = (IndentedMultiSelect) widget;
		writer.write("<div");
		writeTagAttributes(writer, multiSelect);
		writer.write(">");

		List<IndentedSelectInfo> infos = multiSelect.getIndentedSelectInfo();
		if (!DataUtils.isBlank(infos)) {
			List<ValueStore> valueStoreList = multiSelect.getValueList();
			Control selectCtrl = multiSelect.getSelectCtrl();
			selectCtrl.setEditable(true);
			selectCtrl.setDisabled(false);
			
			final int size = valueStoreList.size();
			for (int i = 0; i < size; i++) {
				writer.write("<div class=\"inrow ");
				if (i % 2 == 0) {
					writer.write("ineven");
				} else {
					writer.write("inodd");
				}
				
				writer.write("\">");
				IndentedSelectInfo info = infos.get(i);
				ValueStore valueStore = valueStoreList.get(i);
				selectCtrl.setValueStore(valueStore);
				
				for(int j = 0; j < info.getDepth(); j++) {
					writer.write("<span class=\"intab\"></span>");					
				}

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
				
				writer.write("</div>");
				
				addPageAlias(multiSelect.getId(), selectCtrl);
			}
		}

		writer.write("</div>");
	}

	@Override
	protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
			throws UnifyException {
		super.doWriteBehavior(writer, widget, handlers);
		IndentedMultiSelect multiSelect = (IndentedMultiSelect) widget;
		
		List<IndentedSelectInfo> infos = multiSelect.getIndentedSelectInfo();
		if (!DataUtils.isBlank(infos)) {
			List<ValueStore> valueStoreList = multiSelect.getValueList();
			Control selectCtrl = multiSelect.getSelectCtrl();
			selectCtrl.setGroupId(null);
			
			final int size = valueStoreList.size();
			String[] ids = new String[size];
			int[] depths = new int[size];
			for (int i = 0; i < size; i++) {
				ValueStore valueStore = valueStoreList.get(i);
				selectCtrl.setValueStore(valueStore);
				writer.writeBehavior(selectCtrl);
				
				ids[i] = selectCtrl.getId();
				depths[i] = infos.get(i).getDepth();
			}
			
			writer.beginFunction("ux.rigIndentedSelect");
			writer.writeParam("pId", multiSelect.getId());
			writer.writeParam("pContId", multiSelect.getContainerId());
			writer.writeCommandURLParam("pCmdURL");
			writer.writeParam("pSel", ids);
			writer.writeParam("pDep", depths);
			writer.endFunction();
		}		
	}
}
