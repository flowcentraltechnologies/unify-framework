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
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.web.UnifyWebPropertyConstants;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.BundledCatType;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.http.BundledCategoryManager;

/**
 * Convenient abstract base class for bundled home page controllers.
 * 
 * @author The Code Department
 * @since 4.1
 */
@ResultMappings({
		@ResultMapping(name = "forwardtopath",
				response = { "!forwardresponse pathBinding:$s{targetPath}" }) })
public abstract class AbstractBundledHomePageController<T extends AbstractBundledHomePageBean>
		extends AbstractPageController<T> {

	@Configurable
	private BundledCategoryManager bundledCategoryManager;

	public AbstractBundledHomePageController(Class<T> pageBeanClass) {
		super(pageBeanClass, Secured.FALSE, ReadOnly.TRUE, ResetOnWrite.FALSE);
	}

	@Override
	public final BundledCatType getBundledCategory() throws UnifyException {
		return BundledCatType.ALL;
	}

	@Action
	public final String forwardToFrontOffice() throws UnifyException {
		return forwardToCategoryPath(BundledCatType.FRONTOFFICE);
	}

	@Action
	public final String forwardToBackOffice() throws UnifyException { 
		return forwardToCategoryPath(BundledCatType.BACKOFFICE);
	}

	private String forwardToCategoryPath(BundledCatType category) throws UnifyException {
		AbstractBundledHomePageBean pageBean = getPageBean();
		pageBean.setTargetPath(getContainerSetting(String.class,
				BundledCatType.FRONTOFFICE.equals(category)
						? UnifyWebPropertyConstants.APPLICATION_BUNDLED_MODE_FRONTOFFICE
						: UnifyWebPropertyConstants.APPLICATION_BUNDLED_MODE_BACKOFFICE));
		bundledCategoryManager.setSessionCategory(category);
		return "forwardtopath";
	}

}
