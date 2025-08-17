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
package com.tcdng.unify.core.util;

import java.io.CharArrayReader;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UnifyOperationException;
import com.tcdng.unify.core.data.XmlConfig;

/**
 * Provides utility methods for XML configuration.
 * 
 * @author The Code Department
 * @since 4.1
 */
public final class XmlConfigUtils {

    private XmlConfigUtils() {

    }

    public static <T> T readXmlConfig(Class<T> xmlDataClazz, Object xmlSrcObject) throws UnifyException {
        try {
            XmlMapper unmarshaller = new XmlMapper();
            if (xmlSrcObject instanceof String) {
                Reader reader = new CharArrayReader(((String) xmlSrcObject).toCharArray());
                return unmarshaller.readValue(reader, xmlDataClazz);
            } else if (xmlSrcObject instanceof Reader) {
                return unmarshaller.readValue((Reader) xmlSrcObject, xmlDataClazz);
            } else if (xmlSrcObject instanceof InputStream) {
                return unmarshaller.readValue((InputStream) xmlSrcObject, xmlDataClazz);
            }

            return unmarshaller.readValue((File) xmlSrcObject, xmlDataClazz);
        } catch (Exception e) {
            throw new UnifyOperationException(e);
        }
    }

    public static void writeXmlConfig(XmlConfig configObject, OutputStream outputStream) throws UnifyException {
    	XmlConfigUtils.writeXmlConfig(configObject, outputStream, false);
    }

    public static void writeXmlConfigNoEscape(XmlConfig configObject, OutputStream outputStream) throws UnifyException {
    	XmlConfigUtils.writeXmlConfig(configObject, outputStream, true);
    }

    public static void writeXmlConfig(XmlConfig configObject, Writer writer) throws UnifyException {
    	XmlConfigUtils.writeXmlConfig(configObject, writer, false);
    }

    public static void writeXmlConfigNoEscape(XmlConfig configObject, Writer writer) throws UnifyException {
    	XmlConfigUtils.writeXmlConfig(configObject, writer, true);
    }

    public static String getXmlConfigAsString(XmlConfig configObject) throws UnifyException {
    	StringWriter writer = new StringWriter();
    	XmlConfigUtils.writeXmlConfig(configObject, writer, false);
    	return writer.toString();
    }

    public static String getXmlConfigAsStringNoEscape(XmlConfig configObject) throws UnifyException {
    	StringWriter writer = new StringWriter();
    	XmlConfigUtils.writeXmlConfig(configObject, writer, true);
    	return writer.toString();
    }

	private static void writeXmlConfig(XmlConfig configObject, OutputStream outputStream, boolean noEscape)
			throws UnifyException {
		try {
			if (configObject.includeXmlDeclaration()) {
				outputStream.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n".getBytes(StandardCharsets.UTF_8));
			}

			XmlMapper marshaller = new XmlMapper();
			marshaller.enable(SerializationFeature.INDENT_OUTPUT);
			marshaller.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
			marshaller.writeValue(outputStream, configObject);
			outputStream.flush();
		} catch (Exception e) {
			throw new UnifyOperationException(e);
		}
	}

    private static void writeXmlConfig(XmlConfig configObject, Writer writer, boolean noEscape) throws UnifyException {
        try {
			if (configObject.includeXmlDeclaration()) {
				writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			}

            XmlMapper marshaller = new XmlMapper();
			marshaller.enable(SerializationFeature.INDENT_OUTPUT);
			marshaller.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
			marshaller.writeValue(writer, configObject);
            writer.flush();
        } catch (Exception e) {
            throw new UnifyOperationException(e);
        }
    }

}
