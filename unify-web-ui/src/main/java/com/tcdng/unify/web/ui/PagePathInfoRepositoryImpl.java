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

import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.web.ControllerPathParts;
import com.tcdng.unify.web.PathInfoRepository;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * Default page path information repository implementation.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component(WebUIApplicationComponents.APPLICATION_PAGEPATHINFOREPOSITORY)
public class PagePathInfoRepositoryImpl extends AbstractUnifyComponent implements PagePathInfoRepository {

	@Configurable
	private PathInfoRepository pathInfoRepository;

	private FactoryMap<String, PagePathInfo> pagePathInfos;

	public PagePathInfoRepositoryImpl() {
		pagePathInfos = new FactoryMap<String, PagePathInfo>() {

			@Override
			protected PagePathInfo create(String controllerPathId, Object... params) throws Exception {
				return new PagePathInfo(controllerPathId, null, controllerPathId + "/openPage",
						controllerPathId + "/reloadPage", controllerPathId + "/savePage",
						controllerPathId + "/closePage", false);
			}
		};
	}

	@Override
	public PagePathInfo getPagePathInfo(Page page) throws UnifyException {
		return pagePathInfos.get(page.getPathId());
	}

	@Override
	public PagePathInfo getPagePathInfo(String path) throws UnifyException {
		return pagePathInfos.get(path);
	}

	@Override
	public ControllerPathParts getControllerPathParts(Page page) throws UnifyException {
		return pathInfoRepository.getControllerPathParts(page.getPathId());
	}

	@Override
	public ControllerPathParts getControllerPathParts(String controllerPath) throws UnifyException {
		return pathInfoRepository.getControllerPathParts(controllerPath);
	}

	@Override
	protected void onInitialize() throws UnifyException {

	}

	@Override
	protected void onTerminate() throws UnifyException {

	}
}
