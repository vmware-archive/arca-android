package io.pivotal.arca.provider;

import android.util.SparseArray;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

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
