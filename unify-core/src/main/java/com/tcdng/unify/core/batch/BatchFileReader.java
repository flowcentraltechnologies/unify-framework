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
import com.tcdng.unify.core.data.ValueStore;

/**
 * Batch file reader. This component should be implemented as a non-singleton.
 * 
 * @author The Code Department
 * @since 4.1
 */
public interface BatchFileReader extends UnifyComponent {

    /**
     * Opens batch file reader for reading using supplied configuration.
     * 
     * @param batchFileReadConfig
     *            the batch file read configuration
     * @param file
     *            the array of file objects that constitutes a batch. Supported file
     *            objects are {@link InputStream}, {@link Reader}, {@link File} and
     *            {@link byte[]}
     * @throws UnifyException
     *             if an error occurs
     */
    void open(BatchFileReadConfig batchFileReadConfig, Object... file) throws UnifyException;

    /**
     * Detects if there is a preferred bean for reader instance. Utilized after
     * {@link #open(BatchFileReadConfig, Object...)} when information about batch
     * file would have been known and reader can suggest a preferred bean for
     * creating the {@link #readNextRecord(ValueStore)} value store.
     * 
     * @return a true value if there is a preferred bean, otherwise false
     * @throws UnifyException
     *             if an error occurs
     */
    boolean detectPreferredBean() throws UnifyException;

    /**
     * Gets preferred bean for reader instance.
     * 
     * @return a preferred bean if any, otherwise null
     * @throws UnifyException
     *             if an error occurs
     */
    Class<?> getPreferredBean() throws UnifyException;

    /**
     * Reads next record from batch.
     * 
     * @param recordStore
     *            the record object value store to populate
     * @return true if record was read otherwise false indicating end of records
     * @throws UnifyException
     *             if an error occurs
     */
    boolean readNextRecord(ValueStore recordStore) throws UnifyException;

    /**
     * Skips next record in batch.
     * 
     * @return true if record was skipped otherwise false indicating end of records
     * @throws UnifyException
     *             if an error occurs
     */
    boolean skipNextRecord() throws UnifyException;

    /**
     * Closes batch file reader and all used open resources.
     */
    void close();
}
