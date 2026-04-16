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
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.data.Input;
import com.tcdng.unify.web.ui.DataTransferBlock;
import com.tcdng.unify.web.ui.widget.AbstractValueListMultiControl;
import com.tcdng.unify.web.ui.widget.Control;

/**
 * A dynamic control for capturing multiple input values with each value
 * captured by a different rendered child control that is determined at runtime.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component("ui-multidynamic")
@UplAttributes({ @UplAttribute(name = "isRequiredSymbol", type = String.class, defaultVal = "*"),
		@UplAttribute(name = "captionSuffix", type = String.class, defaultVal = ":"),
		@UplAttribute(name = "inputStyle", type = String.class) })
public class MultiDynamic extends AbstractValueListMultiControl<MultiDynamic.Item, Input<?>> {

	private DynamicField valueCtrl;

	@Override
	public void populate(DataTransferBlock transferBlock) throws UnifyException {
		if (transferBlock != null) {
			DataTransferBlock dynamicCtrlBlock = transferBlock.getChildBlock();
			Control control = (Control) getChildWidgetInfo(dynamicCtrlBlock.getId()).getWidget();
			control.setValueStore(getValueListStoreAt(dynamicCtrlBlock.getChildBlock().getItemIndex()));
			control.populate(dynamicCtrlBlock);
		}
	}

	public String getCaptionSuffix() throws UnifyException {
		return getUplAttribute(String.class, "captionSuffix");
	}

	public String getInputStyle() throws UnifyException {
		return getUplAttribute(String.class, "inputStyle");
	}

	public String getIsRequiredSymbol() throws UnifyException {
		return getUplAttribute(String.class, "isRequiredSymbol");
	}

	public DynamicField getValueCtrl() {
		return valueCtrl;
	}

	@Override
	protected void doOnPageConstruct() throws UnifyException {
		valueCtrl = (DynamicField) addInternalChildWidget("!ui-dynamic binding:input.value descriptorBinding:input.editor");
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<Input<?>> getItemList() throws UnifyException {
		return (List<Input<?>>) getValue();
	}

	@Override
	protected Item newValue(Input<?> input, int index) throws UnifyException {
		return new Item(input, input.getDescription(), input.isMandatory());
	}

	@Override
	protected void onCreateValueList(List<Item> valueList) throws UnifyException {

	}

	public static class Item {

		private String caption;

		private Input<?> input;

		private boolean required;

		public Item(Input<?> input, String caption, boolean required) {
			this.input = input;
			this.caption = caption;
			this.required = required;
		}

		public Input<?> getInput() {
			return input;
		}

		public String getCaption() {
			return caption;
		}

		public boolean isRequired() {
			return required;
		}
	}
}
