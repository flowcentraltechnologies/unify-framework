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
package com.tcdng.unify.web.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Web path utilities tests.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class WebPathUtilsTest {

	@Test
	public void testStripOffCIDBlank() throws Exception {
		assertNull(WebPathUtils.stripOffCID(null));
		assertEquals("", WebPathUtils.stripOffCID(""));
		assertEquals(" ", WebPathUtils.stripOffCID(" "));
	}

	@Test
	public void testStripOffCIDNotPresent() throws Exception {
		assertEquals("id=25", WebPathUtils.stripOffCID("id=25"));
		assertEquals("id=25&name=Tess", WebPathUtils.stripOffCID("id=25&name=Tess"));
		assertEquals("id=25&name=Tess&name=cidss", WebPathUtils.stripOffCID("id=25&name=Tess&name=cidss"));
	}

	@Test
	public void testStripOffCIDPresent() throws Exception {
		assertEquals("", WebPathUtils.stripOffCID("id__=cid25"));
		assertEquals("ret=1234", WebPathUtils.stripOffCID("id__=cid25&ret=1234"));
		assertEquals("id=25", WebPathUtils.stripOffCID("id=25&name__=cidTess"));
		assertEquals("id=25&name=cidss", WebPathUtils.stripOffCID("id=25&name__=cidTess&name=cidss"));
	}

}
