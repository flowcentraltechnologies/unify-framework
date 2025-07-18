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
package com.tcdng.unify.core.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides utility methods for UPL tokens.
 * 
 * @author The Code Department
 * @since 4.1
 */
public final class TokenUtils {

    private static final Set<Character> tokenChars;

    static {
        tokenChars = new HashSet<Character>(
                Arrays.asList('m', 's', 'c', 'j', 'd', 'l', 'n', 't', 'x', 'o', 'f', 'g', 'e', 'q', 'p', 'r', 'w', 'u'));
    }

    private TokenUtils() {

    }

    /**
     * Tests if a string is a message token value
     * 
     * @param string
     *            the string to test
     */
    public static boolean isMessageToken(String string) {
        return string.startsWith("$m{") && string.charAt(string.length() - 1) == '}';
    }

	/**
	 * Get the index of message token.
	 * 
	 * @param string the string to get index of
	 * @return index greater than or equal to 0
	 */
	public static int indexOfMessageToken(String string) {
		return string.indexOf("$m{");
	}

    /**
     * Tests if a string is a unify property token value
     * 
     * @param string
     *            the string to test
     */
    public static boolean isUnifyPropertyToken(String string) {
        return string.startsWith("$u{") && string.charAt(string.length() - 1) == '}';
    }

    /**
     * Tests if a string is a component list token value
     * 
     * @param string
     *            the string to test
     */
    public static boolean isComponentListToken(String string) {
        return string.startsWith("$c{") && string.charAt(string.length() - 1) == '}';
    }

    /**
     * Tests if a string is a string token value
     * 
     * @param string
     *            the string to test
     */
    public static boolean isStringToken(String string) {
        return string.startsWith("$s{") && string.charAt(string.length() - 1) == '}';
    }

    /**
     * Tests if a string is a container setting token value
     * 
     * @param string
     *            the string to test
     */
    public static boolean isSettingToken(String string) {
        return string.startsWith("$x{") && string.charAt(string.length() - 1) == '}';
    }

    /**
     * Tests if a string is a java constant token value
     * 
     * @param string
     *            the string to test
     */
    public static boolean isJavaConstantToken(String string) {
        return string.startsWith("$j{") && string.charAt(string.length() - 1) == '}';
    }

    /**
     * Tests if a string is a descriptor token value.
     * 
     * @param string
     *            the string to test
     */
    public static boolean isDescriptorToken(String string) {
        return string.startsWith("$d{") && string.charAt(string.length() - 1) == '}';
    }

    /**
     * Tests if a string is a list token value.
     * 
     * @param string
     *            the string to test
     */
    public static boolean isListToken(String string) {
        return string.startsWith("$l{") && string.charAt(string.length() - 1) == '}';
    }

    /**
     * Tests if a string is a context scope tag value.
     * 
     * @param string
     *            the string to test
     */
    public static boolean isContextScopeTag(String string) {
        return string.startsWith("$o{") && string.charAt(string.length() - 1) == '}';
    }

    /**
     * Tests if a string is a name tag value.
     * 
     * @param string
     *            the string to test
     */
    public static boolean isNameTag(String string) {
        return string.startsWith("$n{") && string.charAt(string.length() - 1) == '}';
    }

    /**
     * Tests if a string is a path tag value.
     * 
     * @param string
     *            the string to test
     */
    public static boolean isPathTag(String string) {
        return string.startsWith("$p{") && string.charAt(string.length() - 1) == '}';
    }

    /**
     * Tests if a string is a request attribute tag value.
     * 
     * @param string
     *            the string to test
     */
    public static boolean isRequestAttributeTag(String string) {
        return string.startsWith("$r{") && string.charAt(string.length() - 1) == '}';
    }

    /**
     * Tests if a string is a guarded tag value.
     * 
     * @param string
     *            the string to test
     */
    public static boolean isGuardedTag(String string) {
        return string.startsWith("$g{") && string.charAt(string.length() - 1) == '}';
    }

    /**
     * Tests if a string is a foreign tag value.
     * 
     * @param string
     *            the string to test
     */
    public static boolean isForeignTag(String string) {
        return string.startsWith("$f{") && string.charAt(string.length() - 1) == '}';
    }

    /**
     * Tests if a string is a theme tag value.
     * 
     * @param string
     *            the string to test
     */
    public static boolean isThemeTag(String string) {
        return string.startsWith("$t{") && string.charAt(string.length() - 1) == '}';
    }

    /**
     * Tests if a string is a element type tag value.
     * 
     * @param string
     *            the string to test
     */
    public static boolean isElementTypeTag(String string) {
        return string.startsWith("$e{") && string.charAt(string.length() - 1) == '}';
    }

    /**
     * Tests if a string is a quick reference tag value.
     * 
     * @param string
     *            the string to test
     */
    public static boolean isQuickReferenceTag(String string) {
        return string.startsWith("$q{") && string.charAt(string.length() - 1) == '}';
    }

    /**
     * Tests if a string is a popup tag value.
     * 
     * @param string
     *            the string to test
     */
    public static boolean isPopupTag(String string) {
        return string.startsWith("$w{") && string.charAt(string.length() - 1) == '}';
    }

    /**
     * Tests for a token prefix in a string starting from a specified offset.
     * 
     * @param string
     *            the string to test
     */
    public static boolean isTokenPrefix(String string, int offset) {
        if (string != null && string.length() > 3 && string.charAt(offset) == '$' && string.charAt(offset + 2) == '{') {
            return tokenChars.contains(string.charAt(offset + 1));
        }

        return false;
    }

    /**
     * Extracts the value string from a token value. Assumes supplied string is a
     * token value.
     * 
     * @param string
     *            the string to extract from
     * @return the extracted value
     */
    public static String extractTokenValue(String string) {
        return string.substring(3, string.length() - 1);
    }

    /**
     * Extracts the value string from a string token value.
     * 
     * @param string
     *            the string to extract from
     * @return the extracted value
     */
    public static String getStringTokenValue(String string) {
        if (string != null && string.startsWith("$s{") && string.charAt(string.length() - 1) == '}') {
            return string.substring(3, string.length() - 1);
        }
        return string;
    }
}
