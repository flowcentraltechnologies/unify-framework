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
package com.tcdng.unify.web.ui.widget.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.web.ui.widget.AbstractMultiControl;
import com.tcdng.unify.web.ui.widget.Control;

/**
 * Rich text editor.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component("ui-richtexteditor")
@UplAttributes({
		@UplAttribute(name = "rows", type = int.class),
		@UplAttribute(name = "format", type = boolean.class, defaultVal = "true"),
		@UplAttribute(name = "link", type = boolean.class, defaultVal = "true"),
		@UplAttribute(name = "list", type = boolean.class, defaultVal = "true"),
		@UplAttribute(name = "color", type = boolean.class, defaultVal = "true"),
		@UplAttribute(name = "size", type = boolean.class, defaultVal = "true"),
		@UplAttribute(name = "align", type = boolean.class, defaultVal = "true"),
		@UplAttribute(name = "spellCheck", type = boolean.class) })
public class RichTextEditor extends AbstractMultiControl {

	private static final String DEFAULT_FONT_SIZE = "16px";

	private static final String DEFAULT_FONT_COLOR = "#000000";

	private static final int MIN_ROWS = 4;

	private Control boldCtrl;

	private Control italicCtrl;

	private Control underlineCtrl;

	private Control setFontSizeCtrl;

	private Control setFontColorCtrl;

	private Control fontSizeCtrl;

	private Control fontColorCtrl;

	private Control leftAlignCtrl;

	private Control centerAlignCtrl;

	private Control rightAlignCtrl;

	private Control ulistCtrl;

	private Control olistCtrl;

	private Control linkCtrl;

	private Control urlCtrl;

	private Control valueCtrl;

	private List<Control> acontrols;

	private List<Control> bcontrols;

	private String fontSize;

	private String fontColor;

	public RichTextEditor() {
		this.fontSize = DEFAULT_FONT_SIZE;
		this.fontColor = DEFAULT_FONT_COLOR;
	}

	@Override
	public void addPageAliases() throws UnifyException {
		if (isSize()) {
			addPageAlias(fontSizeCtrl);
		}
		
		if (isColor()) {
			addPageAlias(fontColorCtrl);
		}
		
		addPageAlias(valueCtrl);
	}

	public int getRows() throws UnifyException {
		int rows = getUplAttribute(int.class, "rows");
		return rows < MIN_ROWS ? MIN_ROWS : rows;
	}

	public boolean isFormat() throws UnifyException {
		return getUplAttribute(boolean.class, "format");
	}

	public boolean isColor() throws UnifyException {
		return getUplAttribute(boolean.class, "color");
	}

	public boolean isSize() throws UnifyException {
		return getUplAttribute(boolean.class, "size");
	}

	public boolean isAlign() throws UnifyException {
		return getUplAttribute(boolean.class, "align");
	}

	public boolean isSpellCheck() throws UnifyException {
		return getUplAttribute(boolean.class, "spellCheck");
	}

	public boolean isLink() throws UnifyException {
		return getUplAttribute(boolean.class, "link");
	}

	public boolean isList() throws UnifyException {
		return getUplAttribute(boolean.class, "list");
	}

	public String getEditorId() throws UnifyException {
		return getPrefixedId("etr_");
	}

	public String getContent() throws UnifyException {
		return getStringValue();
	}

	public void setContent(String content) throws UnifyException {
		setValue(content);
	}

	public Control getBoldCtrl() {
		return boldCtrl;
	}

	public Control getItalicCtrl() {
		return italicCtrl;
	}

	public Control getUnderlineCtrl() {
		return underlineCtrl;
	}

	public Control getSetFontSizeCtrl() {
		return setFontSizeCtrl;
	}

	public Control getSetFontColorCtrl() {
		return setFontColorCtrl;
	}

	public Control getFontSizeCtrl() {
		return fontSizeCtrl;
	}

	public Control getFontColorCtrl() {
		return fontColorCtrl;
	}

	public Control getLeftAlignCtrl() {
		return leftAlignCtrl;
	}

	public Control getCenterAlignCtrl() {
		return centerAlignCtrl;
	}

	public Control getRightAlignCtrl() {
		return rightAlignCtrl;
	}

	public Control getUlistCtrl() {
		return ulistCtrl;
	}

	public Control getOlistCtrl() {
		return olistCtrl;
	}

	public Control getLinkCtrl() {
		return linkCtrl;
	}

	public Control getUrlCtrl() {
		return urlCtrl;
	}

	public Control getValueCtrl() {
		return valueCtrl;
	}

	public List<Control> getRowAControls() {
		return acontrols;
	}

	public List<Control> getRowBControls() {
		return bcontrols;
	}

	public String getFontSize() {
		return fontSize;
	}

	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}

	public String getFontColor() {
		return fontColor;
	}

	public void setFontColor(String fontColor) {
		this.fontColor = fontColor;
	}

	@Override
	protected void doOnPageConstruct() throws UnifyException {
		acontrols = new ArrayList<Control>();
		bcontrols = new ArrayList<Control>();
		
		if (isFormat()) {
			boldCtrl = (Control) addInternalChildWidget("!ui-button symbol:$s{bold} styleClass:$e{btn}");
			italicCtrl = (Control) addInternalChildWidget("!ui-button symbol:$s{italic} styleClass:$e{btn}");
			underlineCtrl = (Control) addInternalChildWidget("!ui-button symbol:$s{underline} styleClass:$e{btn}");
			acontrols.add(boldCtrl);
			acontrols.add(italicCtrl);
			acontrols.add(underlineCtrl);
		}
		
		valueCtrl = (Control) addInternalChildWidget("!ui-hidden binding:content");

		if (isSize()) {
			fontSizeCtrl = (Control) addInternalChildWidget(
					"!ui-select list:$s{richtextfontsizelist} blankOption:$s{} styleClass:$e{sel} binding:fontSize");
			setFontSizeCtrl = (Control) addInternalChildWidget("!ui-button symbol:$s{text-size} styleClass:$e{btn}");
			acontrols.add(fontSizeCtrl);
			acontrols.add(setFontSizeCtrl);
		}
		
		if (isColor()) {
			fontColorCtrl = (Control) addInternalChildWidget(
					"!ui-select list:$s{richtextfontcolorlist} blankOption:$s{} styleClass:$e{sel} binding:fontColor");
			setFontColorCtrl = (Control) addInternalChildWidget("!ui-button symbol:$s{paint-brush} styleClass:$e{btn}");
			acontrols.add(fontColorCtrl);
			acontrols.add(setFontColorCtrl);
		}
		
		if (isAlign()) {
			leftAlignCtrl = (Control) addInternalChildWidget("!ui-button symbol:$s{align-left} styleClass:$e{btn}");
			centerAlignCtrl = (Control) addInternalChildWidget("!ui-button symbol:$s{align-center} styleClass:$e{btn}");
			rightAlignCtrl = (Control) addInternalChildWidget("!ui-button symbol:$s{align-right} styleClass:$e{btn}");
			acontrols.add(leftAlignCtrl);
			acontrols.add(centerAlignCtrl);
			acontrols.add(rightAlignCtrl);
		}
		
		if (isLink()) {
			urlCtrl = (Control) addInternalChildWidget("!ui-text size:48");
			linkCtrl = (Control) addInternalChildWidget("!ui-button symbol:$s{link} styleClass:$e{btn}");
			bcontrols.add(urlCtrl);
			bcontrols.add(linkCtrl);
		}
		
		if (isList()) {
			ulistCtrl = (Control) addInternalChildWidget("!ui-button symbol:$s{list-unordered} styleClass:$e{btn}");
			olistCtrl = (Control) addInternalChildWidget("!ui-button symbol:$s{list-ordered} styleClass:$e{btn}");
			bcontrols.add(ulistCtrl);
			bcontrols.add(olistCtrl);
		}
		
		acontrols = Collections.unmodifiableList(acontrols);
	}

}
