/*
 * Copyright (c) 2018-2026 The Code Department.
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
package com.tcdng.unify.core.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.tcdng.unify.core.AbstractUnifyComponentTest;
import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.UnifyCorePropertyConstants;
import com.tcdng.unify.core.UnifyException;

/**
 * Database table managed identity entity CRUD tests.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class DatabaseTableEntityManagedIdentityCRUDTest extends AbstractUnifyComponentTest {

	private DatabaseTransactionManager tm;

	private Database db;


	@Test
	public void testCountRecord() throws Exception {
		tm.beginTransaction();
		try {
			db.create(new Fish("apple", "red", 20.00));
			db.create(new Fish("pineapple", "cyan", 60.00));
			db.create(new Fish("banana", "yellow", 45.00));
			db.create(new Fish("orange", "orange", 15.00));
			assertEquals(4, db.countAll(new FishQuery().ignoreEmptyCriteria(true)));
			assertEquals(2, db.countAll(new FishQuery().lesseq("price", 20.00)));
			assertEquals(1, db.countAll(new FishQuery().begins("name", "ban")));
		} catch (Exception e) {
			tm.setRollback();
			throw e;
		} finally {
			tm.endTransaction();
		}
	}

	@Test
	public void testExistsRecord() throws Exception {
		tm.beginTransaction();
		try {
			assertFalse(db.exists(new FishQuery().ignoreEmptyCriteria(true)));
			assertFalse(db.exists(new FishQuery().lesseq("price", 20.00)));
			
			db.create(new Fish("apple", "red", 20.00));
			db.create(new Fish("pineapple", "cyan", 60.00));
			db.create(new Fish("banana", "yellow", 45.00));
			db.create(new Fish("orange", "orange", 15.00));
			
			assertTrue(db.exists(new FishQuery().ignoreEmptyCriteria(true)));
			assertTrue(db.exists(new FishQuery().lesseq("price", 20.00)));
			assertTrue(db.exists(new FishQuery().begins("name", "ban")));
			
			assertFalse(db.exists(new FishQuery().greatereq("price", 65.00)));
			assertFalse(db.exists(new FishQuery().begins("name", "tan")));
		} catch (Exception e) {
			tm.setRollback();
			throw e;
		} finally {
			tm.endTransaction();
		}
	}

	@Test
	public void testGetMinValueEmptyCriteria() throws Exception {
		tm.beginTransaction();
		try {
			db.create(new Fish("apple", "red", 20.00));
			db.create(new Fish("pineapple", "cyan", 60.00));
			db.create(new Fish("banana", "yellow", 45.00));
			db.create(new Fish("orange", "orange", 15.00));
			assertEquals(Double.valueOf(15.00),
					db.min(Double.class, "price", new FishQuery().ignoreEmptyCriteria(true)));
		} catch (Exception e) {
			tm.setRollback();
			throw e;
		} finally {
			tm.endTransaction();
		}
	}

	@Test
	public void testGetMinValueWithCriteria() throws Exception {
		tm.beginTransaction();
		try {
			db.create(new Fish("apple", "red", 20.00));
			db.create(new Fish("pineapple", "cyan", 60.00));
			db.create(new Fish("banana", "yellow", 45.00));
			db.create(new Fish("orange", "orange", 15.00));
			assertEquals(Double.valueOf(20.00), db.min(Double.class, "price", new FishQuery().like("name", "app")));
		} catch (Exception e) {
			tm.setRollback();
			throw e;
		} finally {
			tm.endTransaction();
		}
	}

	@Test
	public void testGetMinValueWithCriteriaCaseInsensitive() throws Exception {
		tm.beginTransaction();
		try {
			db.create(new Fish("Apple", "red", 20.00));
			db.create(new Fish("pineapple", "cyan", 60.00));
			db.create(new Fish("banana", "yellow", 45.00));
			db.create(new Fish("orange", "orange", 15.00));
			assertEquals(Double.valueOf(20.00),
					db.min(Double.class, "price", new FishQuery().ilike("name", "APP")));
		} catch (Exception e) {
			tm.setRollback();
			throw e;
		} finally {
			tm.endTransaction();
		}
	}

	@Test
	public void testGetMinValueNoMatchRecords() throws Exception {
		tm.beginTransaction();
		try {
			db.create(new Fish("apple", "red", 20.00));
			db.create(new Fish("pineapple", "cyan", 60.00));
			db.create(new Fish("banana", "yellow", 45.00));
			db.create(new Fish("orange", "orange", 15.00));
			assertNull(db.min(Double.class, "price", new FishQuery().equals("name", "tangerine")));
		} catch (Exception e) {
			tm.setRollback();
			throw e;
		} finally {
			tm.endTransaction();
		}
	}

	@Test
	public void testGetMaxValueEmptyCriteria() throws Exception {
		tm.beginTransaction();
		try {
			db.create(new Fish("apple", "red", 20.00));
			db.create(new Fish("pineapple", "cyan", 60.00));
			db.create(new Fish("banana", "yellow", 45.00));
			db.create(new Fish("orange", "orange", 15.00));
			assertEquals(Double.valueOf(60.00),
					db.max(Double.class, "price", new FishQuery().ignoreEmptyCriteria(true)));
		} catch (Exception e) {
			tm.setRollback();
			throw e;
		} finally {
			tm.endTransaction();
		}
	}

	@Test
	public void testGetMaxValueWithCriteria() throws Exception {
		tm.beginTransaction();
		try {
			db.create(new Fish("apple", "red", 20.00));
			db.create(new Fish("pineapple", "cyan", 60.00));
			db.create(new Fish("banana", "yellow", 45.00));
			db.create(new Fish("orange", "orange", 15.00));
			assertEquals(Double.valueOf(45.00),
					db.max(Double.class, "price", new FishQuery().notlike("name", "app")));
		} catch (Exception e) {
			tm.setRollback();
			throw e;
		} finally {
			tm.endTransaction();
		}
	}

	@Test
	public void testGetMaxValueNoMaxWithCriteria() throws Exception {
		tm.beginTransaction();
		try {
			db.create(new Fish("apple", "red", 20.00));
			db.create(new Fish("pineapple", "cyan", 60.00));
			db.create(new Fish("banana", "yellow", 45.00));
			db.create(new Fish("orange", "orange", 15.00));
			Long maxId = db.max(Long.class, "id", new FishQuery().less("id", 0L));
			assertNull(maxId);
		} catch (Exception e) {
			tm.setRollback();
			throw e;
		} finally {
			tm.endTransaction();
		}
	}

	@Test
	public void testGetMaxValueNoMatchRecords() throws Exception {
		tm.beginTransaction();
		try {
			db.create(new Fish("apple", "red", 20.00));
			db.create(new Fish("pineapple", "cyan", 60.00));
			db.create(new Fish("banana", "yellow", 45.00));
			db.create(new Fish("orange", "orange", 15.00));
			assertNull(db.max(Double.class, "price", new FishQuery().equals("name", "tangerine")));
		} catch (Exception e) {
			tm.setRollback();
			throw e;
		} finally {
			tm.endTransaction();
		}
	}

	@Test
	public void testCreateRecord() throws Exception {
		tm.beginTransaction();
		try {
			Fish fishToCreate = new Fish("apple", "red", 20.00);
			Long id = (Long) db.create(fishToCreate);
			assertNotNull(id);
			assertEquals(id, fishToCreate.getId());

			Fish createdFish = db.find(Fish.class, id);
			assertEquals(fishToCreate, createdFish);
			assertFalse(createdFish.equals(new Fish("banana", "yellow", 45.00)));
		} catch (Exception e) {
			tm.setRollback();
			throw e;
		} finally {
			tm.endTransaction();
		}
	}

	@Test
	public void testPersistenceAfterTransaction() throws Exception {
		Fish apple = new Fish("apple", "red", 20.00);
		Fish banana = new Fish("banana", "yellow", 45.00);
		tm.beginTransaction();
		try {
			db.create(apple);
			db.create(banana);
		} catch (Exception e) {
			tm.setRollback();
			throw e;
		} finally {
			tm.endTransaction();
		}

		tm.beginTransaction();
		try {
			List<Fish> fishList = db.findAll(new FishQuery().ignoreEmptyCriteria(true).order("price"));
			assertEquals(2, fishList.size());
			assertEquals(apple, fishList.get(0));
			assertEquals(banana, fishList.get(1));
		} catch (Exception e) {
			tm.setRollback();
			throw e;
		} finally {
			tm.endTransaction();
		}
	}

	@Test
	public void testRollback() throws Exception {
		tm.beginTransaction();
		try {
			db.create(new Fish("apple", "red", 20.00));
			db.create(new Fish("banana", "yellow", 45.00));
			tm.setRollback();
		} finally {
			tm.endTransaction();
		}

		tm.beginTransaction();
		try {
			assertEquals(0, db.countAll(new FishQuery().ignoreEmptyCriteria(true)));
		} finally {
			tm.endTransaction();
		}
	}

	@Test
	public void testClearRollback() throws Exception {
		tm.beginTransaction();
		try {
			db.create(new Fish("apple", "red", 20.00));
			db.create(new Fish("banana", "yellow", 45.00));
			tm.setRollback();
			tm.clearRollback();
		} finally {
			tm.endTransaction();
		}

		tm.beginTransaction();
		try {
			assertEquals(2, db.countAll(new FishQuery().ignoreEmptyCriteria(true)));
		} finally {
			tm.endTransaction();
		}
	}

	@Test
	public void testRollbackToSavepoint() throws Exception {
		Fish apple = new Fish("apple", "red", 20.00);
		tm.beginTransaction();
		try {
			db.create(apple);
			tm.setSavePoint();

			db.create(new Fish("banana", "yellow", 45.00));
			tm.rollbackToSavePoint();
		} finally {
			tm.endTransaction();
		}

		tm.beginTransaction();
		try {
			List<Fish> fishList = db.findAll(new FishQuery().ignoreEmptyCriteria(true));
			assertEquals(1, fishList.size());
			assertEquals(apple, fishList.get(0));
		} finally {
			tm.endTransaction();
		}
	}

	@Test
	public void testDeleteRecordById() throws Exception {
		tm.beginTransaction();
		try {
			Long id = (Long) db.create(new Fish("apple", "red", 20.00));
			assertEquals(1, db.countAll(new FishQuery().equals("id", id)));
			db.delete(Fish.class, id);
			assertEquals(0, db.countAll(new FishQuery().equals("id", id)));
		} catch (Exception e) {
			tm.setRollback();
			throw e;
		} finally {
			tm.endTransaction();
		}
	}

	@Test(expected = UnifyException.class)
	public void testDeleteRecordByIdWithInvalidId() throws Exception {
		tm.beginTransaction();
		try {
			db.create(new Fish("apple", "red", 20.00));
			db.delete(Fish.class, 20L);
		} catch (Exception e) {
			tm.setRollback();
			throw e;
		} finally {
			tm.endTransaction();
		}
	}

	@Test
	public void testDeleteRecordByIdVersion() throws Exception {
		tm.beginTransaction();
		try {
			Long id = (Long) db.create(new Fish("apple", "red", 20.00));
			Fish fishData = db.find(Fish.class, id);
			db.deleteByIdVersion(fishData);
		} catch (Exception e) {
			tm.setRollback();
			throw e;
		} finally {
			tm.endTransaction();
		}
	}

	@Test
	public void testDeleteAllRecord() throws Exception {
		tm.beginTransaction();
		try {
			db.create(new Fish("apple", "red", 20.00));
			Fish pineapple = new Fish("pineapple", "cyan", 60.00);
			db.create(pineapple);
			db.create(new Fish("banana", "yellow", 45.00));
			db.create(new Fish("orange", "orange", 15.00));
			assertEquals(3, db.deleteAll(new FishQuery().notequals("color", "cyan")));
			List<Fish> testFishList = db.findAll(new FishQuery().ignoreEmptyCriteria(true));
			assertEquals(1, testFishList.size());
			assertEquals(pineapple, testFishList.get(0));
		} catch (Exception e) {
			tm.setRollback();
			throw e;
		} finally {
			tm.endTransaction();
		}
	}

	@Test
	public void testDeleteAllRecordWithOrder() throws Exception {
		tm.beginTransaction();
		try {
			db.create(new Fish("apple", "red", 20.00));
			Fish pineapple = new Fish("pineapple", "cyan", 60.00);
			db.create(pineapple);
			db.create(new Fish("banana", "yellow", 45.00));
			db.create(new Fish("orange", "orange", 15.00));
			assertEquals(3, db.deleteAll(new FishQuery().notequals("color", "cyan")));
			// Order should be ignored
			List<Fish> testFishList = db.findAll(new FishQuery().order("color").ignoreEmptyCriteria(true));
			assertEquals(1, testFishList.size());
			assertEquals(pineapple, testFishList.get(0));
		} catch (Exception e) {
			tm.setRollback();
			throw e;
		} finally {
			tm.endTransaction();
		}
	}

	@Override
	protected void doAddSettingsAndDependencies() throws Exception {
		addContainerSetting(UnifyCorePropertyConstants.APPLICATION_QUERY_LIMIT, 8);
	}

	@Override
	protected void onSetup() throws Exception {
		tm = (DatabaseTransactionManager) getComponent(ApplicationComponents.APPLICATION_DATABASETRANSACTIONMANAGER);
		db = (Database) getComponent(ApplicationComponents.APPLICATION_DATABASE);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	protected void onTearDown() throws Exception {
		deleteAll(Fish.class);
	}
}
