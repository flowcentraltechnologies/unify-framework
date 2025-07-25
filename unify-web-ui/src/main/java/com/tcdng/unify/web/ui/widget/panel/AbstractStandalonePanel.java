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
package com.tcdng.unify.web.ui.widget.panel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.upl.UplElementReferences;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.PageWidgetValidator;
import com.tcdng.unify.web.ui.widget.AbstractPanel;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.PageAction;
import com.tcdng.unify.web.ui.widget.PageManager;
import com.tcdng.unify.web.ui.widget.PageValidation;
import com.tcdng.unify.web.ui.widget.StandalonePanelInfo;
import com.tcdng.unify.web.ui.widget.Widget;

/**
 * Convenient abstract implementation of standalone panel.
 * 
 * @author The Code Department
 * @since 4.1
 */
@UplAttributes({ @UplAttribute(name = "validationList", type = PageValidation[].class) })
public abstract class AbstractStandalonePanel extends AbstractPanel implements StandalonePanel {

	private StandalonePanelInfo standalonePanelInfo;

	private Map<String, PageWidgetValidator> pageWidgetValidators;

	private boolean validationEnabled;

	public AbstractStandalonePanel() {
		validationEnabled = true;
	}

	@Override
	@Action
	public void switchState() throws UnifyException {
		super.switchState();
	}

	@Override
	public void setStandalonePanelInfo(StandalonePanelInfo standalonePanelInfo) {
		this.standalonePanelInfo = standalonePanelInfo;
	}

	@Override
	public boolean isSourceInvalidated() throws UnifyException {
		return standalonePanelInfo.isSourceInvalidated();
	}

	@Override
	public PageWidgetValidator getPageWidgetValidator(PageManager pageManager, String longName) throws UnifyException {
		if (pageWidgetValidators == null) {
			pageWidgetValidators = new HashMap<String, PageWidgetValidator>();
		}

		PageWidgetValidator pageWidgetValidator = pageWidgetValidators.get(longName);
		if (pageWidgetValidator == null) {
			PageValidation pageValidation = getPageValidation(longName);
			List<Widget> widgets = getWidgetsByLongNames(
					pageManager.getLongNames(pageManager.getExpandedReferences(pageValidation.getId())));
			pageWidgetValidator = new PageWidgetValidator(pageValidation, widgets);
			pageWidgetValidators.put(longName, pageWidgetValidator);
		}

		return pageWidgetValidator;
	}

	@Override
	public PageValidation getPageValidation(String longName) {
		return standalonePanelInfo.getPageValidations().get(longName);
	}

	@Override
	public Set<String> getPageValidationNames() {
		return standalonePanelInfo.getPageValidations().keySet();
	}

	@Override
	public PageAction getPageAction(String longName) {
		return standalonePanelInfo.getPageActions().get(longName);
	}

	@Override
	public void resolvePageActions(Widget widget) throws UnifyException {
		if (standalonePanelInfo != null) {
			EventHandler[] eventHandlers = widget.getEventHandlers();
			if (eventHandlers != null) {
				for (EventHandler eh : eventHandlers) {
					if (eh.getPageAction() == null) {
						UplElementReferences uer = eh.getUplAttribute(UplElementReferences.class, "action");
						if (uer != null && !DataUtils.isBlank(uer.getIds())) {
							List<PageAction> pageActionList = null;
							for (Map.Entry<String, PageAction> entry : standalonePanelInfo.getPageActions()
									.entrySet()) {
								String actionLongName = entry.getKey();
								for (String id : uer.getIds()) {
									int index = actionLongName.lastIndexOf(id);
									if (index > 0 && actionLongName.charAt(index - 1) == '.') {
										if (pageActionList == null) {
											pageActionList = new ArrayList<PageAction>();
										}

										pageActionList.add(entry.getValue());
										break;
									}
								}
							}

							if (pageActionList != null) {
								eh.setPageAction(DataUtils.toArray(PageAction.class, pageActionList));
							}
						}
					}
				}
			}
		}
	}

	@Override
	public List<Widget> getWidgetsByLongNames(List<String> longNames) throws UnifyException {
		List<Widget> widgets = new ArrayList<Widget>(longNames.size());
		for (String longName : longNames) {
			Widget widget = getWidgetByLongName(longName);
			if (widget != null) {
				widgets.add(widget);
			}
		}
		return widgets;
	}

	@Override
	public boolean isValidationEnabled() throws UnifyException {
		return validationEnabled;
	}

	@Override
	public void setValidationEnabled(boolean validationEnabled) {
		this.validationEnabled = validationEnabled;
	}

}
