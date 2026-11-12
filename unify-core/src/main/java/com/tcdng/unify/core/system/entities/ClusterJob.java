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

import java.util.Date;

import com.tcdng.unify.common.annotation.ColumnType;
import com.tcdng.unify.common.annotation.Index;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.constant.JobStatus;

/**
 * Entity for storing jobs for workers.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Table(name = "UNCLUSTERJOB", indexes = {@Index({"status"})})
public class ClusterJob extends AbstractSequenceEntity {

    @ForeignKey(name = "REC_ST")
    private JobStatus status;

    @Column(length = 128)
    private String title;

    @Column(length = 64)
    private String handler;

    @Column(length = 32, nullable = true)
    private String lockedBy;

    @Column(type = ColumnType.TIMESTAMP, nullable = true)
    private Date lockedAt;

    @Column(length = 2048, nullable = true)
    private String error;

    @Column
    private Long payloadId;

    @Column
    private int attempts;

    @Column
    private int maxAttempts;

    @Override
	public String getDescription() {
		return title;
	}

	public JobStatus getStatus() {
		return status;
	}

	public void setStatus(JobStatus status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getLockedBy() {
		return lockedBy;
	}

	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}

	public Date getLockedAt() {
		return lockedAt;
	}

	public void setLockedAt(Date lockedAt) {
		this.lockedAt = lockedAt;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Long getPayloadId() {
		return payloadId;
	}

	public void setPayloadId(Long payloadId) {
		this.payloadId = payloadId;
	}

	public int getAttempts() {
		return attempts;
	}

	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}

	public int getMaxAttempts() {
		return maxAttempts;
	}

	public void setMaxAttempts(int maxAttempts) {
		this.maxAttempts = maxAttempts;
	}

}
