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

	WRAPPER("wrapper-category"),
	CORE("core-category"),
	ALL("all-category");
	
	public static final String CORE_PREFIX = "co:";
	public static final String WRAPPER_PREFIX = "wo:";

	private final String id;
	
	private BundledCatType(String id) {
		this.id = id;
	}
	
	public String id() {
		return id;
	}

	public boolean isCore() {
		return CORE.equals(this);
	}

	public boolean isWrapper() {
		return WRAPPER.equals(this);
	}
	
	public boolean isWithPrefix() {
		return CORE.equals(this) || WRAPPER.equals(this);
	}
	
	public static BundledCatType fromPrefix(String val) {
		if (val != null) {
			if (val.startsWith(CORE_PREFIX)) {
				return CORE;
			}
			
			if (val.startsWith(WRAPPER_PREFIX)) {
				return WRAPPER;
			}
		}
		
		return null;
	}
	
	public static String stripPrefix(String val) {
		if (val != null) {
			if (val.startsWith(CORE_PREFIX)) {
				return val.substring(CORE_PREFIX.length());
			}
			
			if (val.startsWith(WRAPPER_PREFIX)) {
				return val.substring(WRAPPER_PREFIX.length());
			}
		}
		
		return val;
	}
}
