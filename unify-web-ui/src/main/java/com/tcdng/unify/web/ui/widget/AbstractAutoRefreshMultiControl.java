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
package com.tcdng.unify.web.ui.widget;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.constant.ResultMappingConstants;
import com.tcdng.unify.web.constant.UnifyWebRequestAttributeConstants;
import com.tcdng.unify.web.ui.widget.data.AutoRefresh;


/**
 * Base class for auto-referesh multi-controls.
 * 
 * @author The Code Department
 * @since 4.1
 */
public abstract class AbstractAutoRefreshMultiControl extends AbstractMultiControl {

	private EventHandler[] handlers;

	private String[] refs;

	@Override
	public final boolean isRefreshesContainer() throws UnifyException {
		return !isRequestAttribute(UnifyWebRequestAttributeConstants.AUTO_REFRESH);
	}

	@Action
	public final void autoRefresh() throws UnifyException {
		if (!isWithCommandResultMapping()) {
			setRequestAttribute(UnifyWebRequestAttributeConstants.AUTO_REFRESH, new AutoRefresh(this));
			setCommandResultMapping(ResultMappingConstants.AUTO_REFRESH);
		}
	}

	public void saveForRefresh(EventHandler[] handlers, String[] refs) throws UnifyException {
		this.handlers = handlers;
		this.refs = refs;
	}

	public void clearForRefresh() {
		this.handlers = null;
		this.refs = null;
	}

	public EventHandler[] getHandlers() {
		return handlers;
	}

	public String[] getRefs() {
		return refs;
	}

	public boolean isSavePresent() {
		return handlers != null || refs != null;
	}
}
