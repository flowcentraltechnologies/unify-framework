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
package com.tcdng.unify.core.data;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.tcdng.unify.core.AbstractUnifyComponentTest;
import com.tcdng.unify.core.constant.DataType;

/**
 * Map value store tests.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class MapValueStoreTest extends AbstractUnifyComponentTest {

    @Test
    public void testDataType() throws Exception {
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("name", null);
    	map.put("salary", null);
    	
    	Map<String, DataType> datatypes = new HashMap<String, DataType>();
    	datatypes.put("name", DataType.STRING);
    	datatypes.put("salary", DataType.DECIMAL);
    	
    	ValueStore store = new MapValueStore(datatypes, map);
    	store.store("name", "Travis");
    	store.store("salary", "45,000.0");
    	
    	assertEquals("Travis", store.retrieve("name"));
    	assertEquals(BigDecimal.valueOf(45000.00), store.retrieve("salary"));
    }

	@Override
	protected void onSetup() throws Exception {
		
	}

	@Override
	protected void onTearDown() throws Exception {
		
	}
}
