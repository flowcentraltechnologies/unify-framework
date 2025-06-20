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
package com.tcdng.unify.web.ui.widget.writer.panel;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.constant.MimeType;
import com.tcdng.unify.web.ui.widget.Container;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.panel.FixedContentPanel;
import com.tcdng.unify.web.ui.widget.writer.AbstractPanelWriter;

/**
 * Fixed content panel writer.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Writes(FixedContentPanel.class)
@Component("fixedcontentpanel-writer")
public class FixedContentPanelWriter extends AbstractPanelWriter {

    @Override
	protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
			throws UnifyException {
		FixedContentPanel fixedContentPanel = (FixedContentPanel) widget;
		writer.beginFunction("ux.rigFixedContentPanel");
		writer.writeParam("pHintPanelId", fixedContentPanel.getHintPanelId());
		writer.writeParam("pBusyIndId", fixedContentPanel.getBusyIndicatorId());
		writer.endFunction();

		super.doWriteBehavior(writer, widget, handlers);
	}

    @Override
    protected void writeLayoutContent(ResponseWriter writer, Container container) throws UnifyException {
        FixedContentPanel fixedContentPanel = (FixedContentPanel) container;
        writer.write("<div id=\"").write(fixedContentPanel.getHintPanelId()).write("\" class=\"fcphint\"></div>");
        writer.write("<div id=\"").write(fixedContentPanel.getBusyIndicatorId()).write("\" class=\"fcpbusy\">");
        writer.write("<img class=\"fcpimage\" src=\"");
        writer.writeContextResourceURL("/resource/file", MimeType.IMAGE.template(), "$t{images/busy.gif}");
        writer.write("\"></div>");
      
        super.writeLayoutContent(writer, container);
    }

}
