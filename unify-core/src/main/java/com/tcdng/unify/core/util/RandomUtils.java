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

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

/**
 * Random utilities
 * 
 * @author The Code Department
 * @since 4.1
 */
public final class RandomUtils {

	private static final SecureRandom SECURE_RANDOM = new SecureRandom();

	private static String UPPERLETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private static String LOWERLETTERS = "abcdefghijklmnopqrstuvwxyz";

	private static String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private static String ALPHANUMERIC = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private static String DIGITS = "0123456789";

	private static String HEX = "0123456789abcdef";
	
	private static final String SPECIALS = "!@#$%*-_=+";

	//*****************************************************************/
	// DO NOT DELETE
	// SecureRandom.getInstanceStrong(); // Avoid in servers/containers
	// Blocks while waiting for entropy on linux machines. Very hard to debug
	//*****************************************************************/
	
	private RandomUtils() {

	}

	public static String generateRandomWord(int upperLetters, int lowerLetters, int digits, int specials) {
        if (upperLetters < 0 || lowerLetters < 0  || digits < 0 || specials < 0) {
            throw new IllegalArgumentException("Counts must be non-negative");
        }
        
        int total = upperLetters + lowerLetters + digits + specials;
        if (total <= 0) {
            throw new IllegalArgumentException("Total count must be greater than zero.");
        }
       
        char[] rand = new char[total];
        int index = 0;
        index = fillWithRandom(rand, index, UPPERLETTERS, upperLetters);
        index = fillWithRandom(rand, index, LOWERLETTERS, lowerLetters);
        index = fillWithRandom(rand, index, DIGITS, digits);
        fillWithRandom(rand, index, SPECIALS, specials);
        
        shuffleRandomly(rand);
        return new String(rand);		
	}
	
	public static String generateUUID() {
		return UUID.randomUUID().toString();
	}

	public static String generateUUIDInBase64() {
		final UUID uuid = UUID.randomUUID();
		return Base64.getUrlEncoder().withoutPadding().encodeToString(ByteBuffer.allocate(16)
				.putLong(uuid.getMostSignificantBits()).putLong(uuid.getLeastSignificantBits()).array());
	}

	public static String generateRandomLetters(int length) {
		return RandomUtils.generateRandom(LETTERS, length);
	}

	public static String generateRandomAlphanumeric(int length) {
		return RandomUtils.generateRandom(ALPHANUMERIC, length);
	}

	public static String generateRandomDigits(int length) {
		return RandomUtils.generateRandom(DIGITS, length);
	}

	private static String generateRandom(String characters, int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(characters.charAt(SECURE_RANDOM.nextInt(characters.length())));
		}

		return sb.toString();
	}

	public static byte[] generateRandomSeed(int length) {
		return new SecureRandom().generateSeed(length);
	}
	
	public static String bytesToHex(byte[] bytes) {
	    char[] hexArray = HEX.toCharArray();
	    char[] hexChars = new char[bytes.length * 2];

	    for (int i = 0; i < bytes.length; i++) {
	        int val = bytes[i] & 0xFF;
	        hexChars[i * 2] = hexArray[val >>> 4];
	        hexChars[i * 2 + 1] = hexArray[val & 0x0F];
	    }
	    
	    return new String(hexChars);
	}
	
	public static byte[] hexToBytes(String hex) {
	    if (hex == null) {
	        throw new IllegalArgumentException("Hex string cannot be null");
	    }

	    int len = hex.length();
	    if ((len & 1) != 0) {
	        throw new IllegalArgumentException("Hex string must have even length");
	    }

	    byte[] out = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        int hi = Character.digit(hex.charAt(i), 16);
	        int lo = Character.digit(hex.charAt(i + 1), 16);
	        if (hi == -1 || lo == -1) {
	            throw new IllegalArgumentException(
	                "Invalid hex character at position " + i
	            );
	        }

	        out[i / 2] = (byte) ((hi << 4) + lo);
	    }
	    
	    return out;
	}


	private static int fillWithRandom(char[] dest, int startIndex, String source, int count) {
		for (int i = 0; i < count; i++) {
			dest[startIndex + i] = source.charAt(SECURE_RANDOM.nextInt(source.length()));
		}
		
		return startIndex + count;
	}

	private static void shuffleRandomly(char[] dest) {
		for (int i = dest.length - 1; i > 0; i--) {
			int j = SECURE_RANDOM.nextInt(i + 1);
			char tmp = dest[i];
			dest[i] = dest[j];
			dest[j] = tmp;
		}
	}
	
}
