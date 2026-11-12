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
package com.tcdng.unify.core.criterion;

import java.util.Map;

import com.tcdng.unify.core.data.FluentMap;
import com.tcdng.unify.core.expression.AdditionExpression;
import com.tcdng.unify.core.expression.CustomUpdateExpression;
import com.tcdng.unify.core.expression.DivisionExpression;
import com.tcdng.unify.core.expression.MultiplicationExpression;
import com.tcdng.unify.core.expression.SubtractionExpression;

/**
 * Used to specify fields to update with values.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class Update extends FluentMap<String, Object> {

	public Update() {

	}

	public Update(Update update) {
		super(update);
	}

	@Override
	public Update add(String field, Object value) {
		return (Update) super.add(field, value);
	}

	@Override
	public Update addAll(Map<? extends String, ? extends Object> map) {
		return (Update) super.addAll(map);
	}

	@Override
	public Update remove(String key) {
		return (Update) super.remove(key);
	}

	@Override
	public Update clear() {
		return (Update) super.clear();
	}
	
	public Update addSetTo(String field, Object value) {
		return (Update) super.add(field, value);
	}

	public Update addSetToSelfAddition(String lfield, Number value) {
		return (Update) super.add(lfield, new AdditionExpression(value));
	}

	public Update addSetToSelfSubtraction(String lfield, Number value) {
		return (Update) super.add(lfield, new SubtractionExpression(value));
	}

	public Update addSetToSelfMultiplication(String lfield, Number value) {
		return (Update) super.add(lfield, new MultiplicationExpression(value));
	}

	public Update addSetToSelfDivision(String lfield, Number value) {
		return (Update) super.add(lfield, new DivisionExpression(value));
	}

	public Update addSetToCustom(String lfield, String expression) {
		return (Update) super.add(lfield, new CustomUpdateExpression(expression));
	}

	public Update addSetToOther(String lfield, String rfield) {
		return (Update) super.add(lfield, new CustomUpdateExpression("{{" + rfield + "}}"));
	}

	public Update addSetToNow(String lfield) {
		return (Update) super.add(lfield, new CustomUpdateExpression("{{g:now}}"));
	}

	public Update addSetToNowWithOffset(String lfield, int seconds) {
		return (Update) super.add(lfield, new CustomUpdateExpression(
				seconds >= 0 ? "{{g:nowaddsec_" + seconds + "}}" : "{{g:nowsubsec_" + -seconds + "}}"));
	}

	public Update addSetToSelfWithOffset(String lfield, int seconds) {
		return (Update) super.add(lfield, new CustomUpdateExpression(
				seconds >= 0 ? "{{g:fldaddsec_" + seconds + "}}" : "{{g:fldsubsec_" + -seconds + "}}"));
	}

	public Update addSetToOtherWithOffset(String lfield, String rfield, int seconds) {
		return (Update) super.add(lfield, new CustomUpdateExpression(
				seconds >= 0 ? "{{g:fldaddsec_" + seconds + "_" + rfield + "}}"
						: "{{g:fldsubsec_" + -seconds + "_" + rfield + "}}"));
	}
}
