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

package com.tcdng.unify.fontawesome;

import java.util.Arrays;
import java.util.List;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.web.font.AbstractFontSymbolManager;

/**
 * Font Awesome symbol manager.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component(FontAwesomeApplicationComponents.FONTAWESOME_FONTSYMBOLMANAGER)
public class FontAwesomeSymbolManager extends AbstractFontSymbolManager {

	@Override
	public List<String> getFontResources() throws UnifyException {
		// Currently uses font awesome 7.0.0
		return Arrays.asList(
				"webfonts/fa-solid-900.woff2",
				"webfonts/fa-regular-400.woff2",
				"webfonts/fa-brands-400.woff2");
	}

	@Override
	public List<String> getRegularFontResources() throws UnifyException {
		// Currently uses font awesome 7.0.0
		return Arrays.asList(
				"webfonts/fa-regular-400.woff2",
				"webfonts/fa-solid-900.woff2",
				"webfonts/fa-brands-400.woff2");
	}

    @Override
    protected void onInitialize() throws UnifyException {
        super.onInitialize();
        
        // Register symbols by name
        registerSymbol("about", "f059");
        registerSymbol("align-center", "f037");
        registerSymbol("align-left", "f036");
        registerSymbol("align-right", "f038");
        registerSymbol("angle-double-down", "f103");
        registerSymbol("angle-double-left", "f100");
        registerSymbol("angle-double-right", "f101");
        registerSymbol("angle-double-up", "f102");
        registerSymbol("angle-down", "f107");
        registerSymbol("angle-left", "f104");
        registerSymbol("angle-right", "f105");
        registerSymbol("angle-up", "f106");
        registerSymbol("application", "f3fa");
        registerSymbol("archive", "f187");
        registerSymbol("arrow-down", "f063");
        registerSymbol("arrow-left", "f060");
        registerSymbol("arrow-right", "f061");
        registerSymbol("arrow-up", "f062");
        registerSymbol("backward", "f04a");
        registerSymbol("backward-fast", "f049");
        registerSymbol("ban", "f05e");
        registerSymbol("bell", "f0f3");
        registerSymbol("bell-slash", "f1f6");
        registerSymbol("bold", "f032");
        registerSymbol("bomb", "f1e2");
        registerSymbol("buffer", "f837");
        registerSymbol("calendar", "f133");
        registerSymbol("calendar-alt", "f073");
        registerSymbol("camera", "f030");
        registerSymbol("caret-down", "f0d7");
        registerSymbol("caret-left", "f0d9");
        registerSymbol("caret-right", "f0da");
        registerSymbol("caret-up", "f0d8");
        registerSymbol("chart-area", "f1fe");
        registerSymbol("chart-bar", "f080");
        registerSymbol("chart-pie", "f200");
        registerSymbol("chevron-circle-down", "f13a");
        registerSymbol("chevron-circle-left", "f137");
        registerSymbol("chevron-circle-right", "f138");
        registerSymbol("chevron-circle-up", "f139");
        registerSymbol("chevron-down", "f078");
        registerSymbol("chevron-left", "f053");
        registerSymbol("chevron-right", "f054");
        registerSymbol("chevron-up", "f077");
        registerSymbol("clipboard-check", "f46c");
        registerSymbol("circle-arrow-down", "f358");
        registerSymbol("circle-arrow-left", "f359");
        registerSymbol("circle-arrow-right", "f35a");
        registerSymbol("circle-arrow-up", "f35b");
        registerSymbol("circle-check", "f058");
        registerSymbol("circle-info", "f05a");
        registerSymbol("circle-nodes", "e4e2");
        registerSymbol("circle-pause", "f28b");
        registerSymbol("clock", "f017");
        registerSymbol("cloud", "f0c2");
        registerSymbol("cloud-download", "f381");
        registerSymbol("cloud-upload", "f382");
        registerSymbol("code-branch", "f126");
        registerSymbol("collapse", "f066");
        registerSymbol("copy", "f0c5");
        registerSymbol("cog", "f013");
        registerSymbol("cogs", "f085");
        registerSymbol("compact-disk", "f51f");
        registerSymbol("credit-card", "f09d");
        registerSymbol("cross", "f00d");
        registerSymbol("cubes", "f1b3");
        registerSymbol("cut", "f0c4");
        registerSymbol("database", "f1c0");
        registerSymbol("desktop", "f108");
        registerSymbol("diff", "f53e");
        registerSymbol("directions", "f5eb");
        registerSymbol("door-open", "f52b");
        registerSymbol("download", "f019");
        registerSymbol("edit", "f044");
        registerSymbol("ellipsis-horizontal", "f141");
        registerSymbol("ellipsis-vertical", "f142");
        registerSymbol("eye", "f06e");
        registerSymbol("equals", "f52c");
        registerSymbol("ethernet", "f796");
        registerSymbol("exchange-alt", "f362");
        registerSymbol("exclamation-circle", "f06a");
        registerSymbol("expand", "f065");
        registerSymbol("file", "f15b");
        registerSymbol("file-alt", "f15c");
        registerSymbol("file-contract", "f56c");
        registerSymbol("file-download", "f56d");
        registerSymbol("file-edit", "f31c");
        registerSymbol("file-excel", "f1c3");
        registerSymbol("file-export", "f56e");
        registerSymbol("file-import", "f56f");
        registerSymbol("file-invoice", "f570");
        registerSymbol("file-pdf", "f1c1");
        registerSymbol("file-plus", "f477");
        registerSymbol("file-upload", "f574");
        registerSymbol("file-word", "f1c2");
        registerSymbol("filter", "f0b0");
        registerSymbol("flag", "f024");
        registerSymbol("flag-checkered", "f11e");
        registerSymbol("folder", "f07b");
        registerSymbol("folder-open", "f07c");
        registerSymbol("folder-plus", "f65e");
        registerSymbol("forward", "f04e");
        registerSymbol("forward-fast", "f050");
        registerSymbol("globe-africa", "f57c");
        registerSymbol("grid", "f00a");
        registerSymbol("hat-wizard", "f6e8");
        registerSymbol("harddisk", "f0a0");
        registerSymbol("history", "f1da");
        registerSymbol("house-user", "e065");
        registerSymbol("id-card", "f2c2");
        registerSymbol("id-badge", "f2c1");
        registerSymbol("image", "f03e");
        registerSymbol("info", "f129");
        registerSymbol("italic", "f033");
        registerSymbol("key", "f084");
        registerSymbol("laptop-house", "e066");
        registerSymbol("laugh", "f599");
        registerSymbol("lightbulb", "f0eb");
        registerSymbol("link", "f0c1");
        registerSymbol("list", "f03a");
        registerSymbol("list-unordered", "f0ca");
        registerSymbol("list-ordered", "f0cb");
        registerSymbol("lock", "f023");
        registerSymbol("logs", "f46d");
        registerSymbol("man", "f183");
        registerSymbol("magic", "f0d0");
        registerSymbol("mail", "f0e0");
        registerSymbol("mail-bulk", "f674");
        registerSymbol("minus", "f068");
        registerSymbol("minus-square", "f146");
        registerSymbol("money-bill", "f0d6");
        registerSymbol("mountain", "f6fc");
        registerSymbol("network-wired", "f6ff");
        registerSymbol("newspaper", "f1ea");
        registerSymbol("outdent", "f03b");
        registerSymbol("paint-brush", "f1fc");
        registerSymbol("paper-clip", "f0c6");
        registerSymbol("paper-plane", "f1d8");
        registerSymbol("parachute-box", "f4cd");
        registerSymbol("pen-nib", "f5ad");
        registerSymbol("play", "f04b");
        registerSymbol("plus", "f067");
        registerSymbol("plus-square", "f0fe");
        registerSymbol("pop-up", "f35d");
        registerSymbol("project-diagram", "f542");
        registerSymbol("question", "f128");
        registerSymbol("question-circle", "f059");
        registerSymbol("radiation", "f7b9");
        registerSymbol("redo", "f01e");
        registerSymbol("redo-alt", "f2f9");
        registerSymbol("rocket", "f135");
        registerSymbol("satellite-disk", "f7c0");
        registerSymbol("save", "f0c7");
        registerSymbol("scroll", "f70e");
        registerSymbol("search", "f002");
        registerSymbol("sign-in", "f2f6");
        registerSymbol("sign-out", "f2f5");
        registerSymbol("site-map", "f0e8");
        registerSymbol("sort", "f0dc");
        registerSymbol("square-check", "f14a");
        registerSymbol("step", "f54b");
        registerSymbol("stream", "f550");
        registerSymbol("swap", "f362");
        registerSymbol("sync", "f2f1");
        registerSymbol("table", "f0ce");
        registerSymbol("table-list", "f00b");
        registerSymbol("tasks", "f0ae");
        registerSymbol("text", "f039");
        registerSymbol("text-a", "f031");
        registerSymbol("text-size", "f034");
        registerSymbol("thumbtack", "f08d");
        registerSymbol("times-circle", "f057");
        registerSymbol("trash", "f2ed");
        registerSymbol("triangle-exclamation", "f071");
        registerSymbol("underline", "f0cd");
        registerSymbol("undo", "f2ea");
        registerSymbol("unlock", "f09c");
        registerSymbol("user", "f007");
        registerSymbol("users", "f0c0");
        registerSymbol("user-check", "f4fc");
        registerSymbol("user-cog", "f4fe");
        registerSymbol("user-edit", "f4ff");
        registerSymbol("user-tag", "f507");
        registerSymbol("vector-square", "f0ac");
        registerSymbol("window-maximize", "f2d0");
        registerSymbol("window-restore", "f2d2");
        registerSymbol("woman", "f182");
    }

}
