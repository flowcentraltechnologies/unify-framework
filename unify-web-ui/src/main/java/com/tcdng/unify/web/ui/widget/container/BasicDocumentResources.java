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

package com.tcdng.unify.web.ui.widget.container;

import java.util.List;

import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Basic document resources.
 * 
 * @author The Code Department
 * @since 4.1
 */
public interface BasicDocumentResources extends UnifyComponent {

    /**
     * Gets a list of style sheet resource links.
     * 
     * @return list of style sheet links
     * @throws UnifyException
     *             if an error occurs
     */
    List<String> getStyleSheetResourceLinks() throws UnifyException;

    /**
     * Gets a list of script resource links.
     * 
     * @return list of script links
     * @throws UnifyException
     *             if an error occurs
     */
    List<String> getScriptResourceLinks() throws UnifyException;
}
