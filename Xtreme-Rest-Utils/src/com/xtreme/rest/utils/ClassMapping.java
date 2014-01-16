package com.xtreme.rest.utils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import android.util.SparseArray;

public class ClassMapping<T> extends SparseArray<T> {

	private final Map<String, T> mObjects = new LinkedHashMap<String, T>();

	public void append(final int index, final Class<? extends T> klass) {
		final T instance = getOrCreateFromMap(klass);
		append(index, instance);
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