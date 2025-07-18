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

package com.tcdng.unify.web.data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.tcdng.unify.core.data.WebStringWriter;

/**
 * Web string writer tests.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class WebStringWriterTest {

    @Test
    public void testAppendJsonQuotedWithString() throws Exception {
        WebStringWriter lsw = new WebStringWriter(64);
        lsw.appendJsonQuoted("Hello world!");
        lsw.close();
        assertEquals("\"Hello world!\"", lsw.toString());
    }

    @Test
    public void testAppendJsonQuotedWithWebStringWriter() throws Exception {
        WebStringWriter lsw1 = new WebStringWriter(64);
        WebStringWriter lsw2 = new WebStringWriter(64);
        lsw2.append("Hello world!");
        lsw1.appendJsonQuoted(lsw2);
        lsw1.close();
        assertEquals("\"Hello world!\"", lsw1.toString());
    }
}
