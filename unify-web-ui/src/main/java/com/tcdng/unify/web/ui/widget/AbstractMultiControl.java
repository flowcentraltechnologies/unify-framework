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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.upl.UplElementReferences;
import com.tcdng.unify.core.upl.UplUtils;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.ui.DataTransferBlock;
import com.tcdng.unify.web.ui.util.WidgetUtils;
import com.tcdng.unify.web.ui.widget.panel.StandalonePanel;

/**
 * Serves as a base class for controls that contain and make use of other
 * widgets.
 * 
 * @author The Code Department
 * @since 4.1
 */
@UplAttributes({
	@UplAttribute(name = "components", type = UplElementReferences.class),
    @UplAttribute(name = "valueMarker", type = String.class) })
public abstract class AbstractMultiControl extends AbstractControl implements MultiControl {

    private Map<String, ChildWidgetInfo> widgetInfoMap;

    private ValueStore thisValueStore;

    private List<String> standalonePanelNames;

    private String uplValueMarker;
    
    private int childIndex;
    
    public AbstractMultiControl() {
        widgetInfoMap = new LinkedHashMap<String, ChildWidgetInfo>();
    }

    @Override
    public final void onPageConstruct() throws UnifyException {
        super.onPageConstruct();        
        uplValueMarker = getUplAttribute(String.class, "valueMarker");   
        doOnPageConstruct();
    }
    
    @Override
    public void addChildWidget(Widget widget) throws UnifyException {
        doAddChildWidget(widget, false, false, false, true);
    }

    @Override
    public void setValueStore(ValueStore valueStore) throws UnifyException {
        super.setValueStore(valueStore);
        for (ChildWidgetInfo childWidgetInfo : widgetInfoMap.values()) {
            if (childWidgetInfo.isConforming()) {
                childWidgetInfo.getWidget().setValueStore(valueStore);
            }
        }
    }

    @Override
    public void setDisabled(boolean disabled) {
        super.setDisabled(disabled);
        for (ChildWidgetInfo childWidgetInfo : widgetInfoMap.values()) {
            if (!childWidgetInfo.isIgnoreParentState()) {
                childWidgetInfo.getWidget().setDisabled(disabled);
            }
        }
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        for (ChildWidgetInfo childWidgetInfo : widgetInfoMap.values()) {
            if (!childWidgetInfo.isIgnoreParentState()) {
                childWidgetInfo.getWidget().setEditable(editable);
            }
        }
    }

    @Override
    public void populate(DataTransferBlock transferBlock) throws UnifyException {
        if (transferBlock != null) {
            DataTransferBlock childBlock = transferBlock.getChildBlock();
            if (childBlock == null) {
                super.populate(transferBlock);
            } else {
            	ChildWidgetInfo childWidgetInfo = getChildWidgetInfo(childBlock.getId());
            	if (childWidgetInfo != null) {
                    DataTransferWidget dtWidget = (DataTransferWidget) childWidgetInfo.getWidget();
                    dtWidget.populate(childBlock);
                    onInternalChildPopulated(dtWidget);
            	}
            }
        }
    }

    @Override
    public ChildWidgetInfo getChildWidgetInfo(String childId) {
        return widgetInfoMap.get(childId);
    }

    @Override
    public Collection<ChildWidgetInfo> getChildWidgetInfos() {
        return widgetInfoMap.values();
    }

    @Override
    public int getChildWidgetCount() {
        return widgetInfoMap.size();
    }

    @Override
    public void setId(String id) throws UnifyException {
        boolean changed = !DataUtils.equals(getId(), id);
        super.setId(id);
        if (changed && !widgetInfoMap.isEmpty()) {
            Map<String, ChildWidgetInfo> map = new LinkedHashMap<String, ChildWidgetInfo>();
            for (ChildWidgetInfo childWidgetInfo : widgetInfoMap.values()) {
                Widget widget = childWidgetInfo.getWidget();
                String newChildId = WidgetUtils.renameChildId(id, widget.getId());
                widget.setId(newChildId);
                map.put(newChildId, childWidgetInfo);
            }

            widgetInfoMap = map;
        }
    }

