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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.zwobble.mammoth.DocumentConverter;
import org.zwobble.mammoth.Result;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.constant.FileAttachmentType;
import com.tcdng.unify.core.data.FileAttachmentInfo;
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

	private static final long MAX_USE_ENCODED = 200 * 1024; // 200K

    private static final String INDENT = "    "; // 4 spaces per level

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
			final FileAttachmentType type = preview.getType();
			final UploadedFile uploadFile = preview.getDownloadFile();
			if (type != null) {
				writer.write("<div class=\"ufinfo\">");
				writer.write("<span class=\"ufname\">");
				writer.writeWithHtmlEscape(preview.getFileName());
				writer.write("</span>");
				writer.write("<span class=\"ufbadge\">");
				writer.writeWithHtmlEscape(
						getListItemByKey("fileattachmenttypelist", type.code()).getListDescription());
				writer.write("</span>");
				writer.write("<span class=\"ufsize\">");
				writer.writeWithHtmlEscape(preview.getFileSize());
				writer.write("</span>");
				writer.write("</div>");

				writer.write("<div class=\"ufviewer");
				if (type.isSrcDoc()) {
					writer.write(" ufviewerpad");
				}
				
				writer.write("\">");				
				if (type.isPdf()) {
					if (preview.getFileLength() <= MAX_USE_ENCODED) {
						writer.write(
								"<iframe style=\"width:100%;height:100%;border:none;\" src=\"data:application/pdf;base64,");
						writer.write(Base64.getEncoder().encodeToString(uploadFile.getDataAndInvalidate()));
						writer.write("#toolbar=0&navpanes=0&scrollbar=0\">");
						writer.write("</iframe>");
					} else {
						final FileAttachmentInfo fileAttachmentInfo = new FileAttachmentInfo(type,
								preview.getFileName(), preview.getFileTitle(), preview.getFileName());
						fileAttachmentInfo.setAttachment(uploadFile);
						final String resourceName = getTimestampedResourceName(fileAttachmentInfo.getFilename());
						setSessionAttribute(resourceName, fileAttachmentInfo);
						writer.write("<iframe style=\"width:100%;height:100%;border:none;\" src=\"");
						writer.writeContextResourceURL("/resource/fileattachment", type.mimeType().template(),
								resourceName, null, false, false);
						writer.write("\">");
						writer.write("</iframe>");
					}
				} else if (type.isSrcDoc()) {
					writer.write("<iframe style=\"width:100%;height:100%;border:none;\" srcdoc=\"");
					final String[] styleSheet = previewWidget.getStyleSheet();
					final String[] script = previewWidget.getScript();
					final String html = type.isFullDoc() ? getSrcDocHtml(type, uploadFile)
							: getSrcDocHtml(styleSheet, script, type, preview, uploadFile);
					writer.writeWithHtmlEscape(html);
					writer.write("\">");
					writer.write("</iframe>");
				}

				writer.write("</div>");
			} else {
				// TODO Unknown file type
			}
		} else {
			// TODO
			// No file
		}
		writer.write("</div>");
	}

	private String getSrcDocHtml(FileAttachmentType type, UploadedFile uploadFile) throws UnifyException {
		if (type.isDoc()) {
			// TODO
		}

		return ""; // TODO
	}

	private String getSrcDocHtml(String[] styleSheets, String[] scripts, FileAttachmentType type,
			UploadedFilePreview preview, UploadedFile uploadFile) throws UnifyException {
		ResponseWriter writer = responseWriterPool.getResponseWriter(getRequestContextUtil().getClientRequest());
		try {
			writer.setDirectFuncCall(true);
			writer.write("<!DOCTYPE html>");
			writer.write("<html>");
			// Head
			writer.write("<head>");

			WriterUtils.writeStyleSheet(writer, "$t{css/unify-preview-web.css}");
			Set<String> excludeStyleSheet = new HashSet<String>();
			if (styleSheets != null) {
				for (String styleSheet : styleSheets) {
					if (!excludeStyleSheet.contains(styleSheet)) {
						WriterUtils.writeStyleSheet(writer, styleSheet);
						excludeStyleSheet.add(styleSheet); // Avoid duplication
					}
				}
			}

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
				writeImageHtml(writer, type, preview, uploadFile);
			} else if (type.isCsv()) {
				writeCsvHtml(writer, type, preview, uploadFile);
			} else if (type.isXlsX()) {
				writeXlsxHtml(writer, type, preview, uploadFile);
			} else if (type.isXls()) {
				writeXlsHtml(writer, type, preview, uploadFile);
			} else if (type.isDocX()) {
				writeDocxHtml(writer, type, preview, uploadFile);
			} else if (type.isText()) {
				writeTextHtml(writer, type, preview, uploadFile);
			} else if (type.isXml()) {
				writeXmlHtml(writer, type, preview, uploadFile);
			}

			writer.write("</body>");
			writer.write("</html>");
			return writer.toString();
		} finally {
			responseWriterPool.restore(writer);
		}
	}

	private void writeImageHtml(ResponseWriter writer, FileAttachmentType type, UploadedFilePreview preview,
			UploadedFile uploadFile) throws UnifyException {
		if (preview.getFileLength() <= MAX_USE_ENCODED) {
			writer.write("<img style=\"max-width:100%;\" src=\"data:").write(type.mimeType().template())
					.write(";base64,").write(Base64.getEncoder().encodeToString(uploadFile.getDataAndInvalidate()))
					.write("\" />");
		} else {
			String imageName = "Img_" + preview.getFileName() + '_' + new Date().getTime();
			setSessionAttribute(imageName, uploadFile);
			writer.write("<img style=\"max-width:100%;\" src=\"");
			writer.writeScopeImageContextURL(imageName, true);
			writer.write("\"/>");
		}
	}
	
	private void writeXlsHtml(ResponseWriter writer, FileAttachmentType type, UploadedFilePreview preview,
			UploadedFile uploadFile) throws UnifyException {
		writer.write("<table class=\"xlsxbody\" style=\"width:100%;max-width:100%;\">");
		try (Workbook wb = new HSSFWorkbook(uploadFile.getIn())) {
			org.apache.poi.ss.usermodel.Sheet sheet = wb.getSheetAt(0);
			final int lastRow = sheet.getLastRowNum();
			for (int r = 0; r <= lastRow; r++) {
				org.apache.poi.ss.usermodel.Row row = sheet.getRow(r);
				writer.write("<tr>");
				final int len = row != null ? row.getLastCellNum() : 0;
				for (int i = 0; i < len; i++) {
					writer.write("<td>");
					org.apache.poi.ss.usermodel.Cell cell = row.getCell(i);
					if (cell != null) {
						switch (cell.getCellType()) {
						case BOOLEAN:
							writer.write(String.valueOf(cell.getBooleanCellValue()));
							break;
						case BLANK:
							break;
						case ERROR:
							break;
						case FORMULA:
							writer.write(cell.getCellFormula());
							break;
						case NUMERIC:
							if (DateUtil.isCellDateFormatted(cell)) {
								writer.write(cell.getDateCellValue());
							} else {
								writer.write(BigDecimal.valueOf(cell.getNumericCellValue()));
							}
							break;
						case STRING:
							writer.write(cell.getStringCellValue());
							break;
						default:
							break;
						}
					}
					writer.write("</td>");
				}

				writer.write("</tr>");
			}
		} catch (IOException e) {
			throwOperationErrorException(e);
		}

		writer.write("</table>");
	}

	private void writeXlsxHtml(ResponseWriter writer, FileAttachmentType type, UploadedFilePreview preview,
			UploadedFile uploadFile) throws UnifyException {
		writer.write("<table class=\"xlsxbody\" style=\"width:100%;max-width:100%;\">");
		try (ReadableWorkbook wb = new ReadableWorkbook(uploadFile.getIn());) {
			Sheet sheet = wb.getFirstSheet();
			for (Row row : sheet.read()) {
				writer.write("<tr>");
				final int len = row.getCellCount();
				for (int i = 0; i < len; i++) {
					writer.write("<td>");
					Cell cell = row.getCell(i);
					if (cell != null) {
						switch (cell.getType()) {
						case BOOLEAN:
							writer.write(cell.asString());
							break;
						case EMPTY:
							break;
						case ERROR:
							break;
						case FORMULA:
							writer.write(cell.getFormula());
							break;
						case NUMBER:
							if (isDateFormat(cell.getDataFormatId(), cell.getDataFormatString())) {
								writer.write(cell.asDate());
							} else {
								writer.write(cell.asNumber());
							}
							break;
						case STRING:
							writer.write(cell.asString());
							break;
						default:
							break;
						}
					}
					writer.write("</td>");
				}

				writer.write("</tr>");
			}
		} catch (IOException e) {
			throwOperationErrorException(e);
		}

		writer.write("</table>");
	}
	
	private static boolean isDateFormat(Integer formatId, String format) {
		if (formatId != null && ((formatId >= 14 && formatId <= 22) || (formatId >= 45 && formatId <= 47))) {
			return true;
		}
		if (format == null) {
			return false;
		}

		String f = format.replaceAll("\"[^\"]*\"|\\[[^\\]]*\\]|\\\\.", "").toLowerCase();
		return f.matches(".*[ymdhs].*");
	}
	
	private void writeDocxHtml(ResponseWriter writer, FileAttachmentType type, UploadedFilePreview preview,
			UploadedFile uploadFile) throws UnifyException {
		try (InputStream in = uploadFile.getIn();){
			DocumentConverter converter = new DocumentConverter();
			Result<String> result = converter.convertToHtml(uploadFile.getIn());
			writer.write(result.getValue());
		} catch (IOException e) {
			throwOperationErrorException(e);
		}
	}

	private void writeCsvHtml(ResponseWriter writer, FileAttachmentType type, UploadedFilePreview preview,
			UploadedFile uploadFile) throws UnifyException {
		writer.write("<table class=\"csvbody\" style=\"width:100%;max-width:100%;\">");
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(uploadFile.getIn(), StandardCharsets.UTF_8))) {
			CSVParser parser = CSVFormat.DEFAULT.parse(reader);
			for (CSVRecord record : parser) {
				writer.write("<tr>");
				for (String cell : record) {
					writer.write("<td>").write(cell).write("</td>");
				}

				writer.write("</tr>");
			}
		} catch (Exception e) {
			throwOperationErrorException(e);
		}
		writer.write("</table>");
	}

	private void writeTextHtml(ResponseWriter writer, FileAttachmentType type, UploadedFilePreview preview,
			UploadedFile uploadFile) throws UnifyException {
		writer.write("<pre class=\"txtbody\" style=\"max-width:100%;\">");
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(uploadFile.getIn(), StandardCharsets.UTF_8))) {
			String line;
			while ((line = reader.readLine()) != null) {
				writer.write(line).write("<br/>");
			}
		} catch (Exception e) {
			throwOperationErrorException(e);
		}
		writer.write("</pre>");
	}

	private void writeXmlHtml(ResponseWriter writer, FileAttachmentType type, UploadedFilePreview preview,
			UploadedFile uploadFile) throws UnifyException {
		try (InputStream in = uploadFile.getIn();) {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
			doc.getDocumentElement().normalize();

			StringBuilder sb = new StringBuilder();
			convertNode(doc.getDocumentElement(), sb, 0);
			writer.write(sb.toString());
		} catch (IOException e) {
			throwOperationErrorException(e);
		} catch (SAXException e) {
			throwOperationErrorException(e);
		} catch (ParserConfigurationException e) {
			throwOperationErrorException(e);
		}
	}
	
    private void convertNode(Node node, StringBuilder sb, int depth) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            final String pad = INDENT.repeat(depth);
            final String name = node.getNodeName();

            NodeList children = node.getChildNodes();
            final boolean leaf = children.getLength() == 0
                    || (children.getLength() == 1 && children.item(0).getNodeType() == Node.TEXT_NODE);

            sb.append(pad).append("<div class='xmlnode'>\n");

            if (leaf) {
                sb.append(pad).append(INDENT)
                    .append("<span class='xmltag'>&lt;").append(name).append("&gt;</span>")
                    .append("<span class='xmlval'>").append(node.getTextContent().trim()).append("</span>")
                    .append("<span class='xmltag'>&lt;/").append(name).append("&gt;</span>\n");
            } else {
                sb.append(pad).append(INDENT)
                    .append("<span class='xmltag'>&lt;").append(name).append("&gt;</span>\n");
                for (int i = 0; i < children.getLength(); i++) {
                    convertNode(children.item(i), sb, depth + 1);
                }

                sb.append(pad).append(INDENT)
                    .append("<span class='xmltag'>&lt;/").append(name).append("&gt;</span>\n");
            }

            sb.append(pad).append("</div>\n");
        }
    }

}
