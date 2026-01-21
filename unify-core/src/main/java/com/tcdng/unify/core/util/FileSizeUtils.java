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

import java.text.DecimalFormat;

/**
 * File size utilities.
 * 
 * @author The Code Department
 * @version 1.0
 */
public final class FileSizeUtils {

    private static final long KB = 1024L;
    private static final long MB = KB * 1024;
    private static final long GB = MB * 1024;
    private static final long TB = GB * 1024;
    private static final long PB = TB * 1024;
    private static final long EB = PB * 1024;
	
	private FileSizeUtils() {

	}

	public static String formatReadable(long bytes) {
		if (bytes < 0) {
			throw new IllegalArgumentException("Size must not be negative");
		}

		final DecimalFormat format = new DecimalFormat("#0.0");
		if (bytes < KB) {
			return bytes + " B";
		} else if (bytes < MB) {
			return format.format((double) bytes / KB) + " KB";
		} else if (bytes < GB) {
			return format.format((double) bytes / MB) + " MB";
		} else if (bytes < TB) {
			return format.format((double) bytes / GB) + " GB";
		} else if (bytes < PB) {
			return format.format((double) bytes / TB) + " TB";
		} else if (bytes < EB) {
			return format.format((double) bytes / PB) + " PB";
		}

		return format.format((double) bytes / EB) + " EB";
	}
}
