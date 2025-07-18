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
package com.tcdng.unify.core.system;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.task.AbstractTask;
import com.tcdng.unify.core.task.TaskInput;
import com.tcdng.unify.core.task.TaskMonitor;

/**
 * Sequence block test task.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Component("clustersequenceblock-test")
public class SequenceBlockTestTask extends AbstractTask {

    @Configurable
    private SequenceNumberService sequenceNumberService;

    @Override
    public void execute(TaskMonitor taskMonitor, TaskInput input) throws UnifyException {
        while (sequenceNumberService == null) {
            Thread.yield();
        }

        String sequenceId = input.getParam(String.class, SequenceTestTaskConstants.SEQUENCEID);
        int seqCount = input.getParam(int.class, SequenceTestTaskConstants.SEQUENCECOUNT);
        for (int i = 0; i < seqCount; i++) {
            sequenceNumberService.getCachedBlockNextSequenceNumber(sequenceId);
            if (seqCount % 11 == 0) {
                Thread.yield();
            }
        }
    }

}
