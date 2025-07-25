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
package com.tcdng.unify.core.file;

import java.io.File;
import java.util.Set;

import com.tcdng.unify.core.util.DataUtils;

/**
 * File filter.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class FileFilter implements java.io.FileFilter {

    private Set<String> prefixes;

    private Set<String> suffixes;

    private boolean fileOnly;

    private boolean caseSensitive;

    public FileFilter(FileTransferSetup fileTransferSetup) {
        this(fileTransferSetup.getFilePrefixes(), fileTransferSetup.getFileSuffixes());
        this.caseSensitive = fileTransferSetup.isCaseSensitive();
        this.fileOnly = fileTransferSetup.isFileOnly();
    }

    public FileFilter(Set<String> prefixes, Set<String> suffixes) {
        this(prefixes, suffixes, false);
    }

    public FileFilter(boolean fileOnly) {
        this(null, null, fileOnly);
    }

    public FileFilter(Set<String> prefixes, Set<String> suffixes, boolean fileOnly) {
        this.prefixes = DataUtils.unmodifiableSet(prefixes);
        this.suffixes = DataUtils.unmodifiableSet(suffixes);
        this.fileOnly = fileOnly;
    }

    public Set<String> getPrefixes() {
        return prefixes;
    }

    public Set<String> getSuffixes() {
        return suffixes;
    }

    public boolean isFileOnly() {
        return fileOnly;
    }

    @Override
    public boolean accept(File file) {
        return accept(file.getName(), file.isFile());
    }

    public boolean accept(String filename, boolean isFile) {
        if (!isFile) {
            return !fileOnly;
        }
        if (!caseSensitive) {
            filename = filename.toLowerCase();
        }

        boolean accept = true;
        if (!prefixes.isEmpty()) {
            accept = false;
            for (String prefix : prefixes) {
                if (!caseSensitive) {
                    prefix = prefix.toLowerCase();
                }
                if (filename.startsWith(prefix)) {
                    accept = true;
                    break;
                }
            }
        }

        if (accept && !suffixes.isEmpty()) {
            accept = false;
            for (String suffix : suffixes) {
                if (!caseSensitive) {
                    suffix = suffix.toLowerCase();
                }
                if (filename.endsWith(suffix)) {
                    accept = true;
                    break;
                }
            }
        }
        return accept;
    }
}
