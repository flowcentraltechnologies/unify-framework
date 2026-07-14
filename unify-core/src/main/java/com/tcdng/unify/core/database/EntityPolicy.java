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

import java.util.Date;

import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Update;

/**
 * Entity policy.
 * 
 * @author The Code Department
 * @since 4.1
 */
public interface EntityPolicy<T extends Entity> extends UnifyComponent {
    /**
     * Entity pre-create method. Called before creation of entity.
     * 
     * @param entity
     *            the entity to be created
     * @param now
     *            the now time stamp
     * @return Object the entity primary key
     * @throws UnifyException
     *             if an error occurs
     */
    Object preCreate(T entity, Date now) throws UnifyException;

    /**
     * Entity pre-update method. Called before update of entity.
     * 
     * @param entity
     *            the entity to be updated
     * @param now
     *            the now time stamp
     * @throws UnifyException
     *             if an error occurs
     */
    void preUpdate(T entity, Date now) throws UnifyException;

    /**
     * Entity pre-update method. Called before update of entity.
     * 
     * @param update
     *            the update object
     * @param now
     *            the now time stamp
     * @throws UnifyException
     *             if an error occurs
     */
    void preUpdate(Update update, Date now) throws UnifyException;

    /**
     * Entity pre-delete method. Called before delete of entity.
     * 
     * @param entity
     *            the entity to be deleted
     * @param now
     *            the now time stamp
     * @throws UnifyException
     *             if an error occurs
     */
    void preDelete(T entity, Date now) throws UnifyException;

    /**
     * Called before usage of query object.
     * 
     * @param query
     *            the query object which may be altered
     * @throws UnifyException
     *             if an error occurs
     */
    void preQuery(Query<T> query) throws UnifyException;
    
    /**
     * Entity post-create method. Called after creation of entity.
     * 
     * @param entity
     *            the created entity
     * @param now
     *            the now time stamp
     * @throws UnifyException
     *             if an error occurs
     */
    void postCreate(T entity, Date now) throws UnifyException;

    /**
     * Entity post-update method. Called after update of entity.
     * 
     * @param entity
     *            the updated entity
     * @param now
     *            the now time stamp
     * @throws UnifyException
     *             if an error occurs
     */
    void postUpdate(T entity, Date now) throws UnifyException;

    /**
     * Entity post-update method. Called after update of entity.
     * 
     * @param update
     *            the update object
     * @param now
     *            the now time stamp
     * @throws UnifyException
     *             if an error occurs
     */
    void postUpdate(Update update, Date now) throws UnifyException;

    /**
     * Entity post-delete method. Called after delete of entity.
     * 
     * @param entity
     *            the deleted entity
     * @param now
     *            the now time stamp
     * @throws UnifyException
     *             if an error occurs
     */
    void postDelete(T entity, Date now) throws UnifyException;

    /**
     * Called on creation of entity error.
     * 
     * @param entity
     *            the entity to be created
     */
    void onCreateError(T entity);

    /**
     * Called on update of entity error.
     * 
     * @param entity
     *            the entity to be updated
     */
    void onUpdateError(T entity);

    /**
     * Called on delete of entity error.
     * 
     * @param entity
     *            the entity to be deleted
     */
    void onDeleteError(T entity);

    /**
     * Indicates if now should be set in alter methods
     * 
     * @return if an error occurs
     */
    boolean isSetNow();
}
