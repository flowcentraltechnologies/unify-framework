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
package com.tcdng.unify.core.data;

import java.util.Date;

/**
 * Data object that represents an uploaded file.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class UploadedFile {

    private String filename;

    private Date creationDate;

    private Date modificationDate;

    byte[] data;

    public UploadedFile(String filename, Date creationDate, Date modificationDate, byte[] data) {
        this.filename = filename;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
        this.data = data;
    }

    public String getFilename() {
        return filename;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public byte[] getData() {
        return data;
    }

    public int getSize() {
        if (data != null) {
            return data.length;
        }

        return 0;
    }

}
