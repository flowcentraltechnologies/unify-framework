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
package com.tcdng.unify.core.security;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.PostResp;

/**
 * Convenient abstract base class for two-factor authentication REST service.
 * 
 * @author The Code Department
 * @since 4.1
 */
public abstract class AbstractTwoFactorAutenticationRestService extends AbstractTwoFactorAutenticationService {

	@Override
	public void sendOneTimePasscode(String userName, String userEmail) throws UnifyException {
		IOUtils.postObjectToEndpointUsingJson(TwoFactorAuthResponse.class, getSendEndpoint(),
				new TwoFactorAuthRequest(userName, userEmail, null));
	}

	@Override
	public boolean authenticate(String userName, String userEmail, String password) throws UnifyException {
		PostResp<TwoFactorAuthResponse> resp = IOUtils.postObjectToEndpointUsingJson(TwoFactorAuthResponse.class,
				getAuthEndpoint(), new TwoFactorAuthRequest(userName, userEmail, password));
		return resp.isSuccess() && resp.getResult().isSuccess();
	}

	protected abstract String getSendEndpoint() throws UnifyException;

	protected abstract String getAuthEndpoint() throws UnifyException;
}
