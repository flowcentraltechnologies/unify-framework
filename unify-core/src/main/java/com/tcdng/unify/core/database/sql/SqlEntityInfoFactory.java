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
package com.tcdng.unify.core.database.sql;

import java.util.List;

import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.CallableProc;

/**
 * SQL entity information factory.
 * 
 * @author The Code Department
 * @since 4.1
 */
public interface SqlEntityInfoFactory extends UnifyComponent {

	/**
	 * Gets all SQL entity information.
	 * 
	 * @return the list of entity information for this data source
	 * @throws UnifyException if an error occurs
	 */
	List<SqlEntityInfo> getSqlEntityInfos() throws UnifyException;
	
	/**
	 * Checks if factory is with entity information.
	 * 
	 * @param clazz the entity type
	 * @return true if present otherwise false
	 * @throws UnifyException if an error occurs
	 */
	boolean isWithSqlEntityInfo(Class<?> clazz) throws UnifyException;
	
	/**
	 * Finds all SQL child entity information for an entity.
	 * 
	 * @param clazz the entity type
	 * @return the type SQL child entity information
	 * @throws UnifyException if supplied entity type is not found. if an error
	 *                        occurs
	 */
	List<SqlEntityInfo> findAllChildSqlEntityInfos(Class<?> clazz) throws UnifyException;

    /**
     * Finds the SQL entity information for an entity.
     * 
     * @param clazz
     *            the entity type
     * @return the type SQL entity information
     * @throws UnifyException
     *             if supplied entity type is not found. if an error occurs
     */
    SqlEntityInfo findSqlEntityInfo(Class<?> clazz) throws UnifyException;

    /**
     * Creates the SQL entity information for a entity type if not existing.
     * 
     * @param entityClass
     *            the entity class
     * @return entity information for specified entity type.
     * @throws UnifyException
     *             if an error occurs
     */
    SqlEntityInfo createSqlEntityInfo(Class<?> entityClass) throws UnifyException;

    /**
     * Removes the SQL entity information for a entity type from factory.
     * 
     * @param entityClass
     *            the entity class
     * @return entity information for specified entity type if removed otherwise null.
     * @throws UnifyException
     *             if an error occurs
     */
    SqlEntityInfo removeSqlEntityInfo(Class<?> entityClass) throws UnifyException;

    /**
     * Returns the SQL callable information for type.
     * 
     * @param callableClass
     *            the callable type
     * @return callable information for specified type.
     * @throws UnifyException
     *             if an error occurs
     */
    SqlCallableInfo getSqlCallableInfo(Class<? extends CallableProc> callableClass) throws UnifyException;

    /**
     * Sets the factory SQL data source dialect.
     * 
     * @param sqlDataSourceDialect
     *            the dialect to set
     */
    void setSqlDataSourceDialect(SqlDataSourceDialect sqlDataSourceDialect);
}
