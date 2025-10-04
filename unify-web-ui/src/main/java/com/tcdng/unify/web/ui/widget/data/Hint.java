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
package com.tcdng.unify.web.ui.widget.data;

/**
 * Hint data object.
 * 
 * @author The Code Department
 * @since 4.1
 */
public class Hint {

	public enum MODE {
		INFO("bell", "#1E3A8A", "#909DC6", "#E1ECFA"),
		WARNING("triangle-exclamation", "#B5A200", "#DACB68", "#FFFDD0"),
		ERROR("exclamation-circle", "#B22222", "#DA8282", "#FDE2E2");

		private final String icon;

		private final String dark;

		private final String normal;

		private final String light;

		private MODE(String icon, String dark, String normal, String light) {
			this.icon = icon;
			this.dark = dark;
			this.normal = normal;
			this.light = light;
		}

		public String icon() {
			return icon;
		}

		public String dark() {
			return dark;
		}

		public String normal() {
			return normal;
		}

		public String light() {
			return light;
		}
	}

    private MODE mode;

    private String message;

    private boolean sticky;
    
    public Hint(MODE mode, String message) {
        this.mode = mode;
        this.message = message;
        this.sticky = true;
    }

    public Hint(MODE mode, String message, boolean sticky) {
        this.mode = mode;
        this.message = message;
        this.sticky = sticky;
    }

    public MODE getMode() {
        return mode;
    }

    public String getMessage() {
        return message;
    }
    
    public String key() {
        return mode + message;
    }

	public boolean isSticky() {
		return sticky;
	}
}
