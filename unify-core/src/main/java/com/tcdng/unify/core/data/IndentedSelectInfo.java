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
package com.tcdng.unify.core.data;

import com.tcdng.unify.core.util.StringUtils;

/**
 * Indented select information.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class IndentedSelectInfo {

    private String code;

    private String caption;

    private String description;

    private int depth;

    private boolean selected;

    public IndentedSelectInfo(String code, String caption, String description, int depth, boolean selected) {
		this.code = code;
		this.caption = caption;
		this.description = description;
		this.depth = depth;
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getDepth() {
		return depth;
	}

	public String getCode() {
		return code;
	}

	public String getCaption() {
		return caption;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return StringUtils.toXmlString(this);
	}

}
