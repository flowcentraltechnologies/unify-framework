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

package com.tcdng.unify.core.criterion;

/**
 * Double parameter restriction object.
 * 
 * @author The Code Department
 * @since 4.1
 */
public interface DoubleParamRestriction extends SimpleRestriction {

    /**
     * Gets the first parameter for this restriction.
     * 
     * @return the first parameter
     */
    Object getFirstParam();

    /**
     * Gets the second parameter for this restriction.
     * 
     * @return the second parameter
     */
    Object getSecondParam();

    /**
     * Sets the restriction parameters.
     * 
     * @param firstParam
     *            the first parameter to set
     * @param secondParam
     *            the second parameter to set
     */
    void setParams(Object firstParam, Object secondParam);
}
