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

package com.tcdng.unify.core.resource;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Singleton;

/**
 * Convenient abstract base class for picture handlers.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Singleton(false)
public abstract class AbstractPictureHandler extends AbstractImageGenerator implements PictureHandler {

	private Long sourceId;
	
	@Override
	public void setSourceId(Long sourceId) throws UnifyException {
		this.sourceId = sourceId;
	}

	@Override
	public boolean isReady() throws UnifyException {
		return true;
	}

	protected Long getSourceId() {
		return sourceId;
	}

}
