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

import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import com.tcdng.unify.common.constants.UnifyStaticSettings;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Singleton;
import com.tcdng.unify.core.constant.LocaleType;
import com.tcdng.unify.core.constant.PrintFormat;
import com.tcdng.unify.core.data.ParamConfig;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.format.Formatter;
import com.tcdng.unify.core.logging.Logger;
import com.tcdng.unify.core.logging.LoggingLevel;
import com.tcdng.unify.core.message.ResourceBundles;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.upl.UplComponent;
import com.tcdng.unify.core.util.AnnotationUtils;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.core.util.ThreadUtils;
import com.tcdng.unify.core.util.TokenUtils;
import com.tcdng.unify.core.util.ValueStoreUtils;

/**
 * Abstract unify component implementation that provides convenience methods for
 * interacting with the component's context, getting component type information,
 * getting references to other components (alternative to configurable
 * references) and handling initialization and shutdown requirements for a unify
 * component.
 * <p>
 * All subclasses are automatically singletons, unless overridden with the
 * {@link Singleton} annotation.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Singleton(true)
public abstract class AbstractUnifyComponent implements UnifyComponent {

	private UnifyComponentContext unifyComponentContext;

	@Override
	public void initialize(UnifyComponentContext unifyComponentContext) throws UnifyException {
		if (this.unifyComponentContext != null) {
			throw new UnifyException(UnifyCoreErrorConstants.COMPONENT_ALREADY_INITIALIZED, getName());
		}

		this.unifyComponentContext = unifyComponentContext;
		onInitialize();
	}

	@Override
	public void terminate() throws UnifyException {
		onTerminate();
	}

	@Override
	public final String getName() {
		if (unifyComponentContext != null) {
			return unifyComponentContext.getName();
		}

		return null;
	}

	@Override
	public final String getNodeId() {
		if (unifyComponentContext != null) {
			return unifyComponentContext.getNodeId();
		}

		return null;
	}

	@Override
	public final boolean isInitialized() {
		return unifyComponentContext != null;
	}
	
	/**
	 * Gets the component context.
	 *
	 * @return the component context
	 * @throws UnifyException if an error occurs
	 */
	@Override
	public UnifyComponentContext getUnifyComponentContext() throws UnifyException {
		return unifyComponentContext;
	}

	/**
	 * Current thread sleep in milliseconds.
	 * 
	 * @param milliSeconds the time in milliseconds
	 */
	protected void sleep(long milliSeconds) {
		ThreadUtils.sleep(milliSeconds);
	}

	/**
	 * Sends a message to the application container.
	 * 
	 * @param command the command to send
	 * @param params  the command parameters
	 * @throws UnifyException if an error occurs
	 */
	protected void sendCommand(String command, String... params) throws UnifyException {
		unifyComponentContext.sendCommand(command, params);
	}

	/**
	 * Gets the container application code.
	 * 
	 * @return the application code
	 * @throws UnifyException if an error occurs
	 */
	protected String getApplicationCode() throws UnifyException {
		return unifyComponentContext.getInstanceCode();
	}

	/**
	 * Gets the container application name.
	 * 
	 * @return the application name
	 * @throws UnifyException if an error occurs
	 */
	protected String getApplicationName() throws UnifyException {
		return unifyComponentContext.getInstanceName();
	}

	/**
	 * Gets the container deployment version.
	 * 
	 * @return the deployment version
	 * @throws UnifyException if an error occurs
	 */
	protected String getDeploymentVersion() throws UnifyException {
		return unifyComponentContext.getDeploymentVersion();
	}

	/**
	 * Gets the container application auxiliary version.
	 * 
	 * @return the auxiliary version
	 * @throws UnifyException if an error occurs
	 */
	protected String getAuxiliaryVersion() throws UnifyException {
		return unifyComponentContext.getAuxiliaryVersion();
	}

	/**
	 * Checks if lock is locked.
	 * 
	 * @param lockName the lock name
	 * @return true if locked otherwise false
	 * @throws Exception if an error occurs
	 */
	protected boolean isLocked(String lockName) throws UnifyException {
		return unifyComponentContext.isLocked(lockName);
	}

	/**
	 * Grabs lock if available.
	 * 
	 * @param lockName the lock name
	 * @return true if lock is grabbed otherwise false
	 * @throws UnifyException if an error occurs
	 */
	protected boolean tryGrabLock(String lockName) throws UnifyException {
		return unifyComponentContext.tryGrabLock(lockName);
	}

	/**
	 * Grabs lock with no timeout.
	 * 
	 * @param lockName the lock name
	 * @return true if lock is grabbed otherwise false
	 * @throws UnifyException if an error occurs
	 */
	protected boolean grabLock(String lockName) throws UnifyException {
		return unifyComponentContext.grabLock(lockName);
	}

	/**
	 * Grabs lock. Waits for lock to be available.
	 * 
	 * @param lockName the lock name
	 * @param timeout  the timeout (no timeout if negetive or zero)
	 * @return true if lock is grabbed otherwise false
	 * @throws UnifyException if an error occurs
	 */
	protected boolean grabLock(String lockName, long timeout) throws UnifyException {
		return unifyComponentContext.grabLock(lockName, timeout);
	}

	/**
	 * Releases lock.
	 * 
	 * @param lockName the lock name
	 * @throws UnifyException if an error occurs
	 */
	protected void releaseLock(String lockName) throws UnifyException {
		unifyComponentContext.releaseLock(lockName);
	}

	/**
	 * Returns the component context container information object.
	 * 
	 * @return the container information object
	 * @throws UnifyException if an error occurs
	 */
	public UnifyContainerInfo getContainerInfo() throws UnifyException {
		return unifyComponentContext.getContainerInfo();
	}

	/**
	 * Returns a container setting.
	 * 
	 * @param clazz the setting value type
	 * @param name  the setting name
	 * @return the setting value
	 * @throws UnifyException if an error occurs
	 */
	protected <T> T getContainerSetting(Class<T> clazz, String name) throws UnifyException {
		return getContainerSetting(clazz, name, null);
	}

	/**
	 * Returns a container setting.
	 * 
	 * @param clazz        the setting value type
	 * @param name         the setting name
	 * @param defaultValue an optional default value
	 * @return the resolved setting value
	 * @throws UnifyException if an error occurs
	 */
	protected <T> T getContainerSetting(Class<T> clazz, String name, T defaultValue) throws UnifyException {
		Object value = unifyComponentContext.getContainerSetting(name);
		if (value == null) {
			value = defaultValue;
		}
		return DataUtils.convert(clazz, value);
	}

	/**
	 * Gets filename based on container working path.
	 * 
	 * @param relativeFilename the relative file name
	 * @return the working path file name
	 * @throws UnifyException if an error occurs
	 */
	protected String getWorkingPathFilename(String relativeFilename) throws UnifyException {
		return unifyComponentContext.getWorkingPathFilename(relativeFilename);
	}

	/**
	 * Gets container working path.
	 * 
	 * @return the working path
	 * @throws UnifyException if an error occurs
	 */
	protected String getWorkingPath() throws UnifyException {
		return unifyComponentContext.getWorkingPath();
	}

	/**
	 * Returns the container static settings.
	 * 
	 * @return list of static settings
	 */
	protected List<UnifyStaticSettings> getStaticSettings() {
		return unifyComponentContext.getStaticSettings();
	}

	/**
	 * Gets the configuration for a component by name. The method gets the
	 * configuration from the component context using the supplied name. If found,
	 * checks if the supplied component type is assignable from the configuration
	 * component type.
	 * 
	 * @param componentType the component type
	 * @param name          the component name
	 * @return the component configuration if found, otherwise null.
	 * @throws UnifyException if component configuration with name is unknown. If
	 *                        supplied component type is not assignable from
	 *                        configuration component type.
	 */
	protected UnifyComponentConfig getComponentConfig(Class<? extends UnifyComponent> componentType, String name)
			throws UnifyException {
		UnifyComponentConfig unifyComponentConfig = unifyComponentContext.getComponentConfig(name);
		if (unifyComponentConfig != null && !componentType.isAssignableFrom(unifyComponentConfig.getType())) {
			throw new UnifyException(UnifyCoreErrorConstants.COMPONENT_CONFIG_DIFF_TYPE, name, componentType,
					unifyComponentConfig.getType());
		}
		return unifyComponentConfig;
	}

	/**
	 * Gets the configuration for a component by name.
	 * 
	 * @param name the component name
	 * @return the component configuration if found, otherwise null.
	 * @throws UnifyException if an error occurs
	 */
	protected UnifyComponentConfig getComponentConfig(String name) throws UnifyException {
		return unifyComponentContext.getComponentConfig(name);
	}

	/**
	 * Gets a component type by name. Looks for a component configuration in context
	 * by name and returns the configured component type.
	 * 
	 * @param name the component name
	 * @return the component type
	 * @throws UnifyException if component configuration with name is not found.
	 */
	protected Class<? extends UnifyComponent> getComponentType(String name) throws UnifyException {
		UnifyComponentConfig unifyComponentConfig = unifyComponentContext.getComponentConfig(name);
		if (unifyComponentConfig == null) {
			throw new UnifyException(UnifyCoreErrorConstants.COMPONENT_UNKNOWN_COMP, name);
		}
		return unifyComponentConfig.getType();
	}

	/**
	 * Finds a component type by name. Looks for a component configuration in
	 * context by name and returns the configured component type.
	 * 
	 * @param name the component name
	 * @return the component type if found otherwise null
	 * @throws UnifyException if an error occurs
	 */
	protected Class<? extends UnifyComponent> findComponentType(String name) throws UnifyException {
		UnifyComponentConfig unifyComponentConfig = unifyComponentContext.getComponentConfig(name);
		if (unifyComponentConfig != null) {
			return unifyComponentConfig.getType();
		}

		return null;
	}

	/**
	 * Gets a component type by name. Looks for a component configuration in context
	 * by name and returns the configured component type.
	 * 
	 * @param clazz the expected type class
	 * @param name  the component name
	 * @return the component type
	 * @throws UnifyException if an error occurs
	 */
	@SuppressWarnings("unchecked")
	protected <T extends UnifyComponent> Class<T> getComponentType(Class<T> clazz, String name) throws UnifyException {
		UnifyComponentConfig unifyComponentConfig = unifyComponentContext.getComponentConfig(name);
		if (unifyComponentConfig == null) {
			throw new UnifyException(UnifyCoreErrorConstants.COMPONENT_UNKNOWN_COMP, name);
		}

		if (!clazz.isAssignableFrom(unifyComponentConfig.getType())) {
			throw new UnifyException(UnifyCoreErrorConstants.CONTEXT_COMPONENT_TYPE_MISMATCH,
					unifyComponentConfig.getType(), clazz);
		}

		return (Class<T>) unifyComponentConfig.getType();
	}

	/**
	 * Gets a component by type.
	 * 
	 * @param componentType the component type
	 * @return the component
	 * @throws UnifyException if container is not started. If component of type
	 *                        unknown. If multiple implementations of type is found.
	 *                        If component instantiation error occurs.
	 */
	protected <T extends UnifyComponent> T getComponent(Class<T> componentType) throws UnifyException {
		return unifyComponentContext.getComponent(componentType);
	}

	/**
	 * Gets a reference to an instance of a component by name.
	 * 
	 * @param name the component name
	 * @return the same reference is always returned for singleton components. A new
	 *         reference is returned for non-singleton components.
	 * @throws UnifyException if component with name is unknown. If an instantiation
	 *                        error occurs.
	 */
	protected UnifyComponent getComponent(String name) throws UnifyException {
		return unifyComponentContext.getComponent(name);
	}

	/**
	 * Gets a reference to an instance of a component by name.
	 * 
	 * @param type the component type
	 * @param name the component name
	 * @return the same reference is always returned for singleton components. A new
	 *         reference is returned for non-singleton components.
	 * @throws UnifyException if component with name is unknown. If an instantiation
	 *                        error occurs.
	 */
	@SuppressWarnings("unchecked")
	protected <T extends UnifyComponent> T getComponent(Class<T> type, String name) throws UnifyException {
		return (T) unifyComponentContext.getComponent(name);
	}

	/**
	 * Gets a component by name using alternate settings. Applies to non-singletons
	 * only..
	 * 
	 * @param name        the component name
	 * @param altSettings the alternate settings
	 * @return the component
	 * @throws UnifyException if container is not started. If component with name is
	 *                        unknown. If component is a singleton. If component
	 *                        instantiation error occurs.
	 */
	protected UnifyComponent getComponent(String name, UnifyComponentSettings altSettings) throws UnifyException {
		return unifyComponentContext.getComponent(name, altSettings);
	}

	/**
	 * Gets a component by name using alternate settings. Applies to non-singletons
	 * only..
	 * 
	 * @param name        the component name
	 * @param altSettings the alternate settings
	 * @return the component
	 * @throws UnifyException if container is not started. If component with name is
	 *                        unknown. If component is a singleton. If component
	 *                        instantiation error occurs.
	 */
	protected UnifyComponent getComponent(String name, Setting... altSettings) throws UnifyException {
		return unifyComponentContext.getComponent(name, new UnifyComponentSettings(altSettings));
	}

	/**
	 * Checks if component with name exists in this component's context.
	 * 
	 * @param name the component name
	 * @return the true value if component with name exists otherwise false
	 * @throws UnifyException If component an error occurs.
	 */
	protected boolean isComponent(String name) throws UnifyException {
		return unifyComponentContext.isComponent(name);
	}

	/**
	 * Checks if component with name is defined in container.
	 * 
	 * @param componentType the component type
	 * @return a true value if component type exists otherwise false
	 * @throws UnifyException If component an error occurs.
	 */
	public boolean isComponent(Class<? extends UnifyComponent> componentType) throws UnifyException {
		return unifyComponentContext.isComponent(componentType);
	}

	/**
	 * Gets a UPL component with specified descriptor.
	 * 
	 * @param locale     the locale
	 * @param descriptor the UPL descriptor
	 * @param cached     the cached flag.
	 * @return If the cached flag is set, the container returns a cached instance
	 *         with the same descriptor and locale if found. Otherwise, a new
	 *         instance is returned.
	 * @throws UnifyException if an error occurs
	 */
	protected UplComponent getUplComponent(Locale locale, String descriptor, boolean cached) throws UnifyException {
		return unifyComponentContext.getUplComponent(locale, descriptor, cached);
	}

	/**
	 * Gets a UPL component using supplied attributes key..
	 * 
	 * @param locale        the locale
	 * @param attributesKey the element attributes ID
	 * @return the same reference is always returned for singleton components. A new
	 *         reference is returned for non-singleton components.
	 * @throws UnifyException if an error occurs
	 */
	protected UplComponent getUplComponent(Locale locale, String attributesKey) throws UnifyException {
		return unifyComponentContext.getUplComponent(locale, attributesKey);
	}

	/**
	 * Fetches a list of names of components of a specified type in the component
	 * context.
	 * 
	 * @param componentType the component type
	 * @return a list of component names.
	 * @throws UnifyException if an error occurs
	 */
	protected List<String> getComponentNames(Class<? extends UnifyComponent> componentType) throws UnifyException {
		return unifyComponentContext.getComponentNames(componentType);
	}

	/**
	 * Fetches a list of configurations of components of a specified type in the
	 * component context.
	 * 
	 * @param componentType the component type
	 * @return a list of component configurations.
	 * @throws UnifyException if an error occurs
	 */
	protected List<UnifyComponentConfig> getComponentConfigs(Class<? extends UnifyComponent> componentType)
			throws UnifyException {
		return unifyComponentContext.getComponentConfigs(componentType);
	}

	/**
	 * Fetches all component instances of a specific type.
	 * 
	 * @param componentType the component type
	 * @return the list of components.
	 * @throws UnifyException if an error occurs
	 */
	protected <T extends UnifyComponent> List<T> getComponents(Class<T> componentType) throws UnifyException {
		return unifyComponentContext.getComponents(componentType);
	}

	/**
	 * Creates a value store using supplied storage object.
	 * 
	 * @param storageObject the storage object to use
	 * @return ValueStore new instance of a value store
	 * @throws UnifyException if an error occurs
	 */
	protected ValueStore createValueStore(Object storageObject) throws UnifyException {
		return ValueStoreUtils.getValueStore(storageObject);
	}

	/**
	 * Creates a value store with index information using supplied storage object.
	 * 
	 * @param storageObject the storage object to use
	 * @param dataIndex     the data index
	 * @return ValueStore new instance of a value store
	 * @throws UnifyException if an error occurs
	 */
	protected ValueStore createValueStore(Object storageObject, int dataIndex) throws UnifyException {
		return ValueStoreUtils.getValueStore(storageObject, null, dataIndex);
	}

	/**
	 * Creates a value store with index information using supplied storage object.
	 * 
	 * @param storageObject the storage object to use
	 * @param dataMarker    the data marker
	 * @param dataIndex     the data index
	 * @return ValueStore new instance of a value store
	 * @throws UnifyException if an error occurs
	 */
	protected ValueStore createValueStore(Object storageObject, String dataMarker, int dataIndex)
			throws UnifyException {
		return ValueStoreUtils.getValueStore(storageObject, dataMarker, dataIndex);
	}

	/**
	 * Creates an array value store with index information using supplied storage
	 * object.
	 * 
	 * @param storageObject the storage object to use
	 * @param dataIndex     the data index
	 * @return ValueStore new instance of a value store
	 * @throws UnifyException if an error occurs
	 */
	protected ValueStore createArrayValueStore(Object[] storageObject, int dataIndex) throws UnifyException {
		return ValueStoreUtils.getArrayValueStore(storageObject, null, dataIndex);
	}

	/**
	 * Creates a list value store with index information using supplied storage
	 * object.
	 * 
	 * @param storageObject the storage object to use
	 * @param dataIndex     the data index
	 * @return ValueStore new instance of a value store
	 * @throws UnifyException if an error occurs
	 */
	protected ValueStore createListValueStore(List<Object> storageObject, int dataIndex) throws UnifyException {
		return ValueStoreUtils.getListValueStore(Object.class, storageObject, null, dataIndex);
	}

	/**
	 * Fetches a list of annotated classes of a specific type and annotation type.
	 * 
	 * @param classType       the annotated class type
	 * @param annotationClass the annotation class
	 * @param basePackages    packages to restrict search to. This parameter is
	 *                        optional.
	 * @return a list of annotated classes
	 * @throws UnifyException if an error occurs
	 */
	protected <T> List<Class<? extends T>> getAnnotatedClasses(Class<T> classType,
			Class<? extends Annotation> annotationClass, String... basePackages) throws UnifyException {
		return unifyComponentContext.getAnnotatedClasses(classType, annotationClass, basePackages);
	}

	/**
	 * Fetches a list of annotated classes of a specific type and annotation type.
	 * 
	 * @param classType       the annotated class type
	 * @param annotationClass the annotation class
	 * @param excludePackages packages to exclude search from. This parameter is
	 *                        optional.
	 * @return a list of annotated classes
	 * @throws UnifyException if an error occurs
	 */
	protected <T> List<Class<? extends T>> getAnnotatedClassesExcluded(Class<T> classType,
			Class<? extends Annotation> annotationClass, String... excludePackages) throws UnifyException {
		return unifyComponentContext.getAnnotatedClassesExcluded(classType, annotationClass, excludePackages);
	}

	/**
	 * Checks if component context is in cluster mode.
	 * 
	 * @return a true if component context is in cluster mode otherwise false
	 */
	protected boolean isClusterMode() {
		return unifyComponentContext.isClusterMode();
	}

	/**
	 * Checks if component context is in production mode.
	 * 
	 * @return a true if component context is in production mode otherwise false
	 * @throws UnifyException if an error occurs
	 */
	protected boolean isProductionMode() throws UnifyException {
		return unifyComponentContext.isProductionMode();
	}

	/**
	 * Checks if component context is in deployment mode.
	 * 
	 * @return a true if component context is in deployment mode otherwise false
	 * @throws UnifyException if an error occurs
	 */
	protected boolean isDeploymentMode() throws UnifyException {
		return unifyComponentContext.isDeploymentMode();
	}

	/**
	 * Checks if container interfaces are opened.
	 * 
	 * @return a true if container interfaces are open otherwise false
	 * @throws UnifyException if an error occurs
	 */
	protected boolean isInterfacesOpen() {
		return unifyComponentContext.isInterfacesOpen();
	}

	/**
	 * Gets the component container runtime ID.
	 * 
	 * @return the runtime ID. A null value is returned if container is not in
	 *         cluster mode.
	 * @throws UnifyException if an error occurs
	 */
	protected String getRuntimeId() throws UnifyException {
		return unifyComponentContext.getRuntimeId();
	}

	/**
	 * Gets the component container preferred port.
	 * 
	 * @return the container preferred port.
	 * @throws UnifyException if an error occurs
	 */
	protected short getPreferredPort() throws UnifyException {
		return unifyComponentContext.getPreferredPort();
	}

	/**
	 * Gets the component context line separator.
	 * 
	 * @return the line separator string
	 * @throws UnifyException if an error occurs
	 */
	protected String getLineSeparator() throws UnifyException {
		return unifyComponentContext.getLineSeparator();
	}

	/**
	 * Converts value to a specific type using an optional formatter.
	 * 
	 * @param targetClazz  the target type to convert to
	 * @param value        the value to convert
	 * @param formatterUpl the formatter UPL descriptor if any
	 * @return the converted value
	 * @throws UnifyException if an error occurs
	 */
	protected <T> T convert(Class<T> targetClazz, Object value, String formatterUpl) throws UnifyException {
		if (formatterUpl == null) {
			return DataUtils.convert(targetClazz, value);
		} else {
			return DataUtils.convert(targetClazz, value, getSessionLocaleFormatter(formatterUpl));
		}
	}

	/**
	 * Converts value to a specific collection type using an optional formatter.
	 * 
	 * @param collectionClazz the target collection type to convert to
	 * @param dataClass       the collation data type
	 * @param value           the value to convert
	 * @param formatterUpl    the formatter UPL descriptor if any
	 * @return the converted value
	 * @throws UnifyException if an error occurs
	 */
	protected <T, U extends Collection<T>> U convert(Class<U> collectionClazz, Class<T> dataClass, Object value,
			String formatterUpl) throws UnifyException {
		if (formatterUpl == null) {
			return DataUtils.convert(collectionClazz, dataClass, value);
		} else {
			return DataUtils.convert(collectionClazz, dataClass, value, getSessionLocaleFormatter(formatterUpl));
		}
	}

	/**
	 * Gets a formatter component instance for supplied UPL descriptor and using
	 * application locale.
	 * 
	 * @param formatterUpl the formatter UPL descriptor
	 * @return the formatter component instance
	 * @throws UnifyException if descriptor refers to unknown formatter type. If an
	 *                        error occurs
	 */
	protected Formatter<?> getApplicationLocaleFormatter(String formatterUpl) throws UnifyException {
		return (Formatter<?>) getUplComponent(getApplicationLocale(), formatterUpl, true);
	}

	/**
	 * Gets a formatter component instance for supplied UPL descriptor and using
	 * session locale.
	 * 
	 * @param formatterUpl the formatter UPL descriptor
	 * @return the formatter component instance
	 * @throws UnifyException if descriptor refers to unknown formatter type. If an
	 *                        error occurs
	 */
	protected Formatter<?> getSessionLocaleFormatter(String formatterUpl) throws UnifyException {
		return (Formatter<?>) getUplComponent(getSessionLocale(), formatterUpl, true);
	}

	/**
	 * Gets the current session context.
	 * 
	 * @return the session context
	 * @throws UnifyException if an error occurs.
	 */
	protected SessionContext getSessionContext() throws UnifyException {
		return unifyComponentContext.getRequestContext() != null
				? unifyComponentContext.getRequestContext().getSessionContext()
				: null;
	}

	/**
	 * Gets the current request context.
	 * 
	 * @return the request context
	 * @throws UnifyException if an error occurs.
	 */
	protected RequestContext getRequestContext() throws UnifyException {
		return unifyComponentContext.getRequestContext();
	}

	/**
	 * Sets an attribute in application context.
	 * 
	 * @param name  the attribute name
	 * @param value the attribute value to set
	 * @throws UnifyException if an error occurs
	 */
	protected void setApplicationAttribute(String name, Object value) throws UnifyException {
		unifyComponentContext.setApplicationAttribute(name, value);
	}

	/**
	 * Gets an attribute value from application context.
	 * 
	 * @param name the attribute name
	 * @return the attribute value
	 * @throws UnifyException if an error occurs
	 */
	protected Object getApplicationAttribute(String name) throws UnifyException {
		return unifyComponentContext != null ? unifyComponentContext.getApplicationAttribute(name) : null;
	}

	/**
	 * Gets an attribute value from application context with a conversion to the
	 * appropriate type.
	 * 
	 * @param name the attribute name
	 * @return the attribute value
	 * @throws UnifyException if an error occurs
	 */
	protected <T> T getApplicationAttribute(Class<T> typeClass, String name) throws UnifyException {
		Object val = unifyComponentContext != null ? unifyComponentContext.getApplicationAttribute(name) : null;
		return DataUtils.convert(typeClass, val);
	}

	/**
	 * Removes an attribute value from application context.
	 * 
	 * @param name the attribute name
	 * @return the attribute value
	 * @throws UnifyException if an error occurs
	 */
	protected Object removeApplicationAttribute(String name) throws UnifyException {
		return unifyComponentContext.removeApplicationAttribute(name);
	}

	/**
	 * Checks if application context has an attribute.
	 * 
	 * @param name the attribute name
	 * @return a true value if attribute exists in application context otherwise
	 *         false
	 * @throws UnifyException if an error occurs
	 */
	protected boolean isApplicationAttribute(String name) throws UnifyException {
		return unifyComponentContext.isApplicationAttribute(name);
	}

	/**
	 * Broadcasts attribute to all sessions in this node.
	 * 
	 * @param name  the attribute name
	 * @param value the attribute value. A null value clears attribute.
	 * @throws UnifyException if an error occurs
	 */
	protected void broadcastToOtherSessions(String name, Object value) throws UnifyException {
		logDebug("Broadcasting to all sessions, attribute = [{0}], value = [{1}]", name, value);
		unifyComponentContext.broadcastToSessions(name, value);
	}

	/**
	 * Broadcasts attribute to specific application session context in this node.
	 * 
	 * @param sessionId the session ID
	 * @param name      the attribute name
	 * @param value     the attribute value. A null value clears attribute.
	 * @throws UnifyException if an error occurs
	 */
	protected void broadcastToSession(String sessionId, String name, Object value) throws UnifyException {
		logDebug("Broadcasting to session [{0}], attribute = [{1}], value = [{2}]", sessionId, name, value);
		unifyComponentContext.broadcastToSession(sessionId, name, value);
	}

	/**
	 * Sets an attribute in current session.
	 * 
	 * @param name  the attribute name
	 * @param value the attribute value to set
	 * @throws UnifyException if an error occurs
	 */
	protected void setSessionAttribute(String name, Object value) throws UnifyException {
		if (unifyComponentContext != null) {
			unifyComponentContext.getSessionContext().setAttribute(name, value);
		}
	}

	/**
	 * Sets a sticky attribute in current session.
	 * 
	 * @param name  the attribute name
	 * @param value the attribute value to set
	 * @throws UnifyException if an error occurs
	 */
	protected void setSessionStickyAttribute(String name, Object value) throws UnifyException {
		if (unifyComponentContext != null) {
			unifyComponentContext.getSessionContext().setStickyAttribute(name, value);
		}
	}

	/**
	 * Gets an attribute value from current session.
	 * 
	 * @param name the attribute name
	 * @return the attribute value
	 * @throws UnifyException if an error occurs
	 */
	protected Object getSessionAttribute(String name) throws UnifyException {
		return unifyComponentContext != null ? unifyComponentContext.getSessionContext().getAttribute(name) : null;
	}

	/**
	 * Gets converted attribute value from current session.
	 * 
	 * @param typeClass the type
	 * @param name      the attribute name
	 * @return the attribute value
	 * @throws UnifyException if an error occurs
	 */
	protected <T> T getSessionAttribute(Class<T> typeClass, String name) throws UnifyException {
		Object val = unifyComponentContext != null ? unifyComponentContext.getSessionContext().getAttribute(name)
				: null;
		return DataUtils.convert(typeClass, val);
	}

	/**
	 * Checks if session context has an attribute.
	 * 
	 * @param name the attribute name
	 * @return a true value if attribute exists in session context otherwise false
	 * @throws UnifyException if an error occurs
	 */
	protected boolean isSessionAttribute(String name) throws UnifyException {
		return unifyComponentContext != null && unifyComponentContext.getSessionContext().isAttribute(name);
	}

	/**
	 * Removes an attribute from current session.
	 * 
	 * @param name the name of the attribute to remove
	 * @return the value of attribute remove, otherwise null.
	 * @throws UnifyException if an error occurs
	 */
	protected Object removeSessionAttribute(String name) throws UnifyException {
		return unifyComponentContext != null ? unifyComponentContext.getSessionContext().removeAttribute(name) : null;
	}

	/**
	 * Removes an attribute from current session.
	 * 
	 * @param name the name of the attribute to remove
	 * @return the value of attribute remove, otherwise null.
	 * @throws UnifyException if an error occurs
	 */
	protected <T> T removeSessionAttribute(Class<T> typeClass, String name) throws UnifyException {
		Object val = unifyComponentContext != null ? unifyComponentContext.getSessionContext().removeAttribute(name)
				: null;
		return DataUtils.convert(typeClass, val);
	}

	/**
	 * Removes attributes from current session.
	 * 
	 * @param names the names of the attributes to remove
	 * @throws UnifyException if an error occurs
	 */
	protected void removeSessionAttributes(String... names) throws UnifyException {
		if (unifyComponentContext != null) {
			unifyComponentContext.getSessionContext().removeAttributes(names);
		}
	}

	/**
	 * Returns view directive of supplied visual privilege code for current session
	 * role.
	 * 
	 * @param privilege the privilege to test
	 * @return the view rule
	 * @throws UnifyException if an error occurs
	 */
	protected ViewDirective getViewDirective(String privilege) throws UnifyException {
		return unifyComponentContext.getRoleViewDirective(privilege);
	}

	/**
	 * Returns privilege codes for supplied category and role.
	 * 
	 * @param roleCode              the role code
	 * @param privilegeCategoryCode the privilege category code
	 * @return set of privilege codes
	 * @throws UnifyException if an error occurs
	 */
	protected Set<String> getRolePrivilegeCodes(String roleCode, String privilegeCategoryCode) throws UnifyException {
		return unifyComponentContext.getRolePrivilegeCodes(roleCode, privilegeCategoryCode);
	}

	/**
	 * Returns privilege codes for supplied category and current context role.
	 * 
	 * @param privilegeCategoryCode the privilege category code
	 * @return set of privilege codes
	 * @throws UnifyException if an error occurs
	 */
	protected Set<String> getCurrentRolePrivilegeCodes(String privilegeCategoryCode) throws UnifyException {
		return unifyComponentContext.getCurrentRolePrivilegeCodes(privilegeCategoryCode);
	}

	/**
	 * Checks if current session role has privilege.
	 * 
	 * @param privilegeCategoryCode the privilege category code
	 * @param privilegeCode         the privilege code
	 * @return a true if current session role has privilege otherwise false
	 * @throws UnifyException if an error occurs
	 */
	public boolean isCurrentRolePrivilege(String privilegeCategoryCode, String privilegeCode) throws UnifyException {
		return unifyComponentContext.isCurrentRolePrivilege(privilegeCategoryCode, privilegeCode);
	}

	/**
	 * Tests if context has role attributes loaded for the supplied role code.
	 * 
	 * @param roleCode the role code
	 * @return true if context has attributes for role
	 */
	protected boolean isRoleAttributes(String roleCode) {
		return unifyComponentContext.isRoleAttributes(roleCode);
	}

	/**
	 * Returns workflow step codes that are associated with role of current user.
	 * 
	 * @return a set of workflow step codes
	 * @throws UnifyException if an error occurs
	 */
	public Set<String> getCurrentUserRoleStepCodes() throws UnifyException {
		return unifyComponentContext.getCurrentUserRoleStepCodes();
	}

	/**
	 * Sets attributes for specified role.
	 * 
	 * @param roleCode       the role code
	 * @param roleAttributes the attributes to load.
	 */
	protected void setRoleAttributes(String roleCode, RoleAttributes roleAttributes) {
		unifyComponentContext.setRoleAttributes(roleCode, roleAttributes);
	}

	/**
	 * Checks if user in current session is logged in.
	 * 
	 * @return a true if current session user is logged in otherwise false
	 * @throws UnifyException if an error occurs
	 */
	protected boolean isUserLoggedIn() throws UnifyException {
		return unifyComponentContext.getSessionContext().isUserLoggedIn();
	}

	/**
	 * Checks if session is in global accounting mode
	 * 
	 * @return the global accounting mode flag
	 * @throws UnifyException if an error occurs
	 */
	protected boolean isGlobalAccounting() throws UnifyException {
		return getSessionAttribute(boolean.class, UnifyCoreSessionAttributeConstants.INPUT_GLOBAL_ACCOUNTING_FLAG);
	}

	/**
	 * Checks if session is in widget date format override mode
	 * 
	 * @return the widget date format override flag
	 * @throws UnifyException if an error occurs
	 */
	protected boolean isWidgetDateFormatOverride() throws UnifyException {
		return getSessionAttribute(boolean.class, UnifyCoreSessionAttributeConstants.OVERRIDE_WIDGET_DATEFORMAT_FLAG);
	}

	/**
	 * Gets widget date format override formatter.
	 * 
	 * @return the override formatter otherwise null
	 * @throws UnifyException if an error occurs
	 */
	protected Formatter<Object> getWidgetDateFormatOverrideFormatter() throws UnifyException {
		if (isWidgetDateFormatOverride()) {
			String formatterUpl = getSessionAttribute(String.class,
					UnifyCoreSessionAttributeConstants.WIDGET_DATEFORMAT_OVERRIDE);
			if (!StringUtils.isBlank(formatterUpl)) {
				return unifyComponentContext.getFormatter(formatterUpl);
			}
		}

		return null;
	}

	/**
	 * Gets session formatter.
	 * 
	 * @return the override formatter otherwise null
	 * @throws UnifyException if an error occurs
	 */
	protected Formatter<Object> getSessionFormatter(String name) throws UnifyException {
		String formatterUpl = getSessionAttribute(String.class, name);
		if (!StringUtils.isBlank(formatterUpl)) {
			return unifyComponentContext.getFormatter(formatterUpl);
		}

		return null;
	}

	/**
	 * Gets current session user token.
	 * 
	 * @return the current session user token
	 * @throws UnifyException if an error occurs
	 */
	protected UserToken getUserToken() throws UnifyException {
		return unifyComponentContext.getSessionContext().getUserToken();
	}

	/**
	 * Sets the user token tenant ID for current session.
	 * 
	 * @param tenantId the tenant ID
	 * @throws UnifyException if an error occurs
	 */
	protected void setUserTokenTenantId(Long tenantId) throws UnifyException {
		unifyComponentContext.getSessionContext().setUserTokenTenantId(tenantId);
	}

	/**
	 * Sets request page ID (pid)
	 * 
	 * @param pid the page ID
	 * @throws UnifyException if an error occurs
	 */
	protected void setRequestClientPageId(String pid) throws UnifyException {
		unifyComponentContext.getRequestContext().setPid(pid);
	}
	
	/**
	 * Gets request page ID (pid)
	 * 
	 * @return the page ID
	 * @throws UnifyException if an error occurs
	 */
	protected String getRequestClientPageId() throws UnifyException {
		return unifyComponentContext.getRequestContext().getPid();
	}
	
	/**
	 * Gets client request target
	 * 
	 * @return the request target
	 * @throws UnifyException if an error occurs
	 */
	protected String getClientRequestTarget() throws UnifyException {
		return unifyComponentContext.getRequestContext().getRequestTarget();
	}
	
	/**
	 * Sets an attribute in current request.
	 * 
	 * @param name  the attribute name
	 * @param value the attribute value to set
	 * @throws UnifyException if an error occurs
	 */
	protected void setRequestAttribute(String name, Object value) throws UnifyException {
		unifyComponentContext.getRequestContext().setAttribute(name, value);
	}

	/**
	 * Gets an attribute value from current request.
	 * 
	 * @param name the attribute name
	 * @return the attribute value
	 * @throws UnifyException if an error occurs
	 */
	protected Object getRequestAttribute(String name) throws UnifyException {
		return unifyComponentContext.getRequestContext().getAttribute(name);
	}

	/**
	 * Gets an attribute value from current request.
	 * 
	 * @param dataType the data type to convert to
	 * @param name     the attribute name
	 * @return the attribute value
	 * @throws UnifyException if an error occurs
	 */
	protected <T> T getRequestAttribute(Class<T> dataType, String name) throws UnifyException {
		Object val = unifyComponentContext.getRequestContext().getAttribute(name);
		return DataUtils.convert(dataType, val);
	}

	/**
	 * Removes an attribute value from current request.
	 * 
	 * @param name the attribute name
	 * @return the attribute value
	 * @throws UnifyException if an error occurs
	 */
	protected Object removeRequestAttribute(String name) throws UnifyException {
		return unifyComponentContext.getRequestContext().removeAttribute(name);
	}

	/**
	 * Checks if request context has an attribute.
	 * 
	 * @param name the attribute name
	 * @return a true value if attribute exists in request context otherwise false
	 * @throws UnifyException if an error occurs
	 */
	protected boolean isRequestAttribute(String name) throws UnifyException {
		return unifyComponentContext.getRequestContext().isAttribute(name);
	}

	/**
	 * Logs a message at DEBUG level.
	 * 
	 * @param taskMonitor the task monitor
	 * @param message     the message to log
	 * @param params      message parameters
	 */
	protected void logDebug(TaskMonitor taskMonitor, String message, Object... params) {
		log(taskMonitor, LoggingLevel.DEBUG, message, params);
	}

	/**
	 * Logs a message at INFO level.
	 * 
	 * @param taskMonitor the task monitor
	 * @param message     the message to log
	 * @param params      message parameters
	 */
	protected void logInfo(TaskMonitor taskMonitor, String message, Object... params) {
		log(taskMonitor, LoggingLevel.INFO, message, params);
	}

	/**
	 * Logs a message at WARN level.
	 * 
	 * @param taskMonitor the task monitor
	 * @param message     the message to log
	 * @param params      message parameters
	 */
	protected void logWarn(TaskMonitor taskMonitor, String message, Object... params) {
		log(taskMonitor, LoggingLevel.WARN, message, params);
	}

	/**
	 * Logs a message at ERROR level.
	 * 
	 * @param taskMonitor the task monitor
	 * @param message     the message to log
	 * @param params      message parameters
	 */
	protected void logError(TaskMonitor taskMonitor, String message, Object... params) {
		log(taskMonitor, LoggingLevel.ERROR, message, params);
	}

	/**
	 * Logs a exception at ERROR level.
	 * 
	 * @param taskMonitor the task monitor
	 * @param exception   the exception to log
	 */
	protected void logError(TaskMonitor taskMonitor, Exception exception) {
		log(taskMonitor, LoggingLevel.ERROR, exception);
	}

	/**
	 * Logs a message at DEBUG level.
	 * 
	 * @param message the message to log
	 * @param params  message parameters
	 */
	protected void logDebug(String message, Object... params) {
		log(null, LoggingLevel.DEBUG, message, params);
	}

	/**
	 * Logs a message at INFO level.
	 * 
	 * @param message the message to log
	 * @param params  message parameters
	 */
	protected void logInfo(String message, Object... params) {
		log(null, LoggingLevel.INFO, message, params);
	}

	/**
	 * Logs a message at WARN level.
	 * 
	 * @param message the message to log
	 * @param params  message parameters
	 */
	protected void logWarn(String message, Object... params) {
		log(null, LoggingLevel.WARN, message, params);
	}

	/**
	 * Logs a message at ERROR level.
	 * 
	 * @param message the message to log
	 * @param params  message parameters
	 */
	protected void logError(String message, Object... params) {
		log(null, LoggingLevel.ERROR, message, params);
	}

	/**
	 * Logs a exception at ERROR level.
	 * 
	 * @param exception the exception to log
	 */
	protected void logError(Exception exception) {
		log(null, LoggingLevel.ERROR, exception);
	}

	/**
	 * Logs a unify error at ERROR level.
	 * 
	 * @param unifyError the error to log
	 */
	protected void logError(UnifyError unifyError) {
		log(LoggingLevel.ERROR, unifyError);
	}

	/**
	 * Logs a message at SEVERE level.
	 * 
	 * @param message the message to log
	 * @param params  message parameters
	 */
	protected void logSevere(String message, Object... params) {
		log(null, LoggingLevel.SEVERE, message, params);
	}

	/**
	 * Logs a exception at SEVERE level.
	 * 
	 * @param exception the exception to log
	 */
	protected void logSevere(Exception exception) {
		log(null, LoggingLevel.SEVERE, exception);
	}

	/**
	 * Get JSON string from object.
	 * 
	 * @param src the object to print
	 * @return the JSON string if src is not null otherwise null
	 * @throws UnifyException if an error occurs
	 */
	protected String asJson(Object src) throws UnifyException {
		if (src != null) {
			return DataUtils.asJsonString(src, PrintFormat.NONE);
		}

		return null;
	}

	/**
	 * Gets object from JSON string.
	 * 
	 * @param type the object type
	 * @param json the JSON string
	 * @return the object otherwise null
	 * @throws UnifyException if an error occurs
	 */
	protected <T> T fromJson(Class<T> type, String json) throws UnifyException {
		if (!StringUtils.isBlank(json)) {
			return DataUtils.fromJsonString(type, json);
		}

		return null;
	}

	/**
	 * Get pretty JSON string from object.
	 * 
	 * @param src the object to print
	 * @return the pretty JSON
	 * @throws UnifyException if an error occurs
	 */
	protected String prettyJson(Object src) throws UnifyException {
		if (src != null) {
			return DataUtils.asJsonString(src, PrintFormat.PRETTY);
		}

		return null;
	}

	/**
	 * Get pretty JSON string from object on info.
	 * 
	 * @param src the object to print
	 * @return the pretty JSON
	 * @throws UnifyException if an error occurs
	 */
	protected String prettyJsonOnInfo(Object src) throws UnifyException {
		return prettyJsonOn(LoggingLevel.INFO, src);
	}

	/**
	 * Get pretty JSON string from object on debug.
	 * 
	 * @param src the object to print
	 * @return the pretty JSON
	 * @throws UnifyException if an error occurs
	 */
	protected String prettyJsonOnDebug(Object src) throws UnifyException {
		return prettyJsonOn(LoggingLevel.DEBUG, src);
	}

	/**
	 * Get pretty JSON string from object on warn.
	 * 
	 * @param src the object to print
	 * @return the pretty JSON
	 * @throws UnifyException if an error occurs
	 */
	protected String prettyJsonOnWarn(Object src) throws UnifyException {
		return prettyJsonOn(LoggingLevel.WARN, src);
	}

	/**
	 * Get pretty JSON string from object on error.
	 * 
	 * @param src the object to print
	 * @return the pretty JSON
	 * @throws UnifyException if an error occurs
	 */
	protected String prettyJsonOnError(Object src) throws UnifyException {
		return prettyJsonOn(LoggingLevel.ERROR, src);
	}

	/**
	 * Get pretty JSON string from object on severe.
	 * 
	 * @param src the object to print
	 * @return the pretty JSON
	 * @throws UnifyException if an error occurs
	 */
	protected String prettyJsonOnSevere(Object src) throws UnifyException {
		return prettyJsonOn(LoggingLevel.SEVERE, src);
	}

	private String prettyJsonOn(LoggingLevel level, Object src) throws UnifyException {

		if (src != null) {
			Logger logger = unifyComponentContext.getLogger();
			final boolean enabled = logger.isEnabled(level);
			if (enabled) {
				return DataUtils.asJsonString(src, PrintFormat.PRETTY);
			}
		}

		return null;
	}

	/**
	 * Gets a formatted message. Obtains a message from the messages component using
	 * supplied messageKey and then formats using supplied parameters.
	 * 
	 * @param locale     the locale
	 * @param messageKey the message key
	 * @param params     the formatting parameters
	 * @return the formatted message
	 * @throws UnifyException if message with supplied key is missing from the
	 *                        messages component.
	 */
	protected String getMessage(Locale locale, String messageKey, Object... params) throws UnifyException {
		return unifyComponentContext.getMessages().getMessage(locale, messageKey, params);
	}

	/**
	 * Gets a formatted message using application locale. Obtains a message from
	 * application messages component using supplied messageKey and then formats
	 * using supplied parameters. Uses the locale based on localeType.
	 * 
	 * @param messageKey the message key
	 * @param params     the formatting parameters
	 * @return the formatted message
	 * @throws UnifyException if message with supplied key is missing from the
	 *                        messages component.
	 */
	protected String getApplicationMessage(String messageKey, Object... params) throws UnifyException {
		return unifyComponentContext.getMessages().getMessage(getLocale(LocaleType.APPLICATION), messageKey, params);
	}

	/**
	 * Gets an array of application messages using supplied message keys.
	 * 
	 * @param messageKeys the message keys
	 * @return array of messages
	 * @throws UnifyException if an error occurs
	 */
	protected String[] getApplicationMessages(String... messageKeys) throws UnifyException {
		return getMessages(LocaleType.APPLICATION, messageKeys);
	}

	/**
	 * Gets a formatted message using current session locale. Obtains a message from
	 * application messages component using supplied messageKey and then formats
	 * using supplied parameters. Uses the locale based on localeType.
	 * 
	 * @param messageKey the message key
	 * @param params     the formatting parameters
	 * @return the formatted message
	 * @throws UnifyException if message with supplied key is missing from the
	 *                        messages component.
	 */
	protected String getSessionMessage(String messageKey, Object... params) throws UnifyException {
		return unifyComponentContext.getMessages().getMessage(getLocale(LocaleType.SESSION), messageKey, params);
	}

	/**
	 * Gets an array of session messages using supplied message keys.
	 * 
	 * @param messageKeys the message keys
	 * @return array of messages
	 * @throws UnifyException if an error occurs
	 */
	protected String[] getSessionMessages(String... messageKeys) throws UnifyException {
		return getMessages(LocaleType.SESSION, messageKeys);
	}

	/**
	 * Resolves a message string using application locale. A message is resolved by
	 * checking if it is a value enclosed with a tag. If enclosed with a message tag
	 * <em>$m{&lt;value&gt;}</em>, the value is used as a key to obtain a message
	 * resource from the messages component. The obtained message is returned as the
	 * resolved string. If enclosed with a string tag <em>$s{&lt;value&gt;}</em>,
	 * the value is extracted and returned as the resolved string. For any other
	 * condition, the string is returned as is.
	 * 
	 * @param message the message to resolve.
	 * @param params  parameters
	 * @return the resolved string or null if supplied string is null
	 * @throws UnifyException If an error occurs
	 */
	protected String resolveApplicationMessage(String message, Object... params) throws UnifyException {
		return resolveMessage(getLocale(LocaleType.APPLICATION), message, params);
	}

	/**
	 * Resolves a message string using current session's locale. A message is
	 * resolved by checking if it is a value enclosed with a tag. If enclosed with a
	 * message tag <em>$m{&lt;value&gt;}</em>, the value is used as a key to obtain
	 * a message resource from the messages component. The obtained message is
	 * returned as the resolved string. If enclosed with a string tag
	 * <em>$s{&lt;value&gt;}</em>, the value is extracted and returned as the
	 * resolved string. For any other condition, the string is returned as is.
	 * 
	 * @param message the message to resolve.
	 * @param params  parameters
	 * @return the resolved string or null if supplied string is null
	 * @throws UnifyException If an error occurs
	 */
	protected String resolveSessionMessage(String message, Object... params) throws UnifyException {
		return resolveMessage(getLocale(LocaleType.SESSION), message, params);
	}

	/**
	 * Resolves a message string using specified locale. A message is resolved by
	 * checking if it is a value enclosed with a tag. If enclosed with a message tag
	 * <em>$m{&lt;value&gt;}</em>, the value is used as a key to obtain a message
	 * resource from the messages component. The obtained message is returned as the
	 * resolved string. If enclosed with a string tag <em>$s{&lt;value&gt;}</em>,
	 * the value is extracted and returned as the resolved string. For any other
	 * condition, the string is returned as is.
	 * 
	 * @param locale  the locale to use
	 * @param message the message to resolve.
	 * @param params  parameters
	 * @return the resolved string or null if supplied string is null
	 * @throws UnifyException If an error occurs
	 */
	protected String resolveMessage(Locale locale, String message, Object... params) throws UnifyException {
		if (message != null) {
			int start = TokenUtils.indexOfMessageToken(message);
			if (start >= 0) {
				int end = message.indexOf('}', start);
				if (end > 0) {
					return message.substring(0, start) + getUnifyComponentContext().getMessages().getMessage(locale,
							message.substring(start + 3, end), params) + message.substring(end + 1);
				}
			}

			if (TokenUtils.isUnifyPropertyToken(message)) {
				String resolved = getContainerSetting(String.class, TokenUtils.extractTokenValue(message));
				return resolved != null && params != null && params.length > 0 ? MessageFormat.format(resolved, params)
						: resolved;
			}

			if (TokenUtils.isStringToken(message)) {
				message = TokenUtils.extractTokenValue(message);
			}

			if (params != null && params.length > 0) {
				return MessageFormat.format(message, params);
			}
		}

		return message;
	}

	/**
	 * Gets a message from an exception. If the exception is an instance of
	 * {@link UnifyException}, the method uses the exception's error code and error
	 * parameters to obtain a formatted message from the messages component; which
	 * is then returned as the result. Otherwise, the exception
	 * {@link Exception#getMessage()} is returned.
	 * 
	 * @param localeType the locale type
	 * @param exception  the exception to get message from. Can not be null.
	 * @return the exception message
	 * @throws UnifyException if an error occurs
	 */
	protected String getExceptionMessage(LocaleType localeType, Throwable exception) throws UnifyException {
		if (exception instanceof UnifyException) {
			return getErrorMessage(localeType, ((UnifyException) exception).getUnifyError());
		}

		return exception.getMessage();
	}

	protected boolean isTenancyEnabled() throws UnifyException {
		return getContainerSetting(boolean.class, UnifyCorePropertyConstants.APPLICATION_TENANCY_ENABLED);
	}

	protected String getPrintableStackTraceWithMessageHeader(LocaleType localeType, Throwable exception)
			throws UnifyException {
		String header = getExceptionMessage(localeType, exception);
		return StringUtils.getPrintableStackTrace(exception, header);
	}

	protected String getPrintableStackTrace(Throwable exception) throws UnifyException {
		return StringUtils.getPrintableStackTrace(exception);
	}

	protected String getErrorMessage(LocaleType localeType, UnifyError ue) throws UnifyException {
		return ue.getErrorCode() + ": " + unifyComponentContext.getMessages().getMessage(getLocale(localeType),
				ue.getErrorCode(), ue.getErrorParams());
	}

	/**
	 * Gets a message for an error.
	 * 
	 * @param localeType the locale type
	 * @param err        the error object to get message for. Can not be null.
	 * @return the exception message
	 * @throws UnifyException if an error occurs
	 */
	protected String getExceptionMessage(LocaleType localeType, UnifyError err) throws UnifyException {
		return unifyComponentContext.getMessages().getMessage(getLocale(localeType), err.getErrorCode(),
				err.getErrorParams());
	}

	/**
	 * Adds a message to supplied task monitor. Resolves message if supplied as a
	 * message token.
	 * 
	 * @param taskMonitor the task monitor to add message to
	 * @param message     the message to add
	 * @param params      optional message parameters
	 * @throws UnifyException if an error occurs
	 */
	protected void addTaskMessage(TaskMonitor taskMonitor, String message, Object... params) throws UnifyException {
		if (taskMonitor != null) {
			taskMonitor.addMessage(resolveSessionMessage(message, params));
		}
	}

	/**
	 * Gets the description of a list item. A list is obtained from component's list
	 * manager using the supplied listName then supplied itemKey is applied to the
	 * list to obtain the item description.
	 * 
	 * @param localeType the locale type
	 * @param listName   the name of the list
	 * @param itemKey    the list key to the list item
	 * @return the list item description or null if item with key is not found
	 * @throws UnifyException if list with supplied name is unknown by the list
	 *                        manager
	 */
	protected Listable getListItemByKey(LocaleType localeType, String listName, String itemKey) throws UnifyException {
		return unifyComponentContext.getListManager().getListItemByKey(getLocale(localeType), listName, itemKey);
	}

	protected Listable getListItemByDescription(LocaleType localeType, String listName, String itemDesc)
			throws UnifyException {
		return unifyComponentContext.getListManager().getListItemByDescription(getLocale(localeType), listName,
				itemDesc);
	}

	/**
	 * Gets a list based on supplied parameters.
	 * 
	 * @param localeType the locale type
	 * @param listName   the list name
	 * @param params     the list parameters
	 * @return a list object containing listable items
	 * @throws UnifyException if an error occurs
	 */
	protected List<? extends Listable> getList(LocaleType localeType, String listName, Object... params)
			throws UnifyException {
		return unifyComponentContext.getListManager().getList(getLocale(localeType), listName, params);
	}

	/**
	 * Gets a list based on supplied parameters.
	 * 
	 * @param localeType the locale type
	 * @param listName   the list name
	 * @param filter     optional filter
	 * @param limit      result limit
	 * @param params     the list parameters
	 * @return a list object containing listable items
	 * @throws UnifyException if an error occurs
	 */
	protected List<? extends Listable> getList(LocaleType localeType, String listName, String filter, int limit,
			Object... params) throws UnifyException {
		return unifyComponentContext.getListManager().getSubList(getLocale(localeType), listName, filter, limit,
				params);
	}

	/**
	 * Gets a list map using application locale.
	 * 
	 * @param localeType the locale type
	 * @param listName   the list name
	 * @param params     the list parameters
	 * @return a list map
	 * @throws UnifyException if an error occurs
	 */
	protected Map<String, Listable> getListMap(LocaleType localeType, String listName, Object... params)
			throws UnifyException {
		return unifyComponentContext.getListManager().getListMap(getLocale(localeType), listName, params);
	}

	/**
	 * Gets a list map using application locale.
	 * 
	 * @param localeType the locale type
	 * @param listName   the list name
	 * @param filter     optional filter
	 * @param limit      result limit
	 * @param params     the list parameters
	 * @return a list map
	 * @throws UnifyException if an error occurs
	 */
	protected Map<String, Listable> getListMap(LocaleType localeType, String listName, String filter, int limit,
			Object... params) throws UnifyException {
		return unifyComponentContext.getListManager().getSubListMap(getLocale(localeType), listName, filter, limit,
				params);
	}

	/**
	 * Gets the application locale
	 * 
	 * @return the application locale
	 * @throws UnifyException if an error occurs
	 */
	protected Locale getApplicationLocale() throws UnifyException {
		return unifyComponentContext.getApplicationLocale();
	}

	/**
	 * Gets application time zone
	 * 
	 * @return the application time zone
	 * @throws UnifyException if an error occurs
	 */
	protected TimeZone getApplicationTimeZone() throws UnifyException {
		return unifyComponentContext.getApplicationTimeZone();
	}

	/**
	 * Checks if view directive should be ignored.
	 * 
	 * @return true if ignore view directive is set for application otherwise false
	 * @throws UnifyException if an error occurs
	 */
	protected boolean isApplicationIgnoreViewDirective() throws UnifyException {
		return unifyComponentContext.isApplicationIgnoreViewDirective();
	}

	/**
	 * Gets the application banner ASCII text
	 * 
	 * @return the application banner text as a list of strings
	 * @throws UnifyException if an error occurs
	 */
	protected List<String> getApplicationBanner() throws UnifyException {
		return unifyComponentContext.getApplicationBanner();
	}

	/**
	 * Gets the session locale
	 * 
	 * @return the session locale
	 * @throws UnifyException if an error occurs
	 */
	protected Locale getSessionLocale() throws UnifyException {
		return unifyComponentContext.getRequestContext().getLocale();
	}

	/**
	 * Resolves UTC based on supplied timestamp and current session.
	 * 
	 * @param timestamp the timestamp to use
	 * @return the resolved UTC as milliseconds since January 1, 1970 00:00:00
	 * @throws UnifyException if an error occurs
	 */
	protected long resolveUTC(Date timestamp) throws UnifyException {
		return timestamp.getTime() - getSessionContext().getTimeZoneOffset();
	}

	/**
	 * Throws a component operation error unify exception. Wraps the supplied
	 * exception in a unify exception with component operation error code.
	 * 
	 * @param e the exception to warp
	 * @throws UnifyException always thrown
	 */
	protected void throwOperationErrorException(Exception e) throws UnifyException {
		Throwable t = e.getCause();
		if (t instanceof UnifyException) {
			throw ((UnifyException) t);
		}
		throw new UnifyOperationException(e, getName(), e.getMessage());
	}

	/**
	 * Throws a component unsupported operation unify exception.
	 * 
	 * @throws UnifyException always thrown
	 */
	protected void throwUnsupportedOperationException() throws UnifyException {
		Exception e = new UnsupportedOperationException();
		throw new UnifyOperationException(e, getName(), e.getMessage());
	}

	protected Long getUserTenantId() throws UnifyException {
		UserToken userToken = getUserToken();
		Long tenantId = userToken != null ? getUserToken().getTenantId() : null;
		if (tenantId == null) {
			return Entity.PRIMARY_TENANT_ID;
		}

		return tenantId;
	}

	protected List<ParamConfig> getComponentParamConfigs(Class<? extends UnifyComponent> componentType, String name)
			throws UnifyException {
		UnifyComponentConfig ucc = getComponentConfig(componentType, name);
		List<ParamConfig> list = new ArrayList<ParamConfig>();
		List<Parameter> pal = AnnotationUtils.getParameters(ucc.getType());
		for (Parameter pa : pal) {
			list.add(createParamConfig(pa));
		}

		DataUtils.sortAscending(list, ParamConfig.class, "order");
		return list;
	}

	protected ParamConfig createParamConfig(Parameter pa) throws UnifyException {
		String paramName = AnnotationUtils.getAnnotationString(pa.name());
		if (paramName == null) {
			paramName = AnnotationUtils.getAnnotationString(pa.value());
		}

		String description = AnnotationUtils.getAnnotationString(pa.description());
		if (description != null) {
			description = resolveApplicationMessage(description);
		}

		return new ParamConfig(pa.type(), paramName, description, pa.editor(), pa.order(), pa.mandatory());
	}

	/**
	 * Pauses for specified milliseconds.
	 * 
	 * @param milliSeconds the milliseconds to pause for
	 */
	protected void pause(long milliSeconds) {
		ThreadUtils.sleep(milliSeconds);
	}
	
	/**
	 * Executes on initialization. Called after the component's context has been
	 * properly set. Implementing classes are expected to perform initialization in
	 * this method.
	 * 
	 * @throws UnifyException if an error occurs.
	 */
	protected abstract void onInitialize() throws UnifyException;

	/**
	 * Executes on termination.
	 * 
	 * @throws UnifyException if an error occurs.
	 */
	protected abstract void onTerminate() throws UnifyException;

	private Locale getLocale(LocaleType localeType) throws UnifyException {
		if (isInitialized()) {
			if (LocaleType.SESSION.equals(localeType)) {
				return unifyComponentContext.getRequestContext().getLocale();
			}
			return unifyComponentContext.getApplicationLocale();
		}

		return Locale.getDefault();
	}

	private String[] getMessages(LocaleType localeType, String... messageKeys) throws UnifyException {
		if (isInitialized()) {
			ResourceBundles resourceBundles = unifyComponentContext.getMessages();
			Locale locale = getLocale(localeType);
			String[] messages = new String[messageKeys.length];
			for (int i = 0; i < messages.length; i++) {
				messages[i] = resourceBundles.getMessage(locale, messageKeys[i]);
			}

			return messages;
		}

		return DataUtils.ZEROLEN_STRING_ARRAY;
	}

	private void log(TaskMonitor taskMonitor, LoggingLevel loggingLevel, String message, Object... params) {
		if (isInitialized()) {
			try {
				Logger logger = unifyComponentContext.getLogger();
				boolean enabled = logger.isEnabled(loggingLevel);
				if (enabled || taskMonitor != null) {
					String resolvedMsg = resolveApplicationMessage(message, params);
					if (enabled) {
						logger.log(loggingLevel, resolvedMsg);
					}

					if (taskMonitor != null) {
						taskMonitor.addMessage(resolvedMsg);
					}
				}
			} catch (UnifyException e) {
				e.printStackTrace();
			}
		}
	}

	private void log(LoggingLevel loggingLevel, UnifyError unifyError) {
		if (isInitialized()) {
			try {
				Logger logger = unifyComponentContext.getLogger();
				if (logger.isEnabled(loggingLevel)) {
					logger.log(loggingLevel, getErrorMessage(LocaleType.APPLICATION, unifyError));
				}
			} catch (UnifyException e) {
				e.printStackTrace();
			}
		}
	}

	private void log(TaskMonitor taskMonitor, LoggingLevel loggingLevel, Exception exception) {
		if (isInitialized()) {
			try {
				Logger logger = unifyComponentContext.getLogger();
				boolean enabled = logger.isEnabled(loggingLevel);
				if (enabled || taskMonitor != null) {
					String msg = getExceptionMessage(LocaleType.APPLICATION, exception);
					if (enabled) {
						logger.log(loggingLevel, msg, exception);
					}

					if (taskMonitor != null) {
						taskMonitor.addMessage(msg);
					}
				}
			} catch (UnifyException e) {
				e.printStackTrace();
			}
		}
	}
}
