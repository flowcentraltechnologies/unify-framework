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
package com.tcdng.unify.web.ui.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.WebApplicationComponents;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.util.RealPathUtils;

/**
 * Resource controller for fetching file resources from application real path.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component(WebApplicationComponents.APPLICATION_REALPATHRESOURCECONTROLLER)
public class RealPathResourceController extends FileResourceController {

	protected File file;

	private String subfolder;

	public RealPathResourceController() {
		this(null);
	}

	public RealPathResourceController(String subfolder) {
		super(Secured.FALSE);
		this.subfolder = subfolder;
	}

	@Override
	public void prepareExecution() throws UnifyException {
		String resourceName = getResourceName();
		logDebug("Using subfolder [{0}] to read resource [{1}]...", subfolder, resourceName);
		if (!StringUtils.isBlank(subfolder)) {
			resourceName = IOUtils.buildFilename(subfolder, resourceName);
		} else {
			// Apply restrictions only on direct real path access
			RealPathUtils.checkAccessibleRealPath(resourceName);
		}

		super.prepareExecution();
		
		file = new File(resourceName);
		file = file.exists() ? file
				: new File(IOUtils.buildFilename(getUnifyComponentContext().getWorkingPath(), resourceName));
		logDebug("Reading resolved file [{0}]...", file.getAbsolutePath());
		if (file.exists()) {
			setContentLength(file.length());
		}
	}

	@Override
	protected ResInputStream getInputStream() throws UnifyException {
		if (file != null && file.exists()) {
			try {
				return new ResInputStream(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				throwOperationErrorException(e);
			}
		}
		return null;
	}

	protected void setSubfolder(String subfolder) {
		this.subfolder = subfolder;
	}
}
