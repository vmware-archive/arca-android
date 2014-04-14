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
package com.arca.service;

import com.arca.threading.Identifier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

class IdentifierMap<T> extends HashMap<Identifier<?>, Set<T>> {
	private static final long serialVersionUID = 2219124556989041435L;

	public void add(final Identifier<?> identifier, final T object) {
		final Set<T> set = getOrCreate(identifier);
		set.add(object);
	}

	private Set<T> getOrCreate(final Identifier<?> identifier) {
		Set<T> set = get(identifier);
		if (set == null) {
			set = new HashSet<T>();
			put(identifier, set);
		}
		return set;
	}

}