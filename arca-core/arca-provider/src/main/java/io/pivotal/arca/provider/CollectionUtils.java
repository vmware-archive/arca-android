/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.provider;

import java.util.ArrayList;
import java.util.Collection;

public class CollectionUtils {

	public static <T> Collection<T> filter(final Collection<?> objects, final Class<T> filterType) {
		final Collection<T> filtered = new ArrayList<T>();
		for (final Object object : objects) {
			if (filterType.isInstance(object)) {
				filtered.add(filterType.cast(object));
			}
		}
		return filtered;
	}
}
