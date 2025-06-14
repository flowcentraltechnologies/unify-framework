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
package com.tcdng.unify.core.business;

import java.util.List;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Transactional;

/**
 * Loan disbursement service implementation.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Transactional
@Component("loandisbursementservice")
public class LoanDisbursementServiceImpl extends AbstractBusinessService implements LoanDisbursementService {

    @Override
    public Long create(LoanDisbursement loanDisbursement) throws UnifyException {
        return (Long) db().create(loanDisbursement);
    }

    @Override
    public List<LoanDisbursement> find(LoanDisbursementQuery query) throws UnifyException {
        return db().listAll(query);
    }

}
