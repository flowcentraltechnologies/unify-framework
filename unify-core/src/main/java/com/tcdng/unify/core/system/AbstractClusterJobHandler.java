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
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.system.entities.ClusterJob;
import com.tcdng.unify.core.system.entities.ClusterJobItem;
import com.tcdng.unify.core.system.entities.ClusterJobItemQuery;
import com.tcdng.unify.core.task.TaskMonitor;

/**
 * Abstract base class for cluster job handlers.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Transactional
public abstract class AbstractClusterJobHandler<T extends Entity> extends AbstractBusinessService
		implements ClusterJobHandler {

	private final Class<T> payloadEntityClass;

	public AbstractClusterJobHandler(Class<T> payloadEntityClass) {
		this.payloadEntityClass = payloadEntityClass;
	}

	@Periodic(PeriodicType.FAST)
	public final void performWork(TaskMonitor tm) throws UnifyException {
		final JobFetchInfo jobFetchInfo = getJobFetchInfo();
		Batch<T> batch = claimOldBatch(jobFetchInfo);
		if (!batch.isPresent()) {
			batch = claimNewBatch(jobFetchInfo);
		}

		if (batch.isPresent()) {
			for (T inst : batch.getItems()) {
				Update payloadUpdate = jobFetchInfo.getPassUpdate();
				JobItemStatus itemStatus = JobItemStatus.COMPLETED;
				String err = null;
				try {
					process(inst);
				} catch (Exception e) {
					payloadUpdate = jobFetchInfo.getFailUpdate();
					itemStatus = JobItemStatus.ERROR;
					err = e.getMessage();
				}

				db(payloadEntityClass).updateById(payloadEntityClass, inst.getId(), payloadUpdate);
				db().updateAll(
						new ClusterJobItemQuery().clusterJobId(batch.getClusterJobId()).payloadId((Long) inst.getId())
								.status(JobItemStatus.PENDING),
						itemStatus.isError() ? new Update().set("status", itemStatus).set("error", err)
								: new Update().set("status", itemStatus));
				commitTransactions();
			}
		}
	}

	@Override
	protected void onInitialize() throws UnifyException {

	}

	@Override
	protected void onTerminate() throws UnifyException {

	}

	protected abstract JobFetchInfo getJobFetchInfo() throws UnifyException;

	protected abstract void process(T item) throws UnifyException;

	@SuppressWarnings("unchecked")
	private Batch<T> claimNewBatch(final JobFetchInfo jobFetchInfo) throws UnifyException {
		Batch<T> result = (Batch<T>) Batch.BLANK;
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

				final List<ClusterJobItem> itemList = new ArrayList<ClusterJobItem>();
				for (Long payloadId : payloadIds) {
					ClusterJobItem item = new ClusterJobItem();
					item.setPayloadId(payloadId);
					item.setStatus(JobItemStatus.PENDING);
					itemList.add(item);
				}

				clusterJob.setItemList(itemList);

				final Long clusterJobId = (Long) db().create(clusterJob);
				result = new Batch<T>(clusterJobId, db(payloadEntityClass)
						.listAll(Query.of(payloadEntityClass).amongst("id", payloadIds).order("id")));
			}
		} finally {
			commitTransactions();
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	private Batch<T> claimOldBatch(final JobFetchInfo jobFetchInfo) throws UnifyException {
		Batch<T> result = (Batch<T>) Batch.BLANK;
		// TODO

		return result;
	}

	private static class Batch<T extends Entity> {

		@SuppressWarnings("rawtypes")
		public static final Batch<?> BLANK = new Batch();

		private final Long clusterJobId;

		private final List<T> items;

		public Batch(Long clusterJobId, List<T> items) {
			this.clusterJobId = clusterJobId;
			this.items = items;
		}

		private Batch() {
			this.clusterJobId = null;
			this.items = Collections.emptyList();
		}

		public Long getClusterJobId() {
			return clusterJobId;
		}

		public List<T> getItems() {
			return items;
		}

		public boolean isPresent() {
			return clusterJobId != null;
		}
	}
}
