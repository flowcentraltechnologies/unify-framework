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
package com.tcdng.unify.web.ui.response;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.AbstractJsonPageControllerResponse;
import com.tcdng.unify.web.ui.widget.Page;
import com.tcdng.unify.web.ui.widget.ResponseWriter;

/**
 * Used for generating an open window response.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component("openwindowresponse")
@UplAttributes({ @UplAttribute(name = "path", type = String.class),
        @UplAttribute(name = "pathBinding", type = String.class) })
public class OpenWindowResponse extends AbstractJsonPageControllerResponse {

    public OpenWindowResponse() {
        super("openWindowHdl", false);
    }

    @Override
    public void doGenerate(ResponseWriter writer, Page page) throws UnifyException {
        String path = getUplAttribute(String.class, "path");
        if (StringUtils.isBlank(path)) {
            String pathBinding = getUplAttribute(String.class, "pathBinding");
            path = (String) ReflectUtils.getNestedBeanProperty(page.getPageBean(), pathBinding);
        }

        logDebug("Preparing open window response: path ID = [{0}],  target path = [{1}]", page.getPathId(), path);
        writer.write(",");
        writer.writeJsonPathVariable("openWindow", path);
    }
}
