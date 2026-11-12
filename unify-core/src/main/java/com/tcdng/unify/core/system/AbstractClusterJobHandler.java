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
package com.tcdng.unify.core.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.business.AbstractBusinessService;
import com.tcdng.unify.core.data.Job;
import com.tcdng.unify.core.data.JobFetchInfo;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Abstract base class for cluster job handlers.
 * 
 * @author The Code Department
 * @since 4.1
 */
public abstract class AbstractClusterJobHandler extends AbstractBusinessService implements ClusterJobHandler {

	@Override
	public List<Job> getNewJobs() throws UnifyException {
		final JobFetchInfo jobFetchInfo = getJobFetchInfo();
		final List<Long> workItemIds = claimUnlockedRows(
				jobFetchInfo.getClaimQuery().limit(jobFetchInfo.getFetchBatchSize()), jobFetchInfo.getClaimUpdate(),
				"jobhdl::" + getName());
		if (!DataUtils.isBlank(workItemIds)) {
			final List<Job> jobs = new ArrayList<Job>();
			for (Long workItemId : workItemIds) {
				jobs.add(new Job(workItemId, jobFetchInfo.getTitle(), jobFetchInfo.getJobMaxAttempts()));
			}

			return jobs;
		}

		return Collections.emptyList();
	}

	@Override
	protected void onInitialize() throws UnifyException {

	}

	@Override
	protected void onTerminate() throws UnifyException {

	}

	protected abstract JobFetchInfo getJobFetchInfo() throws UnifyException;
	
}
