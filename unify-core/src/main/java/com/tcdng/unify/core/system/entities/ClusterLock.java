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
package com.tcdng.unify.core.system.entities;

import java.util.Date;

import com.tcdng.unify.common.annotation.ColumnType;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.Id;

/**
 * Entity for storing cluster synchronization lock information.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Table("UNCLUSTERLOCK")
public class ClusterLock extends AbstractSystemEntity {

    @Id(length = 128)
    private String lockName;

    @Column(length = 128, nullable = true)
    private String currentOwner;

    @Column(length = 64, nullable = true)
    private String threadId;

    @Column(type = ColumnType.TIMESTAMP)
    private Date expiryTime;

    @Column
    private Integer lockCount;

    @Override
    public Object getId() {
        return this.lockName;
    }

    public String getLockName() {
        return lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
    }

    public String getCurrentOwner() {
        return currentOwner;
    }

    public void setCurrentOwner(String currentOwner) {
        this.currentOwner = currentOwner;
    }

    public String getThreadId() {
		return threadId;
	}

	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}

	public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public Integer getLockCount() {
        return lockCount;
    }

    public void setLockCount(Integer lockCount) {
        this.lockCount = lockCount;
    }
}
