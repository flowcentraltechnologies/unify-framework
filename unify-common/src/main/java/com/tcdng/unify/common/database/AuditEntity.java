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
package com.tcdng.unify.common.database;

import java.util.Date;

/**
 * Audit entity definition.
 * 
 * @author The Code Department
 * @since 4.1
 */
public interface AuditEntity extends VersionEntity {

	/**
	 * Gets create date (timestamp)
	 * 
	 * @return the create date
	 */
	Date getCreateDt();

	/**
	 * Sets the create date.
	 * 
	 * @param createDt the create date to set.
	 */
	void setCreateDt(Date createDt);

	/**
	 * Gets created by.
	 * 
	 * @return the created by
	 */
	String getCreatedBy();

	/**
	 * Sets created by.
	 * 
	 * @param createdBy the create by to set
	 */
	void setCreatedBy(String createdBy);

	/**
	 * Gets update date (timestamp)
	 * 
	 * @return the update date
	 */
	Date getUpdateDt();

	/**
	 * Sets the update date.
	 * 
	 * @param updateDt the update date to set.
	 */
	void setUpdateDt(Date updateDt);

	/**
	 * Gets updated by.
	 * 
	 * @return the updated by
	 */
	String getUpdatedBy();

	/**
	 * Sets updated by.
	 * 
	 * @param updatedBy the updated by to set
	 */
	void setUpdatedBy(String updatedBy);

}
