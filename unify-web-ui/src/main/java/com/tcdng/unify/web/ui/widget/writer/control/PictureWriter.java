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
import com.tcdng.unify.web.ui.widget.control.Picture;
import com.tcdng.unify.web.ui.widget.writer.AbstractAutoRefreshMultiControlWriter;

/**
 * Picture writer.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Writes(Picture.class)
@Component("picture-writer")
public class PictureWriter extends AbstractAutoRefreshMultiControlWriter {

	@Override
	protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
		Picture picture = (Picture) widget;
		writer.writeStructureAndContent(picture.getFileCtrl());
		writer.writeStructureAndContent(picture.getImageCtrl());
	}

	@Override
	protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
			throws UnifyException {
		super.doWriteBehavior(writer, widget, handlers);

		// Append rigging
		Picture picture = (Picture) widget;
		writer.beginFunction("ux.rigPhotoUpload");
		writer.writeParam("pId", picture.getId());
		writer.writeCommandURLParam("pCmdURL");
		writer.writeParam("pContId", picture.getContainerId());
		writer.writeParam("pFileId", picture.getFileCtrl().getId());
		writer.writeParam("pImgId", picture.getImageCtrl().getId());
		writer.writeParam("pEditable", picture.isContainerEditable());
		writer.writeParam("pRef", picture.getRefs());
		writer.endFunction();
	}

}
