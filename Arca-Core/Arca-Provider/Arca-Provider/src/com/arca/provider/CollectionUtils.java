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

import java.util.ArrayList;
import java.util.Collection;

public class CollectionUtils {

	public static <T> Collection<T> filter(final Collection<? extends Object> objects, final Class<T> filterType) {
		final Collection<T> filtered = new ArrayList<T>();
		for (final Object object : objects) {
			if (filterType.isInstance(object)) {
				filtered.add(filterType.cast(object));
			}
		}
		return filtered;
	}
}
