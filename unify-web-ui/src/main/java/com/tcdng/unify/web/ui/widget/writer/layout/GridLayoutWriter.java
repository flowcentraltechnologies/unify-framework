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
package com.tcdng.unify.web.ui.widget.writer.layout;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.web.ui.widget.Container;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.TabularLayout;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.layout.GridLayout;

/**
 * Grid layout writer.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Writes(GridLayout.class)
@Component("gridlayout-writer")
public class GridLayoutWriter extends AbstractTabularLayoutWriter {

    @Override
    protected void writeTableContent(ResponseWriter writer, TabularLayout layout, Container container)
            throws UnifyException {
        GridLayout gridLayout = (GridLayout) layout;
        int columnIndex = 0;
        int rowIndex = 0;
        int columns = gridLayout.getUplAttribute(int.class, "columns");
        if (columns <= 0) {
            columns = 1;
        }

        boolean isAlternate = container.isAlternate();
        for (String longName : container.getLayoutWidgetLongNames()) {
            Widget widget = container.getWidgetByLongName(longName);
            widget.setAlternateMode(isAlternate);
            if (widget.isVisible()) {
                if (columnIndex == 0) {
                    writer.write("<div class=\"lrow\">");
                }
                appendCellContent(writer, gridLayout, widget, rowIndex, columnIndex);
                if (++columnIndex == columns) {
                    writer.write("</div>");
                    rowIndex++;
                    columnIndex = 0;
                }
            } else if (widget.isHidden()) {
                writer.writeStructureAndContent(widget);
            }
        }

        if (columnIndex > 0) {
            while (columnIndex++ < columns) {
                writer.write("<div class=\"lcellm\">&nbsp;</div>");
            }
            writer.write("</div>");
        }
    }

    @Override
	protected void writeRepeatTableContent(ResponseWriter writer, TabularLayout layout, Container container)
			throws UnifyException {
		GridLayout gridLayout = (GridLayout) layout;
		int columnIndex = 0;
		int rowIndex = 0;
		int columns = gridLayout.getColumns();
		if (columns <= 0) {
			columns = 1;
		}

        final Widget widget = container.getRepeatWidget();
		boolean disabled = widget.isDisabled();
		try {
			boolean isAlternate = container.isAlternate();
			for (ValueStore valueStore : container.getRepeatValueStores()) {
				widget.setValueStore(valueStore);
				widget.setAlternateMode(isAlternate);
				if (widget.isVisible()) {
					if (columnIndex == 0) {
						writer.write("<div class=\"lrow\">");
					}
					appendCellContent(writer, gridLayout, widget, rowIndex, columnIndex);
					if (++columnIndex == columns) {
						writer.write("</div>");
						rowIndex++;
						columnIndex = 0;
					}
				} else if (widget.isHidden()) {
					writer.writeStructureAndContent(widget);
				}
			}

			if (columnIndex > 0) {
				while (columnIndex++ < columns) {
					writer.write("<div class=\"lcellm\">&nbsp;</div>");
				}
				writer.write("</div>");
			}
		} finally {
			widget.setDisabled(disabled);
		}
	}

}
