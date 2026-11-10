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
package com.tcdng.unify.core.system.entities;

import com.tcdng.unify.core.constant.JobStatus;

/**
 * Cluster job query.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class ClusterJobQuery extends SequenceEntityQuery<ClusterJob> {

	public ClusterJobQuery() {
		super(ClusterJob.class);
	}

	public ClusterJobQuery workerId(String workerId) {
		return (ClusterJobQuery) addEquals("workerId", workerId);
	}

	public ClusterJobQuery status(JobStatus status) {
		return (ClusterJobQuery) addEquals("status", status);
	}

	public ClusterJobQuery error() {
		return (ClusterJobQuery) addIsNotNull("error");
	}

	public ClusterJobQuery notError() {
		return (ClusterJobQuery) addIsNull("error");
	}

}
