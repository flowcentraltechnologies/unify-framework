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
package com.tcdng.unify.web.ui.widget.writer;

import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.constant.FileAttachmentType;
import com.tcdng.unify.core.data.UploadedFile;
import com.tcdng.unify.core.data.UploadedFilePreview;
import com.tcdng.unify.web.ui.util.WriterUtils;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.ResponseWriterPool;
import com.tcdng.unify.web.ui.widget.UploadedFilePreviewWidget;
import com.tcdng.unify.web.ui.widget.Widget;

/**
 * Uploaded file preview writer.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Writes(UploadedFilePreviewWidget.class)
@Component("uploadedfilepreview-writer")
public class UploadedFilePreviewWriter extends AbstractWidgetWriter {

	@Configurable
	private ResponseWriterPool responseWriterPool;

	@Override
	protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
		final UploadedFilePreviewWidget previewWidget = (UploadedFilePreviewWidget) widget;
		writer.write("<div");
		writeTagAttributes(writer, previewWidget);
		writer.write(">");

		final UploadedFilePreview preview = previewWidget.getUploadedFilePreview();
		if (preview != null) {
			final UploadedFile uploadFile = preview.getDownloadFile();
			FileAttachmentType type = FileAttachmentType.detectFromFileName(uploadFile.getFilename());
			if (type != null) {
				if (type.isImage()) {
					writer.write("<iframe style=\"width:100%;height:100%;border:none;\" srcdoc=\"");
					final String[] styleSheet = previewWidget.getStyleSheet();
					final String[] script = previewWidget.getScript();
					final String html = getSrcDocHtml(styleSheet, script, type, uploadFile);
					writer.writeWithHtmlEscape(html);
					writer.write("\">");
					writer.write("</iframe>");
				}
			} else {
				// TODO Unknown file type
			}
		} else {
			// TODO
			// No file
		}
		writer.write("</div>");
	}

	private String getSrcDocHtml(String[] styleSheets, String[] scripts, FileAttachmentType type,
			UploadedFile uploadFile) throws UnifyException {
		ResponseWriter writer = responseWriterPool.getResponseWriter(getRequestContextUtil().getClientRequest());
		try {
			writer.setDirectFuncCall(true);
			writer.write("<!DOCTYPE html>");
			writer.write("<html>");

			// Head
			writer.write("<head>");

			// Style sheet links
			Set<String> excludeStyleSheet = new HashSet<String>();
			if (styleSheets != null) {
				for (String styleSheet : styleSheets) {
					if (!excludeStyleSheet.contains(styleSheet)) {
						WriterUtils.writeStyleSheet(writer, styleSheet);
						excludeStyleSheet.add(styleSheet); // Avoid duplication
					}
				}
			}

			// Write javascript sources
			Set<String> excludeScripts = new HashSet<String>();
			if (scripts != null) {
				for (String script : scripts) {
					if (!excludeScripts.contains(script)) {
						WriterUtils.writeJavascript(writer, script, null);
						excludeScripts.add(script); // Avoid duplication
					}
				}
			}

			writer.write("</head>");

			// Body
			writer.write("<body>");
			if (type.isImage()) {
				writeImageHtml(writer, type, uploadFile);
			}
			
			writer.write("</body>");

			writer.write("</html>");

			return writer.toString();
		} finally {
			responseWriterPool.restore(writer);
		}
	}
	
	private void writeImageHtml(ResponseWriter writer, FileAttachmentType type, UploadedFile uploadFile)
			throws UnifyException {
		writer.write("<img style=\"max-width:100%;\" src=\"data:").write(type.mimeType().template()).write(";base64,")
				.write(Base64.getEncoder().encodeToString(uploadFile.getDataAndInvalidate())).write("\" />");
	}
}
