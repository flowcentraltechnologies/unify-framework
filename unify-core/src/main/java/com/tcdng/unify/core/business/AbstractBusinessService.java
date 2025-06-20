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
package com.tcdng.unify.core.business;

import java.util.Date;
import java.util.Map;

import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.EntityEventSource;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.constant.TopicEventType;
import com.tcdng.unify.core.database.Database;
import com.tcdng.unify.core.database.DatabaseTransactionManager;
import com.tcdng.unify.core.database.dynamic.sql.DynamicSqlDatabaseManager;
import com.tcdng.unify.core.database.sql.SqlDataSourceManager;
import com.tcdng.unify.core.task.TaskLauncher;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.core.util.CalendarUtils;

/**
 * An abstract base class that implements the basic requirements of a business
 * service. Any concrete subclass of this class is managed by the container.
 * 
 * @author The Code Department
 * @since 4.1
 */
public abstract class AbstractBusinessService extends AbstractUnifyComponent implements BusinessService {

	@Configurable(ApplicationComponents.APPLICATION_DATABASE)
	private Database db;

	@Configurable
	private DatabaseTransactionManager databaseTransactionManager;

	@Configurable
	private DynamicSqlDatabaseManager dynamicSqlDatabaseManager;

	@Configurable(ApplicationComponents.APPLICATION_DATASOURCEMANAGER)
	private SqlDataSourceManager sqlDataSourceManager;
	
	@Configurable
	private TaskLauncher taskLauncher;

	@Configurable
	private EntityEventSource entityEventSource;
	
	@Override
	public DatabaseTransactionManager tm() throws UnifyException {
		return databaseTransactionManager;
	}

	@Transactional
	@Override
	public Entity getExtendedInstance(Class<? extends Entity> entityClass) throws UnifyException {
		return db().getExtendedInstance(entityClass);
	}

	@Transactional
	@Override
	public Date getToday() throws UnifyException {
		return CalendarUtils.getMidnightDate(db().getNow());
	}

	@Transactional
	@Override
	public Date getNow() throws UnifyException {
		return db().getNow();
	}

	@Override
	protected void onInitialize() throws UnifyException {

	}

	@Override
	protected void onTerminate() throws UnifyException {

	}
	
	/**
	 * Returns application database
	 */
	protected Database db() throws UnifyException {
		return db;
	}

	protected Database db(Class<? extends Entity> entityClass) throws UnifyException {
		return db(sqlDataSourceManager.getDataSource(entityClass));
	}

	/**
	 * Gets a dynamic database instance using data source with supplied
	 * configuration name.
	 * 
	 * @param dataSourceConfigName the data source configuration name. Data source
	 *                             must be already registered in default application
	 *                             dynamic data source manager.
	 * @return the database instance
	 * @throws UnifyException if an error occurs
	 */
	protected Database db(String dataSourceConfigName) throws UnifyException {
		return ApplicationComponents.APPLICATION_DATASOURCE.equals(dataSourceConfigName) ? db
				: dynamicSqlDatabaseManager.getDynamicSqlDatabase(dataSourceConfigName);
	}

	/**
	 * Executes a business logic unit.
	 * 
	 * @param taskMonitor a task monitoring object
	 * @param unitName    the unit name
	 * @param parameters  logic input parameters
	 * @return the business logic output
	 * @throws UnifyException if an error occurs
	 */
	protected BusinessLogicOutput executeBusinessLogic(TaskMonitor taskMonitor, String unitName,
			Map<String, Object> parameters) throws UnifyException {
		BusinessLogicUnit blu = (BusinessLogicUnit) getComponent(unitName);
		BusinessLogicInput input = new BusinessLogicInput(taskMonitor);
		input.setParameters(parameters);

		BusinessLogicOutput output = new BusinessLogicOutput();
		blu.execute(input, output);
		return output;
	}

	/**
	 * Launches a task.
	 * 
	 * @param taskSetup the task setup
	 * @return the task monitor
	 * @throws UnifyException if an error occurs
	 */
	protected TaskMonitor launchTask(TaskSetup taskSetup) throws UnifyException {
		return taskLauncher.launchTask(taskSetup);
	}

	/**
	 * Commits all pending database transactions
	 * 
	 * @throws UnifyException if an error occurs
	 */
	protected void commitTransactions() throws UnifyException { 
		databaseTransactionManager.commit();
	}

	/**
	 * Sets roll back on current transactions in database session.
	 * 
	 * @throws UnifyException if an error occurs
	 */
	protected void setRollbackTransactions() throws UnifyException {
		databaseTransactionManager.setRollback();
	}

	/**
	 * Clears roll back on current transactions in database session.
	 * 
	 * @throws UnifyException if an error occurs
	 */
	protected void clearRollbackTransactions() throws UnifyException {
		databaseTransactionManager.clearRollback();
	}

	/**
	 * Sets save point.
	 * 
	 * @throws UnifyException if an error occurs
	 */
	protected void setSavePoint() throws UnifyException {
		databaseTransactionManager.setSavePoint();
	}

	/**
	 * Clears save point.
	 * 
	 * @throws UnifyException if an error occurs
	 */
	protected void clearSavePoint() throws UnifyException {
		databaseTransactionManager.clearSavePoint();
	}

	/**
	 * Rollback to savepoint
	 * 
	 * @throws UnifyException if an error occurs
	 */
	protected void rollbackToSavePoint() throws UnifyException {
		databaseTransactionManager.rollbackToSavePoint();
	}
	
	/**
	 * Sets of an entity event with current transaction.
	 * 
	 * @param eventType   the event type
	 * @param entityClass the entity class
	 * @param id          optional entity ID
	 * @throws UnifyException if an error occurs
	 */
	protected void setOffEntityEvent(TopicEventType eventType, Class<? extends Entity> entityClass, Object id)
			throws UnifyException {
		final String srcClientId = entityEventSource != null ? entityEventSource.getCurrentRequestClientId() : null;
		databaseTransactionManager.setOffEntityEvent(eventType, srcClientId, entityClass, id);
	}
}
