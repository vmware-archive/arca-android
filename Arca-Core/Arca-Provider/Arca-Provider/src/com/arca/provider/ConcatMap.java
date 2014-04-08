/* 
 * Copyright (C) 2014 Pivotal Software, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arca.provider;

import java.util.HashMap;

public class ConcatMap extends HashMap<String, String> {

	private static final long serialVersionUID = 5200881657758758913L;

	private final String mDelimeter;

	public ConcatMap(final String delimeter) {
		mDelimeter = delimeter;
	}

	@Override
	public String put(final String key, String value) {

		final String existing = get(key);
		if (existing != null) {
			value = existing + mDelimeter + value;
		}

		return super.put(key, value);
	}

}
