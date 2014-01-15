package com.xtreme.rest;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;


import android.util.SparseArray;

class RestMapping<T> extends SparseArray<T> {

	private final Map<String, T> mObjects = new LinkedHashMap<String, T>();

	public void append(final int match, final Class<? extends T> klass) {
		final T instance = getOrCreateFromMap(klass);
		append(match, instance);
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
		final T instance = ClassUtils.getInstance(klass);
		mObjects.put(klass.getName(), instance);
		return instance;
	}

	public Collection<T> collect() {
		return mObjects.values();
	}

}