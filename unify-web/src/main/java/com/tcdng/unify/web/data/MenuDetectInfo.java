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
package com.tcdng.unify.web.data;

/**
 * Menu detect information.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class MenuDetectInfo {

	private String menu;
	
	private String menuItem;

	private boolean privileged;
	
	public MenuDetectInfo(String menu, String menuItem, boolean privileged) {
		this.menu = menu;
		this.menuItem = menuItem;
		this.privileged = privileged;
	}

	public String getMenu() {
		return menu;
	}

	public String getMenuItem() {
		return menuItem;
	}

	public boolean isPrivileged() {
		return privileged;
	}
}
