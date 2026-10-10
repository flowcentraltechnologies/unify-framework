/*
 * Copyright (c) 2018-2026 The Code Department.
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

import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.UploadedFilePreview;

/**
 * Uploaded file preview provider.
 * 
 * @author The Code Department
 * @since 4.1
 */
public interface UploadedFilePreviewProvider extends UnifyComponent {

	/**
	 * Provides uploaded file preview.
	 * 
	 * @param id the fileResourceId resource ID
	 * @return the uploaded file preview otherwise null
	 * @throws UnifyException if an error occurs
	 */
	UploadedFilePreview providePreview(String fileResourceId) throws UnifyException;
}
