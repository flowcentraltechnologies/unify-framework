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
import com.tcdng.unify.core.data.UploadedFile;
import com.tcdng.unify.core.resource.PictureHandler;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.DataTransferBlock;
import com.tcdng.unify.web.ui.widget.AbstractAutoRefreshMultiControl;
import com.tcdng.unify.web.ui.widget.Control;

/**
 * A picture control.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component("ui-picture")
@UplAttributes({
	@UplAttribute(name = "handler", type = String.class, mandatory = false),
	@UplAttribute(name = "category", type = String.class),
	@UplAttribute(name = "parentCategory", type = String.class),
	@UplAttribute(name = "parentFieldName", type = String.class) })
public class Picture extends AbstractAutoRefreshMultiControl {

    private Control fileControl;

    private Control imageControl;

    private UploadedFile[] uploadedFile;

    private PictureHandler handler;
    
    @Override
	public void populate(DataTransferBlock transferBlock) throws UnifyException {
		super.populate(transferBlock);
		if (uploadedFile != null && uploadedFile.length > 0) {
			if (isWithHandler()) {
				getHandler().save(uploadedFile[0].getData());
			} else {
				setValue(uploadedFile[0].getData());
			}
		}

		uploadedFile = null;
	}
    
    public Control getFileCtrl() {
        return fileControl;
    }

    public Control getImageCtrl() {
        return imageControl;
    }

    public UploadedFile[] getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile[] uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public PictureHandler getHandler() throws UnifyException{
		if (handler != null) {
			final Long sourceId = getValue(Long.class);
			handler.setSourceId(sourceId);
		}
		
		return handler;
	}

    public boolean isWithHandler() {
    	return handler != null;
    }

    @Override
	public boolean isSupportStretch() throws UnifyException {
		return false;
	}
   
	@Override
	protected void doOnPageConstruct() throws UnifyException {
		final String _handler = getUplAttribute(String.class, "handler");
		if (!StringUtils.isBlank(_handler)) {
			handler = getComponent(PictureHandler.class, _handler);
		}

		fileControl = (Control) addInternalChildWidget(
				"!ui-fileupload accept:$s{image} binding:uploadedFile selectOnly:true hidden:true");
		StringBuilder sb = new StringBuilder();
		if (handler != null) {
			sb.append("!ui-image src:$t{images/camera.png} binding:handler");
			appendUplAttribute(sb, "styleClass");
			appendUplAttribute(sb, "style");
			imageControl = (Control) addInternalChildWidget(sb.toString(), false, false);
		} else {
			sb.append("!ui-image src:$t{images/camera.png}");
			appendUplAttribute(sb, "binding");
			appendUplAttribute(sb, "styleClass");
			appendUplAttribute(sb, "style");
			imageControl = (Control) addInternalChildWidget(sb.toString(), true, false);
		}
	}
}
