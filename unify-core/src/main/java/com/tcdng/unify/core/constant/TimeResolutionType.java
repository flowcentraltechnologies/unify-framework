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
package com.tcdng.unify.core.constant;

/**
 * Time resolution type constants.
 * 
 * @author The Code Department
 * @version 1.0
 * 
 * @since 4.1.16
 */
public enum TimeResolutionType {
	
	MINUTE(0),
	HOUR(1),
	DAY(2),
	WEEK(3),
	MONTH(4),
	YEAR(5);
	
	private final int resolution;
	
	private TimeResolutionType(int resolution) {
		this.resolution = resolution;
	}
	
	public boolean isMinute() {
		return MINUTE.equals(this);
	}
	
	public boolean isHour() {
		return HOUR.equals(this);
	}
	
	public boolean isDay() {
		return DAY.equals(this);
	}
	
	public boolean isWeek() {
		return WEEK.equals(this);
	}
	
	public boolean isMonth() {
		return MONTH.equals(this);
	}
	
	public boolean isYear() {
		return YEAR.equals(this);
	}
	
	public int resolution() {
		return resolution;
	}
	
	public TimeResolutionType max(TimeResolutionType type) {
		return type != null && type.resolution < this.resolution ? type : this;
	}
}
