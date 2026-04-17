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

import com.tcdng.unify.common.constants.ProcessingStatus;
import com.tcdng.unify.common.constants.WfItemVersionType;

/**
 * Work entity definition.
 * 
 * @author The Code Department
 * @since 4.1
 */
public interface WorkEntity extends AuditEntity {

	/**
	 * Gets the tenant ID
	 */
    Long getTenantId();

    /**
     * Gets the work item processing status
     */
    ProcessingStatus getProcessingStatus();

    /**
     * Sets the work item processing status
     * @param processingStatus the processing status
     */
    void setProcessingStatus(ProcessingStatus processingStatus);
     
    /**
     * Gets the workflow item version type
     */
    WfItemVersionType getWfItemVersionType();

    /**
     * Sets the workflow item version type
     * @param wfItemVersionType the version type to set
     */
    void setWfItemVersionType(WfItemVersionType wfItemVersionType);
    
    /**
     * Gets the workitem branch code
     */
    String getWorkBranchCode();

    /**
     * Sets the workitem branch code
     * @param branchCode the branch code
     */
    void setWorkBranchCode(String branchCode);
    
    /**
     * Gets the workitem department code
     */
    String getWorkDepartmentCode();

    /**
     * Sets the workitem department code
     * @param departmentCode the department code
     */
    void setWorkDepartmentCode(String departmentCode);

    /**
     * Gets the workitem description
     */
    String getWorkflowItemDesc();

    /**
     * Gets the workitem in-workflow status.
     */
    boolean isInWorkflow();

    /**
     * Sets the workitem in-workflow flag.
     * @param inWorkflow the value to set
     */
    void setInWorkflow(boolean inWorkflow);
    
    /**
     * Gets the workitem original copy ID
     */
    Long getOriginalCopyId();

    /**
     * Sets the workitem original copy.
     * @param originalCopyId the ID to set
     */
    void setOriginalCopyId(Long originalCopyId);

	/**
	 * Gets preferred workflow name for this item.
	 * 
	 * @return the preferred workflow name
	 */
	String getPreferredWorkflowName();
}
