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
import com.tcdng.unify.core.annotation.Policy;

/**
 * User session entity.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Policy("usersessiontracking-entitypolicy")
@Table("UNUSERSESSION")
public class UserSessionTracking extends AbstractSystemEntity {

    @Id(length = 64)
    private String sessionId;

    @Column(length = 64, nullable = true)
    private String remoteHost;

    @Column(length = 64, nullable = true)
    private String remoteAddress;

    @Column(length = 64, nullable = true)
    private String remoteUser;

    @Column(length = 64, nullable = true)
    private String userLoginId;

    @Column(length = 64, nullable = true)
    private String userName;

    @Column(length = 48)
    private String node;

    @Column(type = ColumnType.TIMESTAMP)
    private Date createTime;

    @Column(type = ColumnType.TIMESTAMP)
    private Date lastAccessTime;

    @Column(nullable = true)
    private Long tenantId;

    @Column(nullable = true, length = 128)
    private String tenantName;
    
    @Override
    public Object getId() {
        return sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public String getRemoteUser() {
        return remoteUser;
    }

    public void setRemoteUser(String remoteUser) {
        this.remoteUser = remoteUser;
    }

    public String getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(String userLoginId) {
        this.userLoginId = userLoginId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
}
