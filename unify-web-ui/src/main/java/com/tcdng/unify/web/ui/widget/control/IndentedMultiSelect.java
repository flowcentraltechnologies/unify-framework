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
package com.tcdng.unify.web.ui.widget.control;

import java.util.List;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.IndentedSelectInfo;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.web.ui.DataTransferBlock;
import com.tcdng.unify.web.ui.widget.AbstractValueListMultiControl;
import com.tcdng.unify.web.ui.widget.Control;

/**
 * Indented multi-select
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component("ui-indentedmultiselect")
public class IndentedMultiSelect extends AbstractValueListMultiControl<ValueStore, IndentedSelectInfo> {

	private Control selectCtrl;

	@Override
	public void populate(DataTransferBlock transferBlock) throws UnifyException {
		if (transferBlock != null) {
			DataTransferBlock nextBlock = transferBlock.getChildBlock();
			getIndentedSelectInfo().get(nextBlock.getItemIndex())
					.setSelected(Boolean.valueOf((String) nextBlock.getValue()));
		}
	}

	@SuppressWarnings("unchecked")
	public List<IndentedSelectInfo> getIndentedSelectInfo() throws UnifyException {
		return (List<IndentedSelectInfo>) getValue(List.class);
	}

	public Control getSelectCtrl() {
		return selectCtrl;
	}

	@Override
	protected List<IndentedSelectInfo> getItemList() throws UnifyException {
		return getIndentedSelectInfo();
	}

	@Override
	protected ValueStore newValue(IndentedSelectInfo item, int index) throws UnifyException {
		return createValueStore(item, index);
	}

	@Override
	protected void onCreateValueList(List<ValueStore> valueList) throws UnifyException {
		
	}

	@Override
	protected void doOnPageConstruct() throws UnifyException {
		selectCtrl = (Control) addInternalChildWidget("!ui-checkbox binding:selected");
	}

}
