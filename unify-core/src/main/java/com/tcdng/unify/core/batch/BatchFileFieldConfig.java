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
package com.tcdng.unify.core.batch;

import com.tcdng.unify.core.constant.PadDirection;

/**
 * Batch file field configuration.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class BatchFileFieldConfig {

    private String beanFieldName;

    private String fileFieldName;

    private String formatter;

    private PadDirection padDirection;

    private Character padChar;

    private int length;

    private boolean trim;

    private boolean pad;

    private boolean updateOnConstraint;

    public BatchFileFieldConfig(String beanFieldName, String fileFieldName, String formatter, PadDirection padDirection,
            int length, boolean trim, boolean pad, boolean updateOnConstraint, Character padChar) {
        this.beanFieldName = beanFieldName;
        this.fileFieldName = fileFieldName;
        this.formatter = formatter;
        this.padDirection = padDirection;
        this.length = length;
        this.trim = trim;
        this.pad = pad;
        this.updateOnConstraint = updateOnConstraint;
        this.padChar = padChar;
    }

    public String getBeanFieldName() {
        return beanFieldName;
    }

    public String getFileFieldName() {
        return fileFieldName;
    }

    public String getFormatter() {
        return formatter;
    }

    protected PadDirection getPadDirection() {
        return padDirection;
    }

    public int getLength() {
        return length;
    }

    public boolean isTrim() {
        return trim;
    }

    public boolean isPad() {
        return pad;
    }

    public boolean isUpdateOnConstraint() {
        return updateOnConstraint;
    }

    public Character getPadChar() {
        return padChar;
    }

    public boolean isFormatter() {
        return this.formatter != null;
    }
}
