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
package com.tcdng.unify.web.ui.widget.writer.control;

import java.util.Date;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.resource.ImageGenerator;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.core.util.TokenUtils;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.TargetControl;
import com.tcdng.unify.web.ui.widget.control.Image;
import com.tcdng.unify.web.ui.widget.writer.AbstractTargetControlWriter;

/**
 * Image control writer.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Writes(Image.class)
@Component("image-writer")
public class ImageWriter extends AbstractTargetControlWriter {

    @Override
    protected void doWriteTargetControl(ResponseWriter writer, TargetControl targetControl) throws UnifyException {
        Image imageCtrl = (Image) targetControl;
        writer.write("<img");
        writeTagAttributes(writer, imageCtrl);
        writer.write(" src=\"");
        Object image = imageCtrl.getValue();
        if (image instanceof ImageGenerator) {
            if (!((ImageGenerator) image).isReady()) {
                image = null;
            }
        }

        if (image != null) {
            String imageName = "Img_" + imageCtrl.getId() + '_' + (new Date().getTime());
            setSessionAttribute(imageName, image);
            writer.writeScopeImageContextURL(imageName, true);
       } else {
            String src = imageCtrl.getSrc();
            if (StringUtils.isNotBlank(src)) {
                boolean alwaysFetch = imageCtrl.isAlwaysFetch();
                if (TokenUtils.isContextScopeTag(src)) {
                    String imageName = TokenUtils.extractTokenValue(src);
                    writer.writeScopeImageContextURL(imageName, imageCtrl.isClearOnRead());
                    String scope = imageCtrl.getScope();
                    if (StringUtils.isNotBlank(scope)) {
                        writer.writeURLParameter("scope", scope);
                    }

                    if (alwaysFetch) {
                        writer.writeURLParameter("morsic", String.valueOf(System.currentTimeMillis()));
                    }
                } else { 
                    writer.writeFileImageContextURL(src);
                    if (alwaysFetch) {
                        writer.writeURLParameter("morsic", String.valueOf(System.currentTimeMillis()));
                    }
                }
            }
        }
        writer.write("\">");
    }

}
