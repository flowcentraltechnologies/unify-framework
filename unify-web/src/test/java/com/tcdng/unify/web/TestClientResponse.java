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
package com.tcdng.unify.web;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.IOUtils;

/**
 * Test controller response.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class TestClientResponse implements ClientResponse {

    private Map<String, String> metas;

    private String contentType;

    private ByteArrayOutputStream outputStream;

    private PrintWriter writer;

    private boolean used;

    public TestClientResponse() {
        metas = new HashMap<String, String>();
        outputStream = new ByteArrayOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream));
    }

    @Override
    public void setMetaData(String key, String value) {
        metas.put(key, value);
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public void setCharacterEncoding(String charset) {

    }

    @Override
    public OutputStream getOutputStream() throws UnifyException {
        used = true;
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() throws UnifyException {
        used = true;
        return writer;
    }

    @Override
    public boolean isOutUsed() {
        return used;
    }

    @Override
    public void setStatus(int status) {

    }

    @Override
    public void setStatusOk() {
        
    }

    @Override
    public void setStatusForbidden() {
        
    }

    @Override
    public void setStatusNotFound() {
        
    }

    @Override
    public void setCookie(String name, String val) {

    }

    @Override
    public void setCookie(String name, String val, int maxAge) {

    }

    @Override
    public void setCookie(String domain, String path, String name, String val, int maxAge) {

    }

    @Override
    public void close() {
        IOUtils.close(writer);
        IOUtils.close(outputStream);
    }

    public Map<String, String> getMetas() {
        return metas;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getBytes() {
        return outputStream.toByteArray();
    }

    public String toString() {
        return new String(getBytes());
    }
}
