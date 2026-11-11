/*
 * Copyright (c) 2018-2026 The Code Department.
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

/**
 * Job.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class Job {

	private Long workItemId;
	
	private String title;

	private int maxAttempts;
	
	public Job(Long workItemId, String title, int maxAttempts) {
		this.workItemId = workItemId;
		this.title = title;
		this.maxAttempts = maxAttempts > 0 ? maxAttempts : 1;
	}

	public Long getWorkItemId() {
		return workItemId;
	}

	public String getTitle() {
		return title;
	}

	public int getMaxAttempts() {
		return maxAttempts;
	}
	
}
