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
package com.tcdng.unify.web.ui.widget.control;

import java.util.Map;

import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.ColorScheme;
import com.tcdng.unify.core.constant.LocaleType;
import com.tcdng.unify.web.ui.widget.data.BadgeInfo;

/**
 * Convenient abstract base class for enumeration constants badge information provider.
 * 
 * @author The Code Department
 * @since 4.1
 */
public abstract class AbstractEnumConstBadgeInfoProvider extends AbstractBadgeInfoProvider {

	private static BadgeInfo BADGEINFO;

	private final String list;

	public AbstractEnumConstBadgeInfoProvider(String list) {
		this.list = list;
	}

	public BadgeInfo provide() throws UnifyException {
		if (BADGEINFO == null) {
			synchronized (AbstractEnumConstBadgeInfoProvider.class) {
				if (BADGEINFO == null) {
					BadgeInfo.Builder bib = BadgeInfo.newBuilder();
					Map<String, Listable> map = getListMap(LocaleType.APPLICATION, list);
					for (Map.Entry<EnumConst, ColorScheme> entry : getColors().entrySet()) {
						addBadgeInfo(bib, map, entry.getKey(), entry.getValue());
					}

					BADGEINFO = bib.build();
				}
			}
		}

		return BADGEINFO;
	}

	protected abstract Map<EnumConst, ColorScheme> getColors() throws UnifyException;

	private void addBadgeInfo(BadgeInfo.Builder bib, Map<String, Listable> map, EnumConst cons, ColorScheme scheme) {
		bib.addItem(cons.code(), map.get(cons.code()).getListDescription(), scheme);
	}

}
