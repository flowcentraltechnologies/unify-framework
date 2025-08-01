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
package com.tcdng.unify.core.filter.policy;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.filter.AbstractCollectionSizeObjectFilterPolicy;

/**
 * Collection size less than or equal policy.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class LessOrEqualCollectionPolicy extends AbstractCollectionSizeObjectFilterPolicy {

    public LessOrEqualCollectionPolicy() {
        super(false);
    }

    @Override
    protected boolean doMatch(int collSize, int param) throws UnifyException {
        return collSize <= param;
    }

}
