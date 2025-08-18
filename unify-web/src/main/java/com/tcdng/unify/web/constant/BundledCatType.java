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

package com.tcdng.unify.web.constant;

/**
 * Bundled category type enumeration.
 * 
 * @author The Code Department
 * @since 4.1
 */
public enum BundledCatType {

	BACKOFFICE("backoffice-category"),
	FRONTOFFICE("frontoffice-category"),
	ALL("all-category");

	private final String id;
	
	private BundledCatType(String id) {
		this.id = id;
	}
	
	public String id() {
		return id;
	}

	public boolean isBackOffice() {
		return BACKOFFICE.equals(this);
	}

	public boolean isFrontOffice() {
		return FRONTOFFICE.equals(this);
	}
	
	public boolean isAll() {
		return ALL.equals(this);
	}
	
	public boolean isWithPrefix() {
		return BACKOFFICE.equals(this) || FRONTOFFICE.equals(this);
	}
	
	public static BundledCatType fromPrefix(String val) {
		if (val != null) {
			if (val.startsWith("bo:")) {
				return BACKOFFICE;
			}
			
			if (val.startsWith("fo:")) {
				return FRONTOFFICE;
			}
		}
		
		return ALL;
	}
	
	public static String stripPrefix(String val) {
		if (val != null) {
			if (val.startsWith("bo:")) {
				return val.substring("bo:".length());
			}
			
			if (val.startsWith("fo:")) {
				return val.substring("fo:".length());
			}
		}
		
		return val;
	}
}
