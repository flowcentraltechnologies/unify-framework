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
import com.tcdng.unify.core.data.FileAttachmentInfo;
import com.tcdng.unify.core.data.FileAttachmentsInfo;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.ui.widget.Control;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.control.FileAttachment;
import com.tcdng.unify.web.ui.widget.control.FileAttachmentHandler;
import com.tcdng.unify.web.ui.widget.control.FileUpload;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * File attachment writer.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Writes(FileAttachment.class)
@Component("fileattachment-writer")
public class FileAttachmentWriter extends AbstractControlWriter {

	@Override
	protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
		FileAttachment fileAttachment = (FileAttachment) widget;
		writer.write("<div");
		writeTagAttributes(writer, fileAttachment);
		writer.write("><div style=\"display:table;border-collapse:collapse;width:100%;\">");
		String noFilename = getSessionMessage("fileattachment.noname");
		FileAttachmentsInfo fileAttachmentsInfo = fileAttachment.getAttachmentsInfo();
		if (fileAttachmentsInfo != null) {
			FileAttachmentHandler handler = fileAttachment.getFileAttachmentHandler();
			if (handler != null) {
				handler.fillAttachFileNames(fileAttachmentsInfo.getParentId(),
						fileAttachmentsInfo.getAttachmentInfoList());
			}

			boolean isContainerDisabled = fileAttachment.isContainerDisabled();
			boolean isContainerEditable = fileAttachment.isContainerEditable();
			List<ValueStore> valueStoreList = fileAttachment.getValueList();
			FileUpload fileCtrl = fileAttachment.getFileCtrl();
			Control attachCtrl = fileAttachment.getAttachCtrl();
			Control viewCtrl = fileAttachment.getViewCtrl();
			Control removeCtrl = fileAttachment.getRemoveCtrl();
			final boolean adhoc = fileAttachmentsInfo.isAdhoc();
			final boolean disabled = fileAttachmentsInfo.isDisabled();
			int size = valueStoreList.size();
			for (int i = 0; i < size; i++) {
				ValueStore valueStore = valueStoreList.get(i);
				fileCtrl.setValueStore(valueStore);
				attachCtrl.setValueStore(valueStore);
				viewCtrl.setValueStore(valueStore);
				removeCtrl.setValueStore(valueStore);

				FileAttachmentInfo fileAttachmentInfo = (FileAttachmentInfo) valueStore.getValueObject();

				// Row
				if (i % 2 == 0) {
					writer.write("<div class=\"faodd\">");
				} else {
					writer.write("<div class=\"faeven\">");
				}
				writer.write("<div style=\"display:table-cell;\">");

				// Attachment
				writer.writeStructureAndContent(fileCtrl);

				writer.write("<div style=\"display:table;width:100%;\"><div style=\"display:table-row;\">");
				if (!adhoc) {
					String description = fileAttachmentInfo.getDescription();
					if (description == null) {
						description = getSessionMessage("fileattachment.label", i);
					}

					writer.write("<div class=\"falabel\">");
					writer.writeWithHtmlEscape(description);
					writer.write("</div>");
				}

				writer.write("<div style=\"display:table-cell;\">");
				writer.write("<div class=\"facontent\">");
				String filename = fileAttachmentInfo.getFilename();
				if (filename == null) {
					filename = noFilename;
				}

				writer.writeWithHtmlEscape(filename);
				writer.write("</div>");
				writer.write("<div class=\"faaction\">");
				attachCtrl.setDisabled(disabled || isContainerDisabled);
				attachCtrl.setEditable(isContainerEditable);
				writer.writeStructureAndContent(attachCtrl);

				if (!fileAttachmentInfo.isEmpty()) {
					viewCtrl.setDisabled(isContainerDisabled);
					viewCtrl.setEditable(isContainerEditable);
					removeCtrl.setDisabled(disabled || isContainerDisabled);
					removeCtrl.setEditable(isContainerEditable);
				} else {
					viewCtrl.setDisabled(true);
					viewCtrl.setEditable(false);
					removeCtrl.setDisabled(true);
					removeCtrl.setEditable(false);
				}

				writer.writeStructureAndContent(viewCtrl);
				writer.writeStructureAndContent(removeCtrl);
				writer.write("</div>");
				writer.write("</div>");

				writer.write("</div></div>");

				writer.write("</div>");
				writer.write("</div>");
			}
		}

		writer.write("</div></div>");
	}

	@Override
	protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
			throws UnifyException {
		super.doWriteBehavior(writer, widget, handlers);

		// Append rigging
		FileAttachment fileAttachment = (FileAttachment) widget;
		writer.beginFunction("ux.rigFileAttachment");
		writer.writeParam("pId", fileAttachment.getId());
		writer.writeCommandURLParam("pCmdURL");
		String viewPath = fileAttachment.getViewPath();
		if (viewPath != null) {
			writer.writeContextURLParam("pViewURL", viewPath);
		}

		int len = 0;
		List<ValueStore> valueStoreList = fileAttachment.getValueList();
		if (valueStoreList != null) {
			len = valueStoreList.size();
		}

		writer.writeParam("pContId", fileAttachment.getContainerId());
		writer.writeParam("pFileId", fileAttachment.getFileCtrl().getBaseId());
		writer.writeParam("pAttchId", fileAttachment.getAttachCtrl().getBaseId());
		writer.writeParam("pViewId", fileAttachment.getViewCtrl().getBaseId());
		writer.writeParam("pRemId", fileAttachment.getRemoveCtrl().getBaseId());
		writer.writeParam("pLen", len);
		writer.writeParam("pEditable", fileAttachment.isContainerEditable());
		writer.writeParam("pRef", DataUtils.toArray(String.class, writer.getPostCommandRefs()));
		writer.endFunction();
	}
}
