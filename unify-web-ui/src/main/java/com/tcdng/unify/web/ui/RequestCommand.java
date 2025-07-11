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
package com.tcdng.unify.web.ui;

import com.tcdng.unify.core.util.StringUtils;

/**
 * Request command data object.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class RequestCommand {

    private DataTransferBlock transferBlock;

    private String parentLongName;
    
    private String command;

    public RequestCommand(DataTransferBlock transferBlock, String parentLongName, String command) {
        this.transferBlock = transferBlock;
        this.parentLongName = parentLongName;
        this.command = command;
    }

    public DataTransferBlock getTransferBlock() {
        return transferBlock;
    }

    public String getParentLongName() {
        return parentLongName;
    }

    public String getCommand() {
        return command;
    }

	@Override
	public String toString() {
		return StringUtils.toXmlString(this);
	}
}
