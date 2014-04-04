package com.arca.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.arca.threading.Identifier;

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