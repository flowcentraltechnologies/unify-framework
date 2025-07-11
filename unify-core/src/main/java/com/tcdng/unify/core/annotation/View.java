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

package com.tcdng.unify.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tcdng.unify.common.annotation.AnnotationConstants;
import com.tcdng.unify.core.ApplicationComponents;

/**
 * Annotation for defining a managed view.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface View {

    /**
     * The application data source that view belongs to. Defaults to
     * {@link ApplicationComponents#APPLICATION_DATASOURCE}
     */
    String datasource() default ApplicationComponents.APPLICATION_DATASOURCE;

    /** Optional name of schema that view belongs to. */
    String schema() default AnnotationConstants.NONE;

    /** The name of the view. */
    String name() default AnnotationConstants.NONE;

    /** The primary base table alias */
    String primaryAlias();

    /** Base table references that form the view */
    TableRef[] tables();

    /** View restrictions */
    ViewRestriction[] restrictions() default {};
}