    @Override
    public final Object getValue(String attribute) throws UnifyException {
        if (attribute != null) {
            return super.getValue(attribute);
        }

        if (getValueStore() != null) {
            return getValueStore().getValueObject();
        }

        return null;
    }

    @Override
    public Widget getChildWidget(String childId) throws UnifyException {
        if (widgetInfoMap != null) {
            ChildWidgetInfo childWidgetInfo = widgetInfoMap.get(childId);
            if (childWidgetInfo != null) {
                return childWidgetInfo.getWidget();
            }
        }
        
        return null;
    }

    protected String getUplValueMarker() {
        return uplValueMarker;
    }

    /**
     * Creates and adds a non-conforming external child widget that doesn't ignore
     * parent state.
     * 
     * @param descriptor
     *            descriptor used to create child widget.
     * @return the added child widget
     * @throws UnifyException
     *             if an error occurs
     */
    protected Widget addExternalChildWidget(String descriptor) throws UnifyException {
        Widget widget = (Widget) getUplComponent(getSessionLocale(), descriptor, false);
        doAddChildWidget(widget, true, false, false, true);
        return widget;
    }

    /**
     * Creates and adds a non-conforming external child standalone panel that
     * doesn't ignore parent state.
     * 
     * @param panelName
     *            the panelName
     * @param cloneId
     *            the clone ID
     * @return the added child widget
     * @throws UnifyException
     *             if an error occurs
     */
	protected Widget addExternalChildStandalonePanel(String panelName, String cloneId) throws UnifyException {
		return addExternalChildStandalonePanel(panelName, cloneId, null);
	}
    
    /**
     * Creates and adds a non-conforming external child standalone panel that
     * doesn't ignore parent state using binding.
     * 
     * @param panelName
     *            the panelName
     * @param cloneId
     *            the clone ID
     * @param binding
     *            the binding
     * @return the added child widget
     * @throws UnifyException
     *             if an error occurs
     */
	protected Widget addExternalChildStandalonePanel(String panelName, String cloneId, String binding)
			throws UnifyException {
		final String uniqueName = UplUtils.generateUplComponentCloneName(panelName, cloneId);
		Page page = resolveRequestPage();
		StandalonePanel standalonePanel = page.getStandalonePanel(uniqueName);
		if (standalonePanel == null) {
			standalonePanel = getPageManager().createStandalonePanel(getSessionLocale(), uniqueName, binding);
			page.addStandalonePanel(uniqueName, standalonePanel);
			getUIControllerUtil().updatePageControllerInfo(
					getRequestContextUtil().getResponsePathParts().getControllerName(), uniqueName);
			if (standalonePanelNames == null) {
				standalonePanelNames = new ArrayList<String>();
			}

			standalonePanelNames.add(uniqueName);
		}

		standalonePanel.setContainer(getContainer());
		doAddChildWidget(standalonePanel, true, false, false, true);
		return standalonePanel;
	}

    @Override
    public void addPageAliases() throws UnifyException {
        super.addPageAliases();

        if (standalonePanelNames != null) {
            Page page = resolveRequestPage();
            for (String uniqueName : standalonePanelNames) {
                StandalonePanel standalonePanel = page.getStandalonePanel(uniqueName);
                if (standalonePanel != null) {
                    List<String> aliases = getPageManager().getExpandedReferences(standalonePanel.getId());
                    getRequestContextUtil().addPageAlias(getId(), DataUtils.toArray(String.class, aliases));
                }
            }
        }
    }

    @Override
	protected final ValueStore createValueStore(Object storageObject, int dataIndex) throws UnifyException {
		if (uplValueMarker != null) {
			final int tabIndex = getTabIndex();
			return super.createValueStore(storageObject,
					tabIndex >= 0 ? uplValueMarker + "_" + tabIndex : uplValueMarker, dataIndex);
		}

		return super.createValueStore(storageObject, dataIndex);
	}

