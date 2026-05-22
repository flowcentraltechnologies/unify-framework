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

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;

/**
 * I-frame widget.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component("ui-iframe")
@UplAttributes({
		@UplAttribute(name = "htmlProvider", type = String.class, mandatory = true),
		@UplAttribute(name = "styleSheet", type = String[].class),
		@UplAttribute(name = "script", type = String[].class),
		@UplAttribute(name = "font", type = String[].class) })
public class IFrameWidget extends AbstractWidget {

	@Override
	public final boolean isControl() {
		return false;
	}

	@Override
	public final boolean isPanel() {
		return false;
	}

	public String getHtmlProvider() throws UnifyException {
		return getUplAttribute(String.class, "htmlProvider");
	}

	public String[] getStyleSheet() throws UnifyException {
		return getUplAttribute(String[].class, "styleSheet");
	}

	public String[] getScript() throws UnifyException {
		return getUplAttribute(String[].class, "script");
	}

	public String[] getFont() throws UnifyException {
		return getUplAttribute(String[].class, "font");
	}
}
