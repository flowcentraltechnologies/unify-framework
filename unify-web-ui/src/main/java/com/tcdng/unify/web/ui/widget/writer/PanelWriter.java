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
import com.tcdng.unify.web.ui.widget.Panel;
import com.tcdng.unify.web.ui.widget.ResponseWriter;

/**
 * User interface panel writer.
 * 
 * @author The Code Department
 * @since 4.1
 */
public interface PanelWriter extends ContainerWriter {

    /**
     * Writes inner panel structure and content
     * 
     * @param writer
     *            - the response writer
     * @param panel
     *            the panel to write
     * @throws UnifyException
     *             - If an error occurs
     */
    void writeInnerStructureAndContent(ResponseWriter writer, Panel panel) throws UnifyException;
}
