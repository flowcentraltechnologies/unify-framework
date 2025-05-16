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
package com.tcdng.unify.web.ui.widget.data;

import com.tcdng.unify.core.util.StringUtils;

/**
 * Blurb.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class Blurb {

	private String icon;

	private String caption;

	private String path;

	private String description;

	public Blurb(String icon, String caption, String description, String path) {
		this.icon = icon;
		this.caption = caption;
		this.description = description;
		this.path = path;
	}

	public String getIcon() {
		return icon;
	}

	public String getCaption() {
		return caption;
	}

	public String getDescription() {
		return description;
	}

	public String getPath() {
		return path;
	}

	public String getStyleClass() {
		return isWithPath()? "popdiv": null;
	}
	
	public boolean isWithPath() {
		return !StringUtils.isBlank(path);
	}
}
