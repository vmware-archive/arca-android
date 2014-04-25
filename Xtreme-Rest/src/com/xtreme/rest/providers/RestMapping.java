package com.xtreme.rest.providers;

import java.util.LinkedHashSet;
import java.util.Set;

import android.util.SparseArray;

class RestMapping<T> extends SparseArray<T> {

	public Set<T> collect() {
		final int size = size();
		final Set<T> set = new LinkedHashSet<T>(size);
		for (int i = 0; i < size; i++) {
			final int key = keyAt(i);
			final T item = get(key);
			if (item != null)
				set.add(item);
		}
		return set;
	}
}