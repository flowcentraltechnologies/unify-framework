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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.UnifyContainerInterface;
import com.tcdng.unify.core.UnifyCoreRequestAttributeConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Periodic;
import com.tcdng.unify.core.annotation.PeriodicType;
import com.tcdng.unify.core.annotation.TransactionAttribute;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.business.AbstractBusinessService;
import com.tcdng.unify.core.business.AbstractQueuedExec;
import com.tcdng.unify.core.business.QueuedExec;
import com.tcdng.unify.core.constant.JobStatus;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.Less;
import com.tcdng.unify.core.criterion.Or;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.Job;
import com.tcdng.unify.core.data.JobExecInfo;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.system.entities.ClusterCommand;
import com.tcdng.unify.core.system.entities.ClusterCommandParam;
import com.tcdng.unify.core.system.entities.ClusterCommandParamQuery;
import com.tcdng.unify.core.system.entities.ClusterCommandQuery;
import com.tcdng.unify.core.system.entities.ClusterJob;
import com.tcdng.unify.core.system.entities.ClusterJobQuery;
import com.tcdng.unify.core.system.entities.ClusterNode;
import com.tcdng.unify.core.system.entities.ClusterNodeQuery;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.NetworkUtils;

/**
 * Default implementation of application cluster manager. Uses datasource
 * timestamp to coordidate synchronization of method calls across clusters.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Transactional
@Component(ApplicationComponents.APPLICATION_CLUSTERSERVICE)
public class ClusterServiceImpl extends AbstractBusinessService implements ClusterService {

	private static final String CLUSTER_NODE_HOUSEKEEP_LOCK = "lock:clusternodehousekeep";

	private static final String CLUSTER_NEW_JOB_LOCK = "lock:clustenewjobs";

	private static final String CLUSTER_PROCESS_JOB_LOCK = "lock:clusteprocessjobs";

	private static final String CLUSTER_PROCESS_DEAD_LOCK = "lock:clusteprocessdead";
	
	private static final int JOB_PROCESSING_BATCH_SIZE = 64;
	
	private static final int MAX_JOB_PROCESSING_THREADS = 16;

    private final QueuedExec<JobExecInfo> clusterJobQueuedExec;
	
	@Configurable("5")
	private int jobExpirationMinutes;
	
	@Configurable("1")
	private int nodeExpirationMinutes;

	private List<ClusterJobHandler> jobHandlers;
	
	private List<Long> runningClusterJobIds;
	
	public ClusterServiceImpl() {
		this.runningClusterJobIds = new CopyOnWriteArrayList<Long>();
        this.clusterJobQueuedExec = new AbstractQueuedExec<JobExecInfo>(MAX_JOB_PROCESSING_THREADS)
            {
                @Override
                protected void doExecute(JobExecInfo jobExecInfo) {
                	runningClusterJobIds.add(jobExecInfo.getClusterJobId());
                	
                    try {
                        performWork(jobExecInfo);
                    } catch (Exception e) {
                        logError(e);
                        
                        try {
							failWork(jobExecInfo, e.getMessage());
						} catch (Exception e1) {
						}
                    } finally {
                    	runningClusterJobIds.remove(jobExecInfo.getClusterJobId());
                    }
                }

            };
	}
	
	@Override
	public List<ClusterNode> findClusterNodes(ClusterNodeQuery query) throws UnifyException {
		return db().findAll(query);
	}

	@Override
	public void broadcastToOtherNodes(String command, String... params) throws UnifyException {
		if (isClusterMode()
				&& !Boolean.TRUE.equals(getRequestAttribute(UnifyCoreRequestAttributeConstants.SUPPRESS_BROADCAST))) {
			List<String> nodeIdList = db().valueList(String.class, "nodeId",
					new ClusterNodeQuery().nodeNotEqual(getRuntimeId()));
			if (!nodeIdList.isEmpty()) {
				ClusterCommand clusterCommandData = new ClusterCommand();
				clusterCommandData.setCommandCode(command);

				ClusterCommandParam clusterCommandParamData = null;
				boolean isParams = params.length > 0;
				if (isParams) {
					clusterCommandParamData = new ClusterCommandParam();
				}

				for (String nodeId : nodeIdList) {
					clusterCommandData.setNodeId(nodeId);
					Long clusterCommandId = (Long) db().create(clusterCommandData);
					if (isParams) {
						clusterCommandParamData.setClusterCommandId(clusterCommandId);
						for (String param : params) {
							clusterCommandParamData.setParameter(param);
							db().create(clusterCommandParamData);
						}
					}
				}
			}
		}
	}

	@Override
	public List<Command> getClusterCommands() throws UnifyException {
		List<Command> resultList = Collections.emptyList();
		List<ClusterCommand> clusterCommandList = db().findAll(new ClusterCommandQuery().nodeId(getRuntimeId()));

		if (!clusterCommandList.isEmpty()) {
			List<Long> clusterCommandIdList = new ArrayList<Long>();
			resultList = new ArrayList<Command>();
			for (ClusterCommand clusterCommand : clusterCommandList) {
				Long clusterCommandId = clusterCommand.getId();
				clusterCommandIdList.add(clusterCommandId);
				resultList.add(new Command(clusterCommand.getCommandCode(), db().valueList(String.class, "parameter",
						new ClusterCommandParamQuery().clusterCommandId(clusterCommandId))));
			}

			db().deleteAll(new ClusterCommandParamQuery().clusterCommandIdIn(clusterCommandIdList));
			db().deleteAll(new ClusterCommandQuery().idIn(clusterCommandIdList));
		}

		return resultList;
	}

	@Periodic(PeriodicType.SLOWER)
	public void performNodeHouseKeeping(TaskMonitor taskMonitor) throws UnifyException {
		if (tryGrabLock(CLUSTER_NODE_HOUSEKEEP_LOCK)) {
			try {
				List<String> deadNodeIds = db().valueList(String.class, "nodeId", new ClusterNodeQuery()
						.lastHeartBeatOlderThan(getNewNodeExpiryDate()).nodeNotEqual(getRuntimeId()));
				if (!deadNodeIds.isEmpty()) {
					db().deleteAll(new ClusterCommandParamQuery().nodeIdIn(deadNodeIds));
					db().deleteAll(new ClusterCommandQuery().nodeIdIn(deadNodeIds));
					db().deleteAll(new ClusterNodeQuery().nodeIdIn(deadNodeIds));
				}
			} finally {
				releaseLock(CLUSTER_NODE_HOUSEKEEP_LOCK);
			}
		}
	}

	@Periodic(PeriodicType.NORMAL)
	public void loadNewJobs(TaskMonitor taskMonitor) throws UnifyException {
		if (tryGrabLock(CLUSTER_NEW_JOB_LOCK)) {
			try {
				if (jobHandlers == null) {
					jobHandlers = getComponents(ClusterJobHandler.class);
				}

				for (ClusterJobHandler handler: jobHandlers) {
					loadNewJob(handler);
				}
			} finally {
				releaseLock(CLUSTER_NEW_JOB_LOCK);
			}
		}
	}

	@Periodic(PeriodicType.SLOW)
	public void performHeartBeat(TaskMonitor taskMonitor) throws UnifyException {
		final String runtimeId = getRuntimeId();
		// Node
		if (db().updateAll(new ClusterNodeQuery().nodeId(runtimeId),
				new Update().setNow("lastHeartBeat")) == 0) {
			// Register node
			ClusterNode clusterNode = new ClusterNode();
			clusterNode.setNodeId(runtimeId);
			clusterNode.setLastHeartBeat(db().getNow());
			clusterNode.setIpAddress(NetworkUtils.getLocalHostIpAddress());
			UnifyContainerInterface unifyContainerInterface = (UnifyContainerInterface) this
					.getComponent("unify-commandinterface");
			clusterNode.setCommandPort(Integer.valueOf(unifyContainerInterface.getPort()));
			db().create(clusterNode);
		}
		
		commitTransactions();
		
		//Long jobs
		List<Long> running = new ArrayList<Long>(runningClusterJobIds);
		if (!running.isEmpty()) {
			db().updateAll(
					new ClusterJobQuery()
						.amongst("id", running)
						.equals("lockedBy", getRuntimeId())
						.equals("status", JobStatus.RUNNING),
					new Update()
						.setNow("lockedAt"));
		}
		
		commitTransactions();

		// Dead jobs
		db().updateAll(
				new ClusterJobQuery()
					.equals("status", JobStatus.RUNNING)
					.greatereqf("attempts", "maxAttempts")
					.less("lockedAt", Query.expOfNowWithOffset(-jobExpirationMinutes * 60)),
				new Update()
					.set("status", JobStatus.DEAD));
	}

	@Periodic(PeriodicType.SLOW)
	public void performNotifyDead(TaskMonitor taskMonitor) throws UnifyException {
		final List<Long> deadJobIds = claimUnlockedRows(
				new ClusterJobQuery().equals("status", JobStatus.DEAD).limit(JOB_PROCESSING_BATCH_SIZE),
				new Update().set("status", JobStatus.DEAD_ACK), CLUSTER_PROCESS_DEAD_LOCK);
		if (!DataUtils.isBlank(deadJobIds)) {
			for (ClusterJob deadClusterJob : db().findAll(new ClusterJobQuery().idIn(deadJobIds))) {
				ClusterJobHandler handler = getComponent(ClusterJobHandler.class, deadClusterJob.getHandler());
				handler.notifyFail(deadClusterJob.getPayloadId());
			}
		}
	}

	@Transactional(TransactionAttribute.REQUIRES_NEW)
	public void loadNewJob(ClusterJobHandler handler) throws UnifyException {
		try {
			final List<Job> newjobs = handler.claimNewWorkItems();
			if (!DataUtils.isBlank(newjobs)) {
				final String handlerName = handler.getName();
				for (Job job : newjobs) {
					ClusterJob clusterJob = new ClusterJob();
					clusterJob.setHandler(handlerName);
					clusterJob.setTitle(job.getTitle());
					clusterJob.setPayloadId(job.getWorkItemId());
					clusterJob.setMaxAttempts(job.getMaxAttempts());
					clusterJob.setAttempts(0);
					clusterJob.setStatus(JobStatus.PENDING);
					db().create(clusterJob);
				}
			}
		} catch (Exception e) {
			logSevere(e);
		}
	}

	@Periodic(PeriodicType.FAST)
	public void processJobs(TaskMonitor taskMonitor) throws UnifyException {
		final List<Long> clusterJobIds = claimUnlockedRows(
				new ClusterJobQuery()
					.lessf("attempts", "maxAttempts")
					.restriction(new Or()
								.add(new Equals("status", JobStatus.PENDING))
								.add(new And()
										.add(new Equals("status", JobStatus.RUNNING))
										.add(new Less("lockedAt", Query.expOfNowWithOffset(-jobExpirationMinutes * 60)))))
						.limit(JOB_PROCESSING_BATCH_SIZE),
				new Update()
					.set("status", JobStatus.RUNNING)
					.setNow("lockedAt")
					.set("lockedBy", getRuntimeId())
					.setAddition("attempts", 1),
				CLUSTER_PROCESS_JOB_LOCK);
		if (!DataUtils.isBlank(clusterJobIds)) {
			for (ClusterJob clusterJob : db().findAll(new ClusterJobQuery().idIn(clusterJobIds))) {
				clusterJobQueuedExec
						.execute(new JobExecInfo(clusterJob.getHandler(), clusterJob.getPayloadId(), clusterJob.getId()));
			}
		}
	}
	
	public void performWork(JobExecInfo jobExecInfo) throws UnifyException {
		try {
			final ClusterJobHandler handler = getComponent(ClusterJobHandler.class, jobExecInfo.getHandler());
			handler.performWork(jobExecInfo.getPayloadId());
			db().updateAll(new ClusterJobQuery()
						.equals("id", jobExecInfo.getClusterJobId())
						.equals("lockedBy", getRuntimeId())
						.equals("status", JobStatus.RUNNING)
					, new Update()
						.set("status", JobStatus.COMPLETED)
						.setNull("lockedAt"));			
		} catch (UnifyException e) {
			throw e;
		} catch (Exception e) {
			throwOperationErrorException(e);
		}
	}

	public void failWork(JobExecInfo jobExecInfo, String message) throws UnifyException{
		db().updateAll(new ClusterJobQuery()
				.equals("id", jobExecInfo.getClusterJobId())
				.equals("lockedBy", getRuntimeId())
				.equals("status", JobStatus.RUNNING)
			, new Update()
				.set("status", JobStatus.ERROR)
				.set("error", message)
				.setNull("lockedAt"));			
	}
	
	@Override
	protected void onInitialize() throws UnifyException {

	}

	@Override
	protected void onTerminate() throws UnifyException {

	}
	
	private Date getNewNodeExpiryDate() throws UnifyException {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(db().getNow().getTime() - (nodeExpirationMinutes * 60000));
		return cal.getTime();
	}
}
