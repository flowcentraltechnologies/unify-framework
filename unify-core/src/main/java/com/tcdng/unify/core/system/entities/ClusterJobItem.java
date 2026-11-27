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

import com.tcdng.unify.common.annotation.Index;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.constant.JobItemStatus;

/**
 * Entity for storing job items for workers.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Table(name = "UNCLUSTERJOBITEM",
		indexes = {
				@Index({"clusterJobId", "payloadId", "status"})})
public class ClusterJobItem extends AbstractSequenceEntity {

	public static final int MAX_ERROR_LEN = 512;

	@ForeignKey(ClusterJob.class)
	private Long clusterJobId;

	@ForeignKey(name = "REC_ST")
	private JobItemStatus status;

	@Column
	private Long payloadId;

	@Column(name = "PRC_ERROR", length = MAX_ERROR_LEN, nullable = true)
	private String error;

	@Override
	public String getDescription() {
		return null;
	}

	public Long getClusterJobId() {
		return clusterJobId;
	}

	public void setClusterJobId(Long clusterJobId) {
		this.clusterJobId = clusterJobId;
	}

	public Long getPayloadId() {
		return payloadId;
	}

	public void setPayloadId(Long payloadId) {
		this.payloadId = payloadId;
	}

	public JobItemStatus getStatus() {
		return status;
	}

	public void setStatus(JobItemStatus status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
