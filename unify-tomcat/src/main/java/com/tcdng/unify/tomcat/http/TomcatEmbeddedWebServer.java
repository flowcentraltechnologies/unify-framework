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
package com.tcdng.unify.tomcat.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.MultipartConfigElement;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import com.tcdng.unify.core.UnifyCoreErrorConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.constant.NetworkSchemeType;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.tomcat.TomcatApplicationComponents;
import com.tcdng.unify.web.http.AbstractEmbeddedHttpWebServer;
import com.tcdng.unify.web.http.HttpApplicationServlet;

/**
 * Tomcat embedded web server.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component(TomcatApplicationComponents.TOMCAT_EMBEDDEDWEBSERVER)
public class TomcatEmbeddedWebServer extends AbstractEmbeddedHttpWebServer {

	private Tomcat tomcat;

	private NetworkSchemeType networkSchemeType;

	public TomcatEmbeddedWebServer() {
		networkSchemeType = NetworkSchemeType.HTTP;
	}

	@Override
	public String getScheme() {
		return networkSchemeType.code();
	}

	@Override
	public int getPort() throws UnifyException {
		return getHttpPort();
	}

	@Override
	protected void onInitialize() throws UnifyException {
		try {
			tomcat = new Tomcat();
			List<Integer> portList = new ArrayList<Integer>();
			String keyStorePath = getKeyStorePath();
			if (!StringUtils.isBlank(keyStorePath)) {
				final int httpsPort = getHttpsPort();
				logInfo("Configuring HTTPS on port [{0}]...", Integer.toString(httpsPort));
				Path _keyStorePath = Paths.get(keyStorePath).toAbsolutePath();
				if (!Files.exists(_keyStorePath)) {
					throwOperationErrorException(new FileNotFoundException(_keyStorePath.toString()));
				}
				final String keystoreFile = _keyStorePath.toString();
				Connector connector = new Connector();
				connector.setPort(httpsPort);
				connector.setSecure(true);
				connector.setScheme("https");
				connector.setAttribute("keyAlias", null);
				connector.setAttribute("keystorePass", getKeyStorePass());
				connector.setAttribute("keystoreType", "JKS");
				connector.setAttribute("keystoreFile", keystoreFile);
				connector.setAttribute("protocol", "HTTP/1.1");
				connector.setAttribute("sslProtocol", "TLS");
				connector.setAttribute("protocol", "org.apache.coyote.http11.Http11AprProtocol");
				connector.setAttribute("SSLEnabled", true);
				tomcat.setConnector(connector);
				portList.add(httpsPort);
			}

			if (isHttpsOnly()) {
				if (portList.isEmpty()) {
					throwOperationErrorException(new IllegalArgumentException(
							"You must provide SSL keystore properties since you have specified HTTPS only."));
				}
			} else {
				// Setup HTTP
				final int httpPort = getHttpPort();
				logInfo("Configuring HTTP on port [{0}]...", Integer.toString(httpPort));
				Connector connector = new Connector();
				connector.setPort(httpPort);
				tomcat.setConnector(connector);
				portList.add(httpPort);
			}

			logInfo("Initializing HTTP server on ports {0}; using context path {1} and servlet path {2}...", portList,
					getContextPath(), getServletPath());
			final String _docBase = new File(".").getAbsolutePath();
			final String _servletName = TomcatApplicationComponents.TOMCAT_EMBEDDEDWEBSERVER + "-servlet";
			final Context context = tomcat.addContext(getContextPath(), _docBase);
			final String sessionCookieName = generateSessionCookieName();
			context.setSessionCookieName(sessionCookieName);

			// WebSocket
			context.addApplicationListener(TomcatClientSyncWsContextListener.class.getName());
			// context.addApplicationListener(WsContextListener.class.getName());

			// HTTP/HTTPS
			Wrapper servletWrapper = tomcat.addServlet(getContextPath(), _servletName,
					new HttpApplicationServlet(createHttpServletModule()));
			servletWrapper.setMultipartConfigElement(new MultipartConfigElement(getMultipartLocation(),
					getMultipartMaxFileSize(), getMultipartMaxRequestSize(), getMultipartFileSizeThreshold()));
			context.addServletMappingDecoded(getServletPath(), _servletName);

			context.setSessionTimeout(getSessionSeconds() / 60);
			tomcat.start();
			logInfo("HTTP server initialization completed.");
		} catch (Exception e) {
			throw new UnifyException(e, UnifyCoreErrorConstants.COMPONENT_INITIALIZATION_ERROR, getName());
		}
	}

	@Override
	protected void onTerminate() throws UnifyException {
		try {
			tomcat.stop();
		} catch (Exception e) {
			throw new UnifyException(e, UnifyCoreErrorConstants.COMPONENT_TERMINATION_ERROR, getName());
		}
	}

	@Override
	protected void onStartServicingRequests() throws UnifyException {

	}

	@Override
	protected void onStopServicingRequests() throws UnifyException {

	}
}
