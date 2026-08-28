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
package com.tcdng.unify.core.business;

import java.util.Date;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.database.AbstractEntityPolicy;

/**
 * Policy class for bookings.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component("booking-policy")
public class BookingPolicy extends AbstractEntityPolicy<Booking> {

    @Configurable
    private AnotherMockService anotherMockService;

	@Override
	public Object preCreate(Booking record, Date now) throws UnifyException {
		anotherMockService
				.findLoanDisbursements((LoanDisbursementQuery) new LoanDisbursementQuery().ignoreEmptyCriteria(true));
		return record.getId();
	}

    @Override
    public void preUpdate(Booking record, Date now) throws UnifyException {

    }

    @Override
    public void preDelete(Booking record, Date now) throws UnifyException {

    }

    @Override
    public void onCreateError(Booking record) {

    }

    @Override
    public void onUpdateError(Booking record) {

    }

    @Override
    public void onDeleteError(Booking record) {

    }
}
