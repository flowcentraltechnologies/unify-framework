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

	private static final CustomUpdateExpression NOW_EXPRESSION = new CustomUpdateExpression("{{g:now}}");

	private static final CustomUpdateExpression TODAY_EXPRESSION = new CustomUpdateExpression("{{g:today}}");

	public Update() {

	}

	public Update(Update update) {
		super(update);
	}

	@Override
	public Update add(String field, Object val) {
		return (Update) super.add(field, val);
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

	public Update set(String field, Object val) {
		return (Update) super.add(field, val);
	}

	public Update setNull(String field) {
		return (Update) super.add(field, null);
	}

	public Update setAddition(String lfield, Number val) {
		return (Update) super.add(lfield, new AdditionExpression(val));
	}

	public Update setAddition(String lfield, String rfield, Number val) {
		return (Update) super.add(lfield, new CustomUpdateExpression("{{" + rfield + "}} + " + val));
	}

	public Update setSubtraction(String lfield, Number val) {
		return (Update) super.add(lfield, new SubtractionExpression(val));
	}

	public Update setSubtraction(String lfield, String rfield, Number val) {
		return (Update) super.add(lfield, new CustomUpdateExpression("{{" + rfield + "}} - " + val));
	}

	public Update setMultiplication(String lfield, Number val) {
		return (Update) super.add(lfield, new MultiplicationExpression(val));
	}

	public Update setMultiplication(String lfield, String rfield, Number val) {
		return (Update) super.add(lfield, new CustomUpdateExpression("{{" + rfield + "}} * " + val));
	}

	public Update setDivision(String lfield, Number val) {
		return (Update) super.add(lfield, new DivisionExpression(val));
	}

	public Update setDivision(String lfield, String rfield, Number val) {
		return (Update) super.add(lfield, new CustomUpdateExpression("{{" + rfield + "}} / " + val));
	}

	public Update setCustom(String lfield, String expression) {
		return (Update) super.add(lfield, new CustomUpdateExpression(expression));
	}

	public Update setOther(String lfield, String rfield) {
		return (Update) super.add(lfield, new CustomUpdateExpression("{{" + rfield + "}}"));
	}

	public Update setNow(String lfield) {
		return (Update) super.add(lfield, NOW_EXPRESSION);
	}

	public Update setToday(String lfield) {
		return (Update) super.add(lfield, TODAY_EXPRESSION);
	}

	public Update setNowWithOffset(String lfield, int seconds) {
		return (Update) super.add(lfield, new CustomUpdateExpression(
				seconds >= 0 ? "{{g:nowadd_" + seconds + "}}" : "{{g:nowsub_" + -seconds + "}}"));
	}

	public Update setTodayWithOffset(String lfield, int days) {
		return (Update) super.add(lfield, new CustomUpdateExpression(
				days >= 0 ? "{{g:todayadd_" + days + "}}" : "{{g:todaysub_" + -days + "}}"));
	}

	public Update setWithOffset(String lfield, int seconds) {
		return (Update) super.add(lfield, new CustomUpdateExpression(
				seconds >= 0 ? "{{g:fldadd_" + seconds + "}}" : "{{g:fldsub_" + -seconds + "}}"));
	}

	public Update setWithOffset(String lfield, String rfield, int seconds) {
		return (Update) super.add(lfield,
				new CustomUpdateExpression(seconds >= 0 ? "{{g:fldadd_" + seconds + "_" + rfield + "}}"
						: "{{g:fldsub_" + -seconds + "_" + rfield + "}}"));
	}
}
