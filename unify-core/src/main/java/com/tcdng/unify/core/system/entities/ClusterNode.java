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
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.Id;

/**
 * Cluster node entity.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Table("UNCLUSTERNODE")
public class ClusterNode extends AbstractSystemEntity implements Listable {

    @Id(length = 40)
    private String nodeId;

    @Column(type = ColumnType.TIMESTAMP)
    private Date lastHeartBeat;

    @Column(length = 64)
    private String ipAddress;

    @Column(nullable = true)
    private Integer commandPort;

    @Override
    public Object getId() {
        return nodeId;
    }

    @Override
    public String getListKey() {
        return nodeId;
    }

    @Override
    public String getListDescription() {
        return nodeId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public Date getLastHeartBeat() {
        return lastHeartBeat;
    }

    public void setLastHeartBeat(Date lastHeartBeat) {
        this.lastHeartBeat = lastHeartBeat;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getCommandPort() {
        return commandPort;
    }

    public void setCommandPort(Integer commandPort) {
        this.commandPort = commandPort;
    }
}
