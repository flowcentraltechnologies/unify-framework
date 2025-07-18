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
package com.tcdng.unify.core.database;

import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Column;

/**
 * Test office entity.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Table(name = "OFFICE")
public class Office extends AbstractTestVersionedTableEntity {

    @Column
    private String address;

    @Column
    private String telephone;

    @Column
    private Integer size;

    @Column(nullable = true)
    private String[] workDays;

    public Office(String address, String telephone, Integer size) {
        this.address = address;
        this.telephone = telephone;
        this.size = size;
    }

    public Office() {

    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String[] getWorkDays() {
        return workDays;
    }

    public void setWorkDays(String[] workDays) {
        this.workDays = workDays;
    }
}
