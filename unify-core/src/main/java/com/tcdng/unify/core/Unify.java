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
package com.tcdng.unify.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.LogManager;

import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.core.util.TypeRepository;
import com.tcdng.unify.core.util.TypeUtils;
import com.tcdng.unify.core.util.UnifyConfigUtils;

/**
 * Unify application class.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class Unify {

    private static UnifyContainer uc;

    public static void main(String[] args) {
         if (args.length == 0) {
            log("Operation argument is required");
            System.exit(1);
        }

        String operation = args[0];
        String workingFolder = null;
        String configFile = null;
        short port = 0;
        boolean restrictedJARMode = false;

        for (int i = 1; i <= (args.length - 2); i += 2) {
            if ("-w".equals(args[i])) {
                workingFolder = args[i + 1];
            } else if ("-p".equals(args[i])) {
                port = Short.valueOf(args[i + 1]);
            } else if ("-c".equals(args[i])) {
                configFile = args[i + 1];
            } else if ("-j".equals(args[i])) {
                restrictedJARMode = Boolean.parseBoolean(args[i + 1]);
            } else {
                Unify.doHelp();
            }
        }

        if ("startup".equalsIgnoreCase(operation)) {
            Unify.doStartup(workingFolder, configFile, null, port, false);
        } else if ("install".equalsIgnoreCase(operation)) {
            Unify.doStartup(workingFolder, configFile, null, port, true);
        } else if ("install-onejar-fat".equalsIgnoreCase(operation)) {
            if (restrictedJARMode) {
                IOUtils.enterRestrictedJARMode();
            }
            
            URL[] _baseUrls = Unify.getOneJarFatBaseUrls();
            Unify.doStartup(workingFolder, configFile, _baseUrls, port, true);
        } else if ("install-spring-fat".equalsIgnoreCase(operation)) {
            if (restrictedJARMode) {
                IOUtils.enterRestrictedJARMode();
            }
            
            URL[] _baseUrls = Unify.getSpringFatBaseUrls();
            Unify.doStartup(workingFolder, configFile, _baseUrls, port, true);
        } else if ("help".equalsIgnoreCase(operation)) {
            Unify.doHelp();
        } else {
            log("Unknown operation - [{0}]", operation);
            System.exit(1);
        }
    }

    public static synchronized void startup(String workingFolder, short preferredPort, URL... baseUrls)
            throws UnifyException {
        Unify.doStartup(workingFolder, null, baseUrls, preferredPort, true);
    }

    public static synchronized UnifyContainer startup(UnifyContainerEnvironment uce, UnifyContainerConfig ucc)
            throws UnifyException {
        if (uc != null) {
            throw new UnifyException(UnifyCoreErrorConstants.CONTAINER_IN_RUNTIME);
        }

        try {
            uc = new UnifyContainer();
            uc.startup(uce, ucc);
        } catch (UnifyException e) {
            uc = null;
            throw e;
        } catch (Exception e) {
            uc = null;
            throw new UnifyException(e, UnifyCoreErrorConstants.CONTAINER_STARTUP_ERROR);
        }

        return uc;
    }

    public static synchronized void shutdown(String accessKey) throws UnifyException {
        Unify.getContainer();
        if (!uc.getAccessKey().equals(accessKey)) {
            throw new UnifyException(UnifyCoreErrorConstants.INVALID_CONTAINER_RUNTIME_ACCESSKEY);
        }
        uc.shutdown();
        uc = null;
    }

	public static String getWorkingPath() {
		return uc != null ? uc.getWorkingPath() : null;
	}

    public static <T> T getSetting(Class<T> dataType, String name) throws UnifyException {
    	return DataUtils.convert(dataType, uc != null ? uc.getSetting(name) : null) ;
    }
    
    private static UnifyContainer getContainer() throws UnifyException {
        if (uc == null) {
            throw new UnifyException(UnifyCoreErrorConstants.NO_CONTAINER_IN_RUNTIME);
        }

        return uc;
    }

    private static void doStartup(String workingFolder, String configFile, URL[] baseUrls, short preferredPort,
            boolean deploymentMode) {
        LogManager.getLogManager().reset();

        if (workingFolder == null || workingFolder.isEmpty()) {
            workingFolder = System.getProperty("user.dir");
        }

        UnifyContainerEnvironment uce = null;
        UnifyContainerConfig.Builder uccb = UnifyContainerConfig.newBuilder();
        try {
            log("Scanning classpath type repository...");
            TypeRepository tr = TypeUtils.getTypeRepositoryFromClasspath(baseUrls);
            uce = new UnifyContainerEnvironment(tr, workingFolder);
            UnifyConfigUtils.readConfigFromTypeRepository(uccb, tr);
            uccb.deploymentMode(deploymentMode);
        } catch (Exception e) {
            log("Failed scanning classpath type repository.", e);
            System.exit(1);
        }

        InputStream xmlInputStream = null;
        if (configFile == null || configFile.isEmpty()) {
            configFile = UnifyCoreConstants.CONFIGURATION_FILE;
        }

        final String environment = System.getProperty("unify.environment");
        if (!StringUtils.isBlank(environment)) {
            log("Environment specification detected...");
            log("Resolving container configuration file for environment [{0}]...", environment);
            configFile = UnifyConfigUtils.resolveConfigFileToEnvironment(configFile, environment);
        }
        
        try {
            log("Reading container configuration file [{0}]...", configFile);
            xmlInputStream = IOUtils.openFileResourceInputStream(configFile, workingFolder);
        } catch (Exception e) {
            log("Unable to open configuration file - " + IOUtils.buildFilename(workingFolder, configFile), e);
            System.exit(1);
        }

        try {
            UnifyConfigUtils.readConfigFromXml(uccb, xmlInputStream, workingFolder);
        } catch (UnifyException e) {
            IOUtils.close(xmlInputStream);
            log("Failed reading configuration file - " + IOUtils.buildFilename(workingFolder, configFile), e);
            System.exit(1);
        } finally {
            IOUtils.close(xmlInputStream);
        }

        try {
            if (preferredPort > 0) {
                uccb.preferredPort(preferredPort);
            }

            UnifyContainerConfig ucc = uccb.build();
            Unify.startup(uce, ucc);
        } catch (UnifyException e) {
            log("Error initializing Unify container.", e);
            System.exit(1);
        }
    }

    private static void doHelp() {

    }
    
    private static URL[] getOneJarFatBaseUrls() {
        List<URL> baseUrls = new ArrayList<URL>();
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> urls = cl.getResources("META-INF/MANIFEST.MF");
            String base = null;
            List<String[]> partList = new ArrayList<String[]>();
            while (urls.hasMoreElements()) {
                String url = urls.nextElement().toString();
                if (url.startsWith("jar:file:")) {
                    String[] parts = url.split("\\!/");
                    if (parts.length == 2) {
                        if (base == null || base.length() < parts[0].length()) {
                            base = parts[0];
                        }
                    }

                    partList.add(parts);
                }
            }

            if (base != null) {
                for (String[] parts : partList) {
                    if (parts.length == 3) {
                        baseUrls.add(new URL(base + "!/" + parts[1]));
                    }
                }
            }
        } catch (IOException e) {
            log("Error resolving packaged JARs.", e);
       }

        return baseUrls.isEmpty() ? null : baseUrls.toArray(new URL[baseUrls.size()]);
    }
    
    private static URL[] getSpringFatBaseUrls() {
        List<URL> baseUrls = new ArrayList<URL>();
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> urls = cl.getResources("META-INF/MANIFEST.MF");
            String base = null;
            List<String[]> partList = new ArrayList<String[]>();
            while (urls.hasMoreElements()) {
                String url = urls.nextElement().toString();
                if (url.startsWith("jar:file:")) {
                    String[] parts = url.split("\\!/");
                    if (parts.length == 2) {
                        if (base == null || base.length() < parts[0].length()) {
                            base = parts[0];
                        }
                    }

                    partList.add(parts);
                }
            }

            if (base != null) {
                String classPath = base.substring("jar:".length());
                baseUrls.add(new URL(classPath));
                for (String[] parts : partList) {
                    if (parts.length == 3) {
                        baseUrls.add(new URL(base + "!/" + parts[1]));
                    }
                }
            }
        } catch (IOException e) {
            log("Error resolving packaged JARs.", e);
       }

        return baseUrls.isEmpty() ? null : baseUrls.toArray(new URL[baseUrls.size()]);
    }
    
    private static void log(String message, Object... params) {
    	System.out.println(MessageFormat.format(message, params));
    }
    
    private static void log(String message, Exception e) {
    	System.out.println(message);
    	e.printStackTrace();
    }
}
