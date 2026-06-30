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
package com.tcdng.unify.web.ui.widget.control;

import java.io.InputStreamReader;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.data.UploadedFile;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.DataTransferBlock;
import com.tcdng.unify.web.ui.widget.AbstractAutoRefreshMultiControl;
import com.tcdng.unify.web.ui.widget.Control;

/**
 * A text upload control.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component("ui-textupload")
@UplAttributes({ @UplAttribute(name = "rows", type = int.class, defaultVal = "10"),
		@UplAttribute(name = "accept", type = String.class, defaultVal = "text"),
		@UplAttribute(name = "wordWrap", type = boolean.class, defaultVal = "false") })
public class TextUpload extends AbstractAutoRefreshMultiControl {

	private Control fileControl;

	private Control clearControl;

	private Control uploadControl;

	private UploadedFile[] uploadedFile;

	@Action
	public void clear() throws UnifyException {
		setValue(null);
		autoRefresh();
	}

	@Override
	public void populate(DataTransferBlock transferBlock) throws UnifyException {
		super.populate(transferBlock);

		if (uploadedFile != null && uploadedFile.length > 0) {
			setValue(IOUtils.readAll(new InputStreamReader(uploadedFile[0].getIn())));
		} else {
			setValue(null);
		}

		uploadedFile = null;
	}

	public Control getFileCtrl() {
		return fileControl;
	}

	public Control getFileControl() {
		return fileControl;
	}

	public Control getClearControl() {
		return clearControl;
	}

	public Control getUploadControl() {
		return uploadControl;
	}

	public int getRows() throws UnifyException {
		return getUplAttribute(int.class, "rows");
	}

	public boolean isWordWrap() throws UnifyException {
		return getUplAttribute(boolean.class, "wordWrap");
	}

	public UploadedFile[] getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile[] uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	@Override
	public boolean isSupportStretch() throws UnifyException {
		return false;
	}

	@Override
	protected void doOnPageConstruct() throws UnifyException {
		fileControl = (Control) addInternalChildWidget("!ui-fileupload accept:$s{"
				+ getUplAttribute(String.class, "accept") + "} binding:uploadedFile selectOnly:true hidden:true");
		uploadControl = (Control) addInternalChildWidget(
				"!ui-button styleClass:$e{tubutton} caption:$m{button.upload} hint:$m{button.upload} debounce:false");
		clearControl = (Control) addInternalChildWidget(
				"!ui-button styleClass:$e{tubutton-alert} caption:$m{button.clear} hint:$m{button.clear} debounce:false");
	}
}
