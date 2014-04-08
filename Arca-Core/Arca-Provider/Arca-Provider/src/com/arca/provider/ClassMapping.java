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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import android.util.SparseArray;

public class ClassMapping<T> extends SparseArray<T> {

	private final Map<String, T> mObjects = new LinkedHashMap<String, T>();

	public void append(final int index, final Class<? extends T> klass) {
		final T instance = getOrCreateFromMap(klass);
		if (instance != null) {
			removeExistingFromMapIfNeeded(index);
			append(index, instance);
		}
	}

	private void removeExistingFromMapIfNeeded(final int index) {
		final T existing = get(index);
		if (existing != null) {
			final String name = existing.getClass().getName();
			mObjects.remove(name);
		}
	}

	private T getOrCreateFromMap(final Class<? extends T> klass) {
		final T instance = mObjects.get(klass.getName());
		if (instance == null) {
			return createAndAddToMap(klass);
		} else {
			return instance;
		}
	}

	private T createAndAddToMap(final Class<? extends T> klass) {
		final T instance = newInstance(klass);
		if (instance != null) {
			mObjects.put(klass.getName(), instance);
		}
		return instance;
	}

	public Collection<T> values() {
		return mObjects.values();
	}

	private static <T> T newInstance(final Class<T> klass) {
		try {
			return klass.newInstance();
		} catch (final Exception e) {
			return null;
		}
	}

}