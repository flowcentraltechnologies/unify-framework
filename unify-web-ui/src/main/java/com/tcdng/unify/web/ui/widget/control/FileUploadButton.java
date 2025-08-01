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

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.constant.FileAttachmentType;
import com.tcdng.unify.core.data.UploadedFile;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.ui.DataTransferBlock;
import com.tcdng.unify.web.ui.widget.AbstractAutoRefreshMultiControl;
import com.tcdng.unify.web.ui.widget.Control;
import com.tcdng.unify.web.ui.widget.UploadControl;
import com.tcdng.unify.web.ui.widget.UploadControlHandler;

/**
 * File upload button.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component("ui-fileuploadbutton")
@UplAttributes({
	@UplAttribute(name = "refresh", type = String[].class),
	@UplAttribute(name = "type", type = FileAttachmentType.class, defaultVal = "wildcard"),
	@UplAttribute(name = "caption", type = String.class, defaultVal = "$m{button.upload}") })
public class FileUploadButton extends AbstractAutoRefreshMultiControl implements UploadControl { 

	private Control fileControl;

	private Control buttonControl;

	private UploadControlHandler uploadHandler;

	private UploadedFile[] uploadedFile;

	@Override
	public void populate(DataTransferBlock transferBlock) throws UnifyException {
		final int targetIndex = getRequestTarget(int.class);
		uploadedFile = (UploadedFile[]) transferBlock.getValue();
		if (uploadedFile != null && uploadedFile.length > 0) {
			if (uploadHandler != null) {
				UploadedFile _uploadedFile = uploadedFile[0];
				uploadHandler.saveUpload(targetIndex, getType(), _uploadedFile.getFilename(), _uploadedFile.getData());
			} else {
				setValue(uploadedFile[0]);
			}
		}

		uploadedFile = null;
		String[] panels = getUplAttribute(String[].class, "refresh");
		if (!DataUtils.isBlank(panels)) {
			commandRefreshPanels(panels);
		}
	}

	@Override
	public void setUploadHandler(UploadControlHandler handler) throws UnifyException {
		this.uploadHandler = handler;
	}

	@Override
	public UploadControlHandler getUploadHandler() throws UnifyException {
		return uploadHandler;
	}

	public Control getFileCtrl() {
		return fileControl;
	}

	public Control getButtonCtrl() {
		return buttonControl;
	}

	public UploadedFile[] getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(UploadedFile[] uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public FileAttachmentType getType() throws UnifyException {
		return getUplAttribute(FileAttachmentType.class, "type");
	}

	@Override
	protected void doOnPageConstruct() throws UnifyException {
		FileAttachmentType type = getType();
		fileControl = (Control) addInternalChildWidget(
				"!ui-fileupload accept:$s{" + type.name() + "} binding:uploadedFile selectOnly:true hidden:true", true,
				false);
		buttonControl = (Control) addInternalChildWidget(
				"!ui-button symbol:$s{file-upload} alwaysValueIndex:true caption:$s{" + getCaption()
						+ "} styleClass:$e{" + getStyleClass() + "}",
				true, false);
	}
}
