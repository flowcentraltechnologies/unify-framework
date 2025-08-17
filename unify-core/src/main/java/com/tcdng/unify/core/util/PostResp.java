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
package com.tcdng.unify.core.util;

/**
 * Post response object.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class PostResp<T> {

	private T result;

	private String error;

	private String reqJson;

	private String respJson;

	private int status;

	private long execMilliSec;

	public PostResp(T result, String error, String reqJson, String respJson, int status, long execMilliSec) {
		this.result = result;
		this.error = error;
		this.reqJson = reqJson;
		this.respJson = respJson;
		this.status = status;
		this.execMilliSec = execMilliSec;
	}

	public PostResp(T result, PostResp<?> origPostResp) {
		this.result = result;
		this.error = origPostResp.error;
		this.reqJson = origPostResp.reqJson;
		this.respJson = origPostResp.respJson;
		this.status = origPostResp.status;
		this.execMilliSec = origPostResp.execMilliSec;
	}

	public T getResult() {
		return result;
	}

	public String getError() {
		return error;
	}

	public String getReqJson() {
		return reqJson;
	}

	public String getRespJson() {
		return respJson;
	}

	public int getStatus() {
		return status;
	}

	public long getExecMilliSec() {
		return execMilliSec;
	}

	public boolean isSuccess() {
		return status >= 200 && status < 300;
	}

	public boolean isError() {
		return !isSuccess();
	}
}
