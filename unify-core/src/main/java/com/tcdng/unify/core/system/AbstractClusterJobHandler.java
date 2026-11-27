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

import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Periodic;
import com.tcdng.unify.core.annotation.PeriodicType;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.business.AbstractBusinessService;
import com.tcdng.unify.core.constant.JobItemStatus;
import com.tcdng.unify.core.constant.JobStatus;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.JobFetchInfo;
import com.tcdng.unify.core.system.entities.ClusterJob;
import com.tcdng.unify.core.system.entities.ClusterJobItem;
import com.tcdng.unify.core.system.entities.ClusterJobItemQuery;
import com.tcdng.unify.core.system.entities.ClusterJobQuery;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Abstract base class for cluster job handlers.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Transactional
public abstract class AbstractClusterJobHandler extends AbstractBusinessService
		implements ClusterJobHandler {

	private final Class<? extends Entity> payloadEntityClass;

	public AbstractClusterJobHandler(Class<? extends Entity> payloadEntityClass) {
		this.payloadEntityClass = payloadEntityClass;
	}

	@Periodic(PeriodicType.FAST)
	public final void performWork(TaskMonitor tm) throws UnifyException {
		final JobFetchInfo jobFetchInfo = getJobFetchInfo();
		Batch batch = claimOldBatch(jobFetchInfo);
		if (!batch.isPresent()) {
			batch = claimNewBatch(jobFetchInfo);
		}

		if (batch.isPresent()) {
			final Long clusterJobId = batch.getClusterJobId();
			for (Long payloadId : batch.getPayloadIds()) {
				Update payloadUpdate = jobFetchInfo.getPassUpdate();
				JobItemStatus itemStatus = JobItemStatus.COMPLETED;
				String err = null;
				try {
					process(payloadId);
				} catch (Exception e) {
					payloadUpdate = jobFetchInfo.getFailUpdate();
					itemStatus = JobItemStatus.ERROR;
					err = e.getMessage();
				}

				db(payloadEntityClass).updateById(payloadEntityClass, payloadId, payloadUpdate);
				db().updateAll(
						new ClusterJobItemQuery().clusterJobId(clusterJobId).payloadId(payloadId)
								.status(JobItemStatus.PENDING),
						itemStatus.isError()
								? new Update().set("status", itemStatus).set("error",
										StringUtils.truncate(err, ClusterJobItem.MAX_ERROR_LEN))
								: new Update().set("status", itemStatus));
				commitTransactions();
			}

			db().updateById(ClusterJob.class, clusterJobId,
					new Update().set("status", JobStatus.COMPLETED).setNow("completedAt"));
		}
	}

	@Override
	protected void onInitialize() throws UnifyException {

	}

	@Override
	protected void onTerminate() throws UnifyException {

	}

	protected abstract JobFetchInfo getJobFetchInfo() throws UnifyException;

	protected abstract void process(Long payloadId) throws UnifyException;

	private Batch claimNewBatch(final JobFetchInfo jobFetchInfo) throws UnifyException {
		Batch result = Batch.BLANK;
		try {
			final List<Long> payloadIds = claimUnlockedRows(
					jobFetchInfo.getClaimQuery().limit(jobFetchInfo.getFetchBatchSize()), jobFetchInfo.getClaimUpdate(),
					"job::" + getName());
			if (!payloadIds.isEmpty()) {
				ClusterJob clusterJob = new ClusterJob();
				clusterJob.setHandler(getName());
				clusterJob.setClaimedBy(getRuntimeId());
				clusterJob.setClaimedAt(getNow());
				clusterJob.setStatus(JobStatus.RUNNING);

				final List<ClusterJobItem> jobItemList = new ArrayList<ClusterJobItem>();
				for (Long payloadId : payloadIds) {
					ClusterJobItem item = new ClusterJobItem();
					item.setPayloadId(payloadId);
					item.setStatus(JobItemStatus.PENDING);
					jobItemList.add(item);
				}

				clusterJob.setItemList(jobItemList);

				final Long clusterJobId = (Long) db().create(clusterJob);
				result = new Batch(clusterJobId, payloadIds);
			}
		} finally {
			commitTransactions();
		}

		return result;
	}

	private Batch claimOldBatch(final JobFetchInfo jobFetchInfo) throws UnifyException {
		Batch result = Batch.BLANK;
		try {
			final List<Long> clusterjobId = claimUnlockedRows(
					new ClusterJobQuery().handler(getName()).status(JobStatus.RUNNING).isnull("claimedBy").limit(1),
					new Update().set("claimedBy", getRuntimeId()).setNow("claimedAt"), "job::" + getName());
			if (!clusterjobId.isEmpty()) {
				final Long clusterJobId = clusterjobId.get(0);
				ClusterJob clusterJob = db().find(ClusterJob.class, clusterJobId);
				List<Long> payloadIds = new ArrayList<Long>();
				for (ClusterJobItem clusterJobItem : clusterJob.getItemList()) {
					if (clusterJobItem.getStatus().isPending()) {
						payloadIds.add(clusterJobItem.getPayloadId());
					}
				}

				if (!payloadIds.isEmpty()) {
					result = new Batch(clusterJobId, payloadIds);
				} else {
					db().updateById(ClusterJob.class, clusterJobId,
							new Update().set("status", JobStatus.COMPLETED).setNow("completedAt"));
				}
			}
		} finally {
			commitTransactions();
		}

		return result;
	}

	private static class Batch {

		public static final Batch BLANK = new Batch();

		private final Long clusterJobId;

		private final List<Long> payloadIds;

		public Batch(Long clusterJobId, List<Long> payloadIds) {
			this.clusterJobId = clusterJobId;
			this.payloadIds = payloadIds;
		}

		private Batch() {
			this.clusterJobId = null;
			this.payloadIds = Collections.emptyList();
		}

		public Long getClusterJobId() {
			return clusterJobId;
		}

		public List<Long> getPayloadIds() {
			return payloadIds;
		}

		public boolean isPresent() {
			return clusterJobId != null;
		}
	}
}
