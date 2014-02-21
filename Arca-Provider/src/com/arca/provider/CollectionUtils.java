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
