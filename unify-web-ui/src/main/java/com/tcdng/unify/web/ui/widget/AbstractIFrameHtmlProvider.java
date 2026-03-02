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
package com.tcdng.unify.web.ui.widget;

import java.util.HashSet;
import java.util.Set;

import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.ui.util.WriterUtils;

/**
 * Convenient abstract base class for I-frame HTML providers.
 * 
 * @author The Code Department
 * @since 4.1
 */
public abstract class AbstractIFrameHtmlProvider extends AbstractUnifyComponent implements IFrameHtmlProvider {

	@Override
	public void generateHtml(ResponseWriter writer, String[] styleSheets, String[] scripts, String[] font) throws UnifyException {
		writer.write("<!DOCTYPE html>");
		writer.write("<html>");
		
		// Head
		writer.write("<head>");
		
		// Style sheet links
		WriterUtils.writeStyleSheet(writer, "$t{css/unify-web.css}");
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
		WriterUtils.writeJavascript(writer, "web/js/unify-web.js", null);
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
		writeHtml(writer);
		writer.write("</body>");

		// Scripts
		writer.write("<script>");
		writeScript(writer);
		writer.write("</script>");

		writer.write("</html>");
	}

	@Override
	protected void onInitialize() throws UnifyException {

	}

	@Override
	protected void onTerminate() throws UnifyException {

	}

	/**
	 * Writes the document HTML
	 * 
	 * @param writer the writer component
	 * @throws UnifyException if an error occurs
	 */
	protected abstract void writeHtml(ResponseWriter writer) throws UnifyException;

	/**
	 * Writes the document scripts
	 * 
	 * @param writer the writer component
	 * @throws UnifyException if an error occurs
	 */
	protected abstract void writeScript(ResponseWriter writer) throws UnifyException;
}
