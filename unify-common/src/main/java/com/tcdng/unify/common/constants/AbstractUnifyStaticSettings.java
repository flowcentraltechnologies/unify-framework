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

package com.tcdng.unify.common.constants;

import java.util.Collections;
import java.util.List;

import com.tcdng.unify.common.data.UnifyContainerProperty;

/**
 * Base class for abstract static settings.
 * 
 * @author The Code Department
 * @since 4.1
 */
public abstract class AbstractUnifyStaticSettings implements UnifyStaticSettings {

	private String messageBase;

	private int level;

	private List<UnifyContainerProperty> containerProperties;

	public AbstractUnifyStaticSettings(String messageBase, int level,
			List<UnifyContainerProperty> containerProperties) {
		this.messageBase = messageBase;
		this.level = level;
		this.containerProperties = containerProperties;
	}

	public AbstractUnifyStaticSettings(String messageBase, int level) {
		this.messageBase = messageBase;
		this.level = level;
		this.containerProperties = Collections.emptyList();
	}

	public AbstractUnifyStaticSettings(String messageBase) {
		this.messageBase = messageBase;
		this.level = 0;
		this.containerProperties = Collections.emptyList();
	}

	@Override
	public String getMessageBase() {
		return messageBase;
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public List<UnifyContainerProperty> getContainerProperties() {
		return containerProperties;
	}

}
