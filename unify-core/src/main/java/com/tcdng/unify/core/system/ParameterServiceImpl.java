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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.UnifyCoreErrorConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.business.AbstractBusinessService;
import com.tcdng.unify.core.data.Input;
import com.tcdng.unify.core.data.Inputs;
import com.tcdng.unify.core.system.entities.ParameterDef;
import com.tcdng.unify.core.system.entities.ParameterValue;
import com.tcdng.unify.core.system.entities.ParameterValues;
import com.tcdng.unify.core.system.entities.ParameterValuesQuery;
import com.tcdng.unify.core.system.entities.ParametersDef;
import com.tcdng.unify.core.system.entities.ParametersDefQuery;
import com.tcdng.unify.core.util.AnnotationUtils;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.core.util.ReflectUtils;

/**
 * Default implementation of parameter service.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Transactional
@Component(ApplicationComponents.APPLICATION_PARAMETERSERVICE)
public class ParameterServiceImpl extends AbstractBusinessService implements ParameterService {

    @Override
    public void defineParameters(String name, Class<?> type) throws UnifyException {
        List<ParameterDef> parameterList = new ArrayList<ParameterDef>();
        for (Parameter pa : AnnotationUtils.getParameters(type)) {
            String editor = AnnotationUtils.getAnnotationString(pa.editor());
            if (editor != null) {
                ParameterDef parameterDef = new ParameterDef();
                parameterDef.setDescription(pa.description());
                parameterDef.setEditor(pa.editor());
                parameterDef.setOrder(pa.order());
                parameterDef.setMandatory(pa.mandatory());
                parameterDef.setName(pa.name());
                parameterDef.setType(pa.type().getName());
                parameterList.add(parameterDef);
            }
        }

		DataUtils.sortAscending(parameterList, ParameterDef.class, "order");
        defineParameters(name, parameterList);
    }

    @Override
    public void defineParameters(String name, List<ParameterDef> parameterList) throws UnifyException {
        ParametersDef pdd = db().find(new ParametersDefQuery().typeName(name));
        if (pdd == null) {
            pdd = new ParametersDef();
            pdd.setTypeName(name);
            pdd.setParameterDefs(parameterList);
            db().create(pdd);
        } else {
            pdd.setParameterDefs(parameterList);
            db().updateByIdVersion(pdd);
        }
    }

    @Override
    public Map<String, ParameterDef> findParameterDefinitionsByName(String name) throws UnifyException {
        ParametersDef pdd = db().find(new ParametersDefQuery().typeName(name));
        if (pdd != null && !pdd.isEmpty()) {
            Map<String, ParameterDef> map = new LinkedHashMap<String, ParameterDef>();
            for (ParameterDef pd : pdd.getParameterDefs()) {
                map.put(pd.getName(), pd);
            }
            return map;
        }

        return Collections.emptyMap();
    }

    @Override
    public List<Input<?>> fetchInputList(String name) throws UnifyException {
        ParametersDef parametersDef = db().find(new ParametersDefQuery().typeName(name));
        if (parametersDef != null) {
            List<Input<?>> inputList = new ArrayList<Input<?>>();
            for (ParameterDef pdd : parametersDef.getParameterDefs()) {
                inputList.add(getInput(pdd));
            }

            return inputList;
        }

        return Collections.emptyList();
    }

    @Override
    public Inputs fetchNormalizedInputs(String paramTypeName, String instTypeName, Long instId) throws UnifyException {
        List<Input<?>> inputList = null;
        if (QueryUtils.isValidStringCriteria(paramTypeName)) {
            inputList = new ArrayList<Input<?>>();
            Map<String, ParameterDef> parameterDefMap = findParameterDefinitionsByName(paramTypeName);
            Set<String> usedSet = new HashSet<String>();
            ParameterValues parameterValues = db()
                    .list(new ParameterValuesQuery().typeName(paramTypeName).instTypeName(instTypeName).instId(instId));
            if (parameterValues != null) {
                for (ParameterValue parameterValue : parameterValues.getParameterValues()) {
                    String key = parameterValue.getParamKey();
                    ParameterDef parameterDef = parameterDefMap.get(key);
                    if (parameterDef != null) {
                        usedSet.add(key);
                        Input<?> input = getInput(parameterDef);
                        input.setStringValue(parameterValue.getParamValue());
                        inputList.add(input);
                    }
                }
            }

            if (usedSet.size() < parameterDefMap.size()) {
                for (Map.Entry<String, ParameterDef> entry : parameterDefMap.entrySet()) {
                    if (!usedSet.contains(entry.getKey())) {
                        ParameterDef parameterDef = entry.getValue();
                        inputList.add(getInput(parameterDef));
                    }
                }
            }
        } else {
            inputList = Collections.emptyList();
        }

        return new Inputs(inputList);
    }

    @Override
    public Map<String, Object> findParameterTypeValues(String paramTypeName, String instTypeName, Long instId)
            throws UnifyException {
        ParameterValues parameterValuesData =
                db().list(new ParameterValuesQuery().typeName(paramTypeName).instTypeName(instTypeName).instId(instId));
        if (parameterValuesData != null) {
            Map<String, Object> result = new HashMap<String, Object>();
            Map<String, ParameterDef> parameterDefMap = findParameterDefinitionsByName(paramTypeName);
            for (ParameterValue parameterValueData : parameterValuesData.getParameterValues()) {
                ParameterDef parameterDefData = parameterDefMap.get(parameterValueData.getParamKey());
                if (parameterDefData != null) {
                    Class<?> type = ReflectUtils.classForName(parameterDefData.getType());
                    result.put(parameterDefData.getName(),
                            DataUtils.convert(type, parameterValueData.getParamValue()));
                }
            }

            return result;
        }

        return Collections.emptyMap();
    }

    @Override
    public void updateParameterValues(String paramTypeName, String instTypeName, Long instId, Inputs inputs)
            throws UnifyException {
        ParametersDef pdd = db().find(new ParametersDefQuery().typeName(paramTypeName));
        if (pdd == null) {
            throw new UnifyException(UnifyCoreErrorConstants.PARAMETER_DEFINITION_UNKNOWN, paramTypeName);
        }

        ParameterValues parameterValues =
                db().list(new ParameterValuesQuery().typeName(paramTypeName).instTypeName(instTypeName).instId(instId));
        Long parameterValuesId = null;
        if (parameterValues == null) {
            parameterValues = new ParameterValues();
            parameterValues.setParametersDefId(pdd.getId());
            parameterValues.setInstTypeName(instTypeName);
            parameterValues.setInstId(instId);
            parameterValuesId = (Long) db().create(parameterValues);
        } else {
            parameterValuesId = parameterValues.getId();
        }

        List<ParameterValue> parameterValueList = new ArrayList<ParameterValue>();
        for (ParameterDef pddc : pdd.getParameterDefs()) {
            Input<?> input = inputs.getInput(pddc.getName());
            String value = null;
            if (input != null) {
                value = input.getStringValue();
            }

            if (pddc.isMandatory() && !QueryUtils.isValidStringCriteria(value)) {
                throw new UnifyException(UnifyCoreErrorConstants.PARAMETER_VALUE_REQUIRED, pddc.getName(),
                        paramTypeName, instTypeName);
            }

            ParameterValue parameterValue = new ParameterValue();
            parameterValue.setParameterValuesId(parameterValuesId);
            parameterValue.setParamKey(pddc.getName());
            parameterValue.setParamValue(value);
            parameterValueList.add(parameterValue);
        }

        parameterValues.setParameterValues(parameterValueList);
        db().updateByIdVersion(parameterValues);
    }

    @Override
    public void deleteParameterValues(String paramTypeName, String instTypeName, Long instId) throws UnifyException {
        db().deleteAll(new ParameterValuesQuery().typeName(paramTypeName).instTypeName(instTypeName).instId(instId));
    }

    private Input<?> getInput(ParameterDef parameterDef) throws UnifyException {
        Class<?> type = ReflectUtils.classForName(parameterDef.getType());
        return DataUtils.newInput(type, parameterDef.getName(), resolveSessionMessage(parameterDef.getDescription()),
                parameterDef.getEditor(), parameterDef.isMandatory());
    }

}