    /**
     * Removes all external child widgets.
     * 
     * @throws UnifyException
     *             if an error occurs
     */
    protected void removeAllExternalChildWidgets() throws UnifyException {
        Map<String, ChildWidgetInfo> map = new LinkedHashMap<String, ChildWidgetInfo>();
        for (Map.Entry<String, ChildWidgetInfo> entry: widgetInfoMap.entrySet()) {
            if (!entry.getValue().isExternal()) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        widgetInfoMap =  map;
        
        if (standalonePanelNames != null) {
            Page page = resolveRequestPage();
            for (String uniqueName : standalonePanelNames) {
                page.removeStandalonePanel(uniqueName);
            }
        }

        standalonePanelNames = null;
    }

    /**
     * Creates and adds a non-conforming internal child widget that doesn't ignore
     * parent state.
     * 
     * @param descriptor
     *            descriptor used to create child widget.
     * @return the added child widget
     * @throws UnifyException
     *             if an error occurs
     */
    protected Widget addInternalChildWidget(String descriptor) throws UnifyException {
        return addInternalChildWidget(descriptor, false, false);
    }

    /**
     * Creates and adds an internal child widget.
     * 
     * @param descriptor
     *            descriptor used to create child widget.
     * @param conforming
     *            indicates if child is conforming
     * @param ignoreParentState
     *            set this flag to true if child widget ignore parent state.
     * @return the added child widget
     * @throws UnifyException
     *             if an error occurs
     */
    protected Widget addInternalChildWidget(String descriptor, boolean conforming, boolean ignoreParentState)
            throws UnifyException {
        Widget widget = (Widget) getUplComponent(getSessionLocale(), descriptor, false);
        doAddChildWidget(widget, true, conforming, ignoreParentState, false);
        return widget;
    }

    /**
     * Adds child widget id to request context page aliases.
     * 
     * @param widget
     *            the child widget
     * @throws UnifyException
     *             if an error occurs
     */
    protected void addPageAlias(Widget widget) throws UnifyException {
        getRequestContextUtil().addPageAlias(getId(), widget.getId());
    }

    /**
     * Adds id to request context page aliases.
     * 
     * @param id
     *            the to add
     * @throws UnifyException
     *             if an error occurs
     */
    protected void addPageAlias(String id) throws UnifyException {
        getRequestContextUtil().addPageAlias(getId(), id);
    }

    protected void onInternalChildPopulated(Widget widget) throws UnifyException {

    }

    protected Control createInternalHiddenControl(String binding) throws UnifyException {
        return (Control) addInternalChildWidget("!ui-hidden binding:" + binding);
    }

    protected abstract void doOnPageConstruct() throws UnifyException;

    private void doAddChildWidget(Widget widget, boolean pageConstruct, boolean conforming, boolean ignoreParentState,
            boolean external) throws UnifyException {
        final String childId = WidgetUtils.getChildId(getId(), widget.getBaseId(), childIndex++);
        widget.setId(childId);
        if (pageConstruct) {
            widget.onPageConstruct();
            widget.setContainer(getContainer());
        }

        if (!ignoreParentState) {
            widget.setEditable(isEditable());
            widget.setDisabled(isDisabled());
        }

        if (conforming) {
            widget.setValueStore(getValueStore());
        } else {
            widget.setValueStore(getThisValueStore());
        }

        widget.setConforming(conforming);
        widgetInfoMap.put(childId, new ChildWidgetInfo(widget, ignoreParentState, external));
    }

    private ValueStore getThisValueStore() throws UnifyException {
        if (thisValueStore == null) {
            thisValueStore = createValueStore(this);
        }

        return thisValueStore;
    }

    public static class ChildWidgetInfo {

        private Widget widget;

        private String name;
        
        private boolean external;

        private boolean ignoreParentState;

        public ChildWidgetInfo(Widget widget, boolean ignoreParentState, boolean external) {
            this.widget = widget;
            this.ignoreParentState = ignoreParentState;
            this.external = external;
        }

        public Widget getWidget() {
            return widget;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isIgnoreParentState() {
            return ignoreParentState;
        }

        public boolean isExternal() {
            return external;
        }

        public boolean isConforming() {
            return widget.isConforming();
        }

        public boolean isControl() {
            return widget.isControl();
        }

        public boolean isPanel() {
            return widget.isPanel();
        }

        public boolean isPrivilegeVisible() throws UnifyException {
            return widget.isVisible();
        }
    }
}
