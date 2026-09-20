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
package com.tcdng.unify.core;

import java.util.Locale;

import com.tcdng.unify.core.data.Context;
import com.tcdng.unify.core.data.UnifyClientRequest;

/**
 * Request context class.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class RequestContext extends Context {

	private UnifyClientRequest clientRequest;

    private SessionContext sessionContext;

    private Object quickReference;

    private String requestPath;

    private String requestTarget;

    private String pid;
    
    public RequestContext(String requestPath, String requestTarget, SessionContext sessionContext) {
        this.requestPath = requestPath;
        this.requestTarget = requestTarget;
        this.sessionContext = sessionContext;
    }
    
    public void removeAllAttributes() {
    	clear();
    }
    
    public String getContextPath() {
        return sessionContext.getContextPath();
    }

    public String getTenantPath() {
        return sessionContext.getTenantPath();
    }

    public boolean isWithTenantPath() {
        return sessionContext.isWithTenantPath();
    }

    public String getRequestPath() {
        return requestPath;
    }

    public String getRequestTarget() {
		return requestTarget;
	}

	public SessionContext getSessionContext() {
        return sessionContext;
    }

    public Locale getLocale() {
        return sessionContext.getLocale();
    }

    public Object getQuickReference() {
        return quickReference;
    }

    public void setQuickReference(Object quickReference) {
        this.quickReference = quickReference;
    }

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public UnifyClientRequest getClientRequest() {
		return clientRequest;
	}

	public void setClientRequest(UnifyClientRequest clientRequest) {
		this.clientRequest = clientRequest;
	}

	public void swapSession() {
		if (clientRequest != null) {
			clientRequest.swapSession();
		}
	}
	
	public void invalidateSession() {
		if (sessionContext != null) {
			sessionContext.invalidate();
		}

		if (clientRequest != null) {
			clientRequest.invalidateSession();
		}
	}

}
