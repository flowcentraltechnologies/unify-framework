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
package com.tcdng.unify.web.ui.widget;

import java.util.List;
import java.util.Set;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;

/**
 * A user interface component that contains other user interface components.
 * 
 * @author The Code Department
 * @since 4.1
 */
public interface Container extends DataTransferWidget, WidgetContainer {

    /**
     * Sets the container's widget repository.
     * 
     * @param widgetRepository
     *            the repository to set
     * @throws UnifyException
     *             if an error occurs
     */
    void setWidgetRepository(WidgetRepository widgetRepository) throws UnifyException;

    /**
     * Gets the container's widget repository.
     * 
     * @return the widget repository
     */
    WidgetRepository getWidgetRepository();

    /**
     * Returns true if container has a widget repository.
     */
    boolean hasWidgetRepository();

    /**
     * Returns the long names of all widgets in this container.
     * 
     * @return the list of component long names
     * @throws UnifyException
     *             if an error occurs
     */
    Set<String> getWidgetLongNames() throws UnifyException;

	/**
	 * Gets this container's repeat widget.
	 * 
	 * @return the repeat widget
	 * @throws UnifyException if an error occurs
	 */
	Widget getRepeatWidget() throws UnifyException;
    
    /**
     * Gets widget by long name.
     * 
     * @param longName
     *            - the component long name
     * @return Widget - the component
     * @exception UnifyException
     *                if component with long name is unknown
     */
    Widget getWidgetByLongName(String longName) throws UnifyException;

    /**
     * Returns true if widget with supplied long name exists in this container.
     * 
     * @param longName
     *            the long name to use
     * @throws UnifyException
     *             if an error occurs
     */
    boolean isWidget(String longName) throws UnifyException;

    /**
     * Gets a widget by short name.
     * 
     * @param shortName
     *            the component short name
     * @return Widget - the component
     * @exception UnifyException
     *                if component with short name is unknown
     */
    Widget getWidgetByShortName(String shortName) throws UnifyException;

    /**
     * Returns this containers layout component list.
     * 
     * @throws UnifyException
     *             if an error occurs
     */
    List<String> getLayoutWidgetLongNames() throws UnifyException;

    /**
     * Sets the disabled state of a widget in container.
     * 
     * @param shortName
     *            the widget short name
     * @param disabled
     *            the disabled flag to set
     * @throws UnifyException
     *             if an error occurs
     */
    void setWidgetDisabled(String shortName, boolean disabled) throws UnifyException;

    /**
     * Returns the disabled state flag of a widget in container.
     * 
     * @param shortName
     *            the widget short name
     * @throws UnifyException
     *             if an error occurs
     */
    boolean isWidgetDisabled(String shortName) throws UnifyException;

    /**
     * Sets the visible state of a widget in container.
     * 
     * @param shortName
     *            the widget short name
     * @param visible
     *            the disabled flag to set
     * @throws UnifyException
     *             if an error occurs
     */
    void setWidgetVisible(String shortName, boolean visible) throws UnifyException;

    /**
     * Returns the visible state flag of a widget in container.
     * 
     * @param shortName
     *            the widget short name
     * @throws UnifyException
     *             if an error occurs
     */
    boolean isWidgetVisible(String shortName) throws UnifyException;

    /**
     * Sets the editable state of a widget in container.
     * 
     * @param shortName
     *            the widget short name
     * @param editable
     *            the editable flag to set
     * @throws UnifyException
     *             if an error occurs
     */
    void setWidgetEditable(String shortName, boolean editable) throws UnifyException;

    /**
     * Returns the editable state flag of a widget in container.
     * 
     * @param shortName
     *            the widget short name
     * @throws UnifyException
     *             if an error occurs
     */
    boolean isWidgetEditable(String shortName) throws UnifyException;

    /**
     * Sets the focused state of a widget in container.
     * 
     * @param shortName
     *            the widget short name
     * @throws UnifyException
     *             if an error occurs
     */
    void setWidgetFocus(String shortName) throws UnifyException;

    /**
     * Returns a widget from page associated with this container by long name.
     * 
     * @param clazz
     *            the widget type
     * @param longName
     *            the component long name
     * @throws UnifyException
     *             if an error occurs
     */
    <T> T getWidgetByLongName(Class<T> clazz, String longName) throws UnifyException;

    /**
     * Returns a widget from page associated with this container.
     * 
     * @param clazz
     *            the widget type
     * @param shortName
     *            the component short name
     * @throws UnifyException
     *             if an error occurs
     */
    <T> T getWidgetByShortName(Class<T> clazz, String shortName) throws UnifyException;

    /**
     * Returns repeat value stores.
     * 
     * @throws UnifyException
     *             if an error occurs
     */
    ValueStore getRepeatValueStores() throws UnifyException;

    /**
     * Returns container layout
     * 
     * @throws UnifyException
     *             if an error occurs
     */
    Layout getLayout() throws UnifyException;

    /**
     * Returns true if container spacing
     * 
     * @throws UnifyException
     *             if an error occurs
     */
    boolean isSpace() throws UnifyException;

    /**
     * Returns true if container is repeater
     */
    boolean isRepeater();

    /**
     * Returns true if container should switch state always
     */
    boolean isSwitchStateAlways();

    /**
     * Returns use-layout-if-present flag.
     */
    boolean isUseLayoutIfPresent();

    /**
     * Returns true if container is in alternate mode.
     * 
     * @throws UnifyException
     *             if an error occurs
     */
    boolean isAlternate() throws UnifyException ;

    /**
     * Cascades value store to child components.
     * 
     * @throws UnifyException
     *             if an error occurs
     */
    void cascadeValueStore() throws UnifyException;
}
