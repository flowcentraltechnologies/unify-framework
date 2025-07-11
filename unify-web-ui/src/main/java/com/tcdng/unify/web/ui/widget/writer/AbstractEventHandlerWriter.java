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
package com.tcdng.unify.web.ui.widget.writer;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.util.WriterUtils;
import com.tcdng.unify.web.ui.widget.AbstractBehaviorWriter;
import com.tcdng.unify.web.ui.widget.Behavior;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.PageAction;
import com.tcdng.unify.web.ui.widget.ResponseWriter;

/**
 * Abstract base class for event handler writers.
 * 
 * @author The Code Department
 * @since 4.1
 */
public abstract class AbstractEventHandlerWriter extends AbstractBehaviorWriter implements EventHandlerWriter {

	@Override
	public void writeBehavior(ResponseWriter writer, Behavior behavior, String id, String cmdTag, String preferredEvent)
			throws UnifyException {
		EventHandler eventHandler = (EventHandler) behavior;
		final String event = !StringUtils.isBlank(preferredEvent) ? preferredEvent : eventHandler.getEvent();
		if (!"none".equals(event)) {
			if (eventHandler.getPageAction() != null) {
				if (writer.isKeepPostCommandRefs()) {
					for (PageAction pageAction : eventHandler.getPageAction()) {
						keepPostCommandRefs(writer, id, pageAction);
					}
				} else {
					final String translatedEvent = WriterUtils.getEventJS(event.toLowerCase());
					for (PageAction pageAction : eventHandler.getPageAction()) {
						writer.beginFunction("ux.setOnEvent");
						String function = WriterUtils.getActionJSFunction(pageAction.getAction().toLowerCase());
						writeActionParamsJS(writer, translatedEvent, function, id, cmdTag, pageAction, null, null,
								null);
						writer.endFunction();
					}
				}
			}
		}
	}
}
