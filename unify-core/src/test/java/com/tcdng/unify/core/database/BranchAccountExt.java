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

import java.math.BigDecimal;

import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.TableExt;

/**
 * Test branch account extension record.
 * 
 * @author The Code Department
 * @since 4.1
 */
@TableExt
public class BranchAccountExt extends BranchAccount {

    @Column
    private Double tax;

    public BranchAccountExt(Long branchId, String accountName, BigDecimal balance, Double tax) {
        super(branchId, accountName, balance);
        this.tax = tax;
    }

    public BranchAccountExt() {
        
    }
    
    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

}
