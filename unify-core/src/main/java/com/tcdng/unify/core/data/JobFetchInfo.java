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
package com.tcdng.unify.core.data;

import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.database.Query;

/**
 * Job fetch information.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class JobFetchInfo {

	private Query<?> claimQuery;

	private Update claimUpdate;

	private Update passUpdate;

	private Update failUpdate;

	private int fetchBatchSize;

	private JobFetchInfo(Query<?> claimQuery, Update claimUpdate, Update passUpdate, Update failUpdate,
			int fetchBatchSize) {
		this.claimQuery = claimQuery;
		this.claimUpdate = claimUpdate;
		this.passUpdate = passUpdate;
		this.failUpdate = failUpdate;
		this.fetchBatchSize = fetchBatchSize;
	}

	public Query<?> getClaimQuery() {
		return claimQuery;
	}

	public Update getClaimUpdate() {
		return claimUpdate;
	}

	public Update getPassUpdate() {
		return passUpdate;
	}

	public Update getFailUpdate() {
		return failUpdate;
	}

	public int getFetchBatchSize() {
		return fetchBatchSize;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {

		private Query<?> claimQuery;

		private Update claimUpdate;

		private Update passUpdate;

		private Update failUpdate;

		private int fetchBatchSize;

		public Builder() {
			this.fetchBatchSize = 20;
		}

		public Builder claimQuery(Query<?> claimQuery) {
			this.claimQuery = claimQuery;
			return this;
		}

		public Builder claimUpdate(Update claimUpdate) {
			this.claimUpdate = claimUpdate;
			return this;
		}

		public Builder passUpdate(Update passUpdate) {
			this.passUpdate = passUpdate;
			return this;
		}

		public Builder failUpdate(Update failUpdate) {
			this.failUpdate = failUpdate;
			return this;
		}

		public Builder fetchBatchSize(int fetchBatchSize) {
			this.fetchBatchSize = fetchBatchSize;
			return this;
		}

		public JobFetchInfo build() {
			if (claimQuery == null) {
				throw new IllegalArgumentException("Claim query is required.");
			}

			if (claimUpdate == null) {
				throw new IllegalArgumentException("Claim update is required.");
			}

			if (passUpdate == null) {
				throw new IllegalArgumentException("Pass update is required.");
			}

			if (failUpdate == null) {
				throw new IllegalArgumentException("Fail update is required.");
			}

			return new JobFetchInfo(claimQuery, claimUpdate, passUpdate, failUpdate, fetchBatchSize);
		}
	}
}