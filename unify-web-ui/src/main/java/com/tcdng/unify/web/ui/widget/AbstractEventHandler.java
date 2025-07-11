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
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.upl.UplElementReferences;

/**
 * Serves as a base class for an event handler.
 * 
 * @author The Code Department
 * @since 4.1
 */
@UplAttributes({
	@UplAttribute(name = "event", type = String.class, mandatory = true),
	@UplAttribute(name = "eventBinding", type = String.class),
    @UplAttribute(name = "action", type = UplElementReferences.class) })
public abstract class AbstractEventHandler extends AbstractBehavior implements EventHandler {

    private PageAction[] pageAction;

    @Override
	public String getEvent() throws UnifyException {
		return getUplAttribute(String.class, "event");
	}

	@Override
	public String getEventBinding() throws UnifyException {
		return getUplAttribute(String.class, "eventBinding");
	}

	@Override
    public void setPageAction(PageAction[] pageAction) {
        this.pageAction = pageAction;
    }

	@Override
    public PageAction[] getPageAction() {
        return pageAction;
    }
}
