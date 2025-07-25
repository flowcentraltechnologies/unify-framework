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

package com.tcdng.unify.core.list;

import java.util.List;

import com.tcdng.unify.common.data.Listable;

/**
 * List listable value parameter.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class ListableListParam extends AbstractListParam {

    private List<? extends Listable> listableList;

    public ListableListParam(List<? extends Listable> listableList) {
        this.listableList = listableList;
    }

    public List<? extends Listable> getListableList() {
        return listableList;
    }

    @Override
    public boolean isPresent() {
        return listableList != null && !listableList.isEmpty();
    }
}
