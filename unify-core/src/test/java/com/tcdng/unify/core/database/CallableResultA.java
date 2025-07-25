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

import java.util.Date;

import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.CallableResult;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ResultField;

/**
 * Test callable result class.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Table(name = "LICENSE")
@CallableResult
public class CallableResultA extends AbstractTestVersionedTableEntity {

    @Column
    @ResultField
    private String licenseNo;

    @Column
    @ResultField
    private Date expiryDt;

    public CallableResultA(String licenseNo, Date expiryDt) {
        this.licenseNo = licenseNo;
        this.expiryDt = expiryDt;
    }

    public CallableResultA() {

    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public Date getExpiryDt() {
        return expiryDt;
    }

    public void setExpiryDt(Date expiryDt) {
        this.expiryDt = expiryDt;
    }
}
