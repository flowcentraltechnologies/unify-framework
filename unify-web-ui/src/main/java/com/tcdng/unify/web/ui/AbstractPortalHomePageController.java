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
package com.tcdng.unify.web.ui;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.EncodingUtils;
import com.tcdng.unify.web.UnifyWebPropertyConstants;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.PortalCategoryConstants;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.http.HttpRequestCookieConstants;

/**
 * Convenient abstract base class for portal home page controllers.
 * 
 * @author The Code Department
 * @since 4.1
 */
@ResultMappings({
		@ResultMapping(name = "forwardtopath",
				response = { "!forwardresponse pathBinding:$s{targetPath}" }) })
public abstract class AbstractPortalHomePageController<T extends AbstractPortalHomePageBean>
		extends AbstractPageController<T> {

	public AbstractPortalHomePageController(Class<T> pageBeanClass) {
		super(pageBeanClass, Secured.FALSE, ReadOnly.TRUE, ResetOnWrite.FALSE);
	}

	@Override
	public final String getPortalCategory() throws UnifyException {
		return PortalCategoryConstants.GLOBAL_CATEGORY;
	}

	@Action
	public final String forwardToFrontOffice() throws UnifyException {
		return forwardToCategoryPath(PortalCategoryConstants.FRONTOFFICE_CATEGORY);
	}

	@Action
	public final String forwardToBackOffice() throws UnifyException {
		return forwardToCategoryPath(PortalCategoryConstants.BACKOFFICE_CATEGORY);
	}
	
	private String forwardToCategoryPath(String category) throws UnifyException {
		AbstractPortalHomePageBean pageBean = getPageBean();
		pageBean.setTargetPath(getContainerSetting(String.class,
				PortalCategoryConstants.FRONTOFFICE_CATEGORY.equals(category)
						? UnifyWebPropertyConstants.APPLICATION_PORTAL_MODE_FRONTOFFICE
						: UnifyWebPropertyConstants.APPLICATION_PORTAL_MODE_BACKOFFICE));
		getPageRequestContextUtil().getClientResponse().setCookie(HttpRequestCookieConstants.UNIFY_PORTAL_CATEGORY,
				EncodingUtils.getBase64String(category));
		return "forwardtopath";
	}

}
