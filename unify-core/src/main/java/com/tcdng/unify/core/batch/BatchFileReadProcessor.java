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

import java.io.File;
import java.io.InputStream;
import java.io.Reader;

import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Batch file read processor.
 * 
 * @author The Code Department
 * @since 4.1
 */
public interface BatchFileReadProcessor extends UnifyComponent {

    /**
     * Performs batch file processing using supplied configuration.
     * 
     * @param batchFileReadConfig
     *            the batch file read configuration
     * @param file
     *            the array of file objects that constitutes a batch. Supported file
     *            objects are {@link InputStream}, {@link Reader}, {@link File} and
     *            {@link byte[]}
     * @return a processing result object
     * @throws UnifyException
     *             if an error occurs
     */
    Object process(BatchFileReadConfig batchFileReadConfig, Object... file) throws UnifyException;
}
