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
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.tcdng.unify.common.annotation.AnnotationConstants;

/**
 * Annotation for declaring a component property as configurable. Allows setting
 * default configuration value. The component factory uses this annotation to
 * know which component properties to inject with configuration values at
 * runtime.
 * 
 * @author The Code Department
 * @since 4.1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Configurable {

    /** The default configuration value */
    String value() default AnnotationConstants.NONE;

    /**
     * The default configuration values. Use when multiple values need configured
     * for a property
     */
    String[] values() default {};

    /**
     * The configurable property name. Effective only in {@link Configuration}
     * annotation.
     */
    String property() default AnnotationConstants.NONE;

    /**
     * Indicates that configurable property is hidden.
     */
    boolean hidden() default false;

    /**
     * Indicates annotated component type is automatically resolved and injected if
     * no default is specified and there's no explicit configuration.
     */
    boolean resolve() default true;
}
