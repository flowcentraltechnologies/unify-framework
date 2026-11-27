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
import java.util.List;

import com.tcdng.unify.common.annotation.ColumnType;
import com.tcdng.unify.common.annotation.Index;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.constant.JobStatus;

/**
 * Entity for storing jobs for workers.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Table(name = "UNCLUSTERJOB",
	indexes = {
			@Index({"handler"}),
			@Index({"status"})})
public class ClusterJob extends AbstractSequenceEntity {

    @ForeignKey(name = "REC_ST")
    private JobStatus status;

    @Column(length = 64)
    private String handler;

    @Column(length = 32, nullable = true)
    private String claimedBy;

    @Column(type = ColumnType.TIMESTAMP, nullable = true)
    private Date claimedAt;

    @Column(type = ColumnType.TIMESTAMP, nullable = true)
    private Date completedOn;

    @ChildList
    private List<ClusterJobItem> itemList;
    
	@Override
	public String getDescription() {
		return handler;
	}

	public JobStatus getStatus() {
		return status;
	}

	public void setStatus(JobStatus status) {
		this.status = status;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getClaimedBy() {
		return claimedBy;
	}

	public void setClaimedBy(String claimedBy) {
		this.claimedBy = claimedBy;
	}

	public Date getClaimedAt() {
		return claimedAt;
	}

	public void setClaimedAt(Date claimedAt) {
		this.claimedAt = claimedAt;
	}

	public Date getCompletedOn() {
		return completedOn;
	}

	public void setCompletedOn(Date completedOn) {
		this.completedOn = completedOn;
	}

	public List<ClusterJobItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<ClusterJobItem> itemList) {
		this.itemList = itemList;
	}

}
