/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.service;

import io.pivotal.arca.threading.Identifier;

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