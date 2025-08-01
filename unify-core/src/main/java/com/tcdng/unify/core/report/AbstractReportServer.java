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
package com.tcdng.unify.core.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.openhtmltopdf.pdfboxout.PdfBoxRenderer;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.UnifyCoreErrorConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.database.DataSource;
import com.tcdng.unify.core.database.dynamic.sql.DynamicSqlDataSourceManager;
import com.tcdng.unify.core.format.Formatter;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.IOUtils;

/**
 * Abstract base implementation of a report server. Implements all the
 * requirements of a report server component, with subclasses only needing to
 * implement a single {@link #doGenerateReport(Report, OutputStream)} method.
 * 
 * @author The Code Department
 * @since 4.1
 */
public abstract class AbstractReportServer extends AbstractUnifyComponent
		implements ReportServer, ReportFormatterStore {

	@Configurable(ApplicationComponents.APPLICATION_DATASOURCE)
	private String defaultDatasource;

	@Configurable
	private DynamicSqlDataSourceManager dynamicSqlDataSourceManager;

	private Map<String, ReportTheme> reportThemes;

	private Map<ReportLayoutType, ReportLayoutManager> reportLayoutManagers;

	public AbstractReportServer() {
		reportThemes = new HashMap<String, ReportTheme>();
		reportLayoutManagers = new HashMap<ReportLayoutType, ReportLayoutManager>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Formatter<T> getFormatter(String formatterUpl) throws UnifyException {
		return (Formatter<T>) getSessionLocaleFormatter(formatterUpl);
	}

	@Override
	public void registerReportTheme(String themeName, ReportTheme reportTheme) throws UnifyException {
		reportThemes.put(themeName, reportTheme);
	}

	@Override
	public void registerReportLayoutManager(ReportLayoutType layoutName, ReportLayoutManager reportLayoutManager)
			throws UnifyException {
		reportLayoutManagers.put(layoutName, reportLayoutManager);
	}

	@Override
	public void generateReport(Report report, String filename) throws UnifyException {
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(filename);
			generateReport(report, fileOutputStream);
		} catch (FileNotFoundException e) {
			throwOperationErrorException(e);
		} finally {
			IOUtils.close(fileOutputStream);
		}
	}

	@Override
	public void generateReport(Report report, File file) throws UnifyException {
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file);
			generateReport(report, fileOutputStream);
		} catch (FileNotFoundException e) {
			throwOperationErrorException(e);
		} finally {
			IOUtils.close(fileOutputStream);
		}
	}

	@Override
	public void generateReport(Report report, OutputStream outputStream) throws UnifyException {
		if (report.getDataSource() == null) {
			report.setDataSource(defaultDatasource);
			report.setDynamicDataSource(false);
		}

		if (report.getProcessor() != null) {
			ReportProcessor reportProcessor = ((ReportProcessor) getComponent(report.getProcessor()));
			reportProcessor.process(report);
		}

		if (report.isMultiDocHtmlToPDF()) {
			logDebug("Generating multi-document HTML to PDF report [{0}]...", report.getTitle());
			PDDocument doc = null;
			try {
				doc = new PDDocument();
				List<ReportHtml> embeddedHtmls = report.getEmbeddedHtmls();
				logDebug("Multi-document HTML count is [{0}]...", embeddedHtmls != null ? embeddedHtmls.size() : 0);
				if (!DataUtils.isBlank(embeddedHtmls)) {
					for (ReportHtml html : embeddedHtmls) {
						PdfRendererBuilder builder = new PdfRendererBuilder();
						builder.withHtmlContent(html.getHtml(), html.getResourceBaseUri());
						builder.usePDDocument(doc);
						builder.useFastMode();
						PdfBoxRenderer renderer = builder.buildPdfRenderer();
						renderer.createPDFWithoutClosing();
						renderer.close();
					}
				}

				doc.save(outputStream);
			} catch (IOException e) {
				throwOperationErrorException(e);
			} finally {
				if (doc != null) {
					try {
						doc.close();
					} catch (IOException e) {
						logError(e);
					}
				}
			}
		} else {
			logDebug("Generating report [{0}]...", report.getTitle());
			doGenerateReport(report, outputStream);
		}
	}

	@Override
	protected void onInitialize() throws UnifyException {
		ReportFormatUtils.setReportFormatterStore(this);
	}

	@Override
	protected void onTerminate() throws UnifyException {
		ReportFormatUtils.setReportFormatterStore(null);
	}

	protected DataSource getDataSource(Report report) throws UnifyException {
		if (report.isDynamicDataSource()) {
			return dynamicSqlDataSourceManager.getDataSource(report.getDataSource());
		}

		return (DataSource) getComponent(report.getDataSource());
	}

	protected ReportTheme getReportTheme(String themeName) throws UnifyException {
		ReportTheme reportTheme = reportThemes.get(themeName);
		if (reportTheme == null) {
			return ReportTheme.DEFAULT_THEME;
		}

		return reportTheme;
	}

	protected ReportLayoutManager getReportLayoutManager(ReportLayoutType type) throws UnifyException {
		ReportLayoutManager reportLayoutManager = reportLayoutManagers.get(type);
		if (reportLayoutManager == null) {
			throw new UnifyException(UnifyCoreErrorConstants.REPORTSERVER_NO_AVAILABLE_REPORTLAYOUTMANAGER, type,
					getName());
		}

		return reportLayoutManager;
	}

	protected abstract void doGenerateReport(Report report, OutputStream outputStream) throws UnifyException;
}
