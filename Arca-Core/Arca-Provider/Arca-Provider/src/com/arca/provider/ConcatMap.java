package com.arca.provider;

import java.util.HashMap;

public class ConcatMap extends HashMap<String, String> {

	private static final long serialVersionUID = 5200881657758758913L;

	private final String mDelimeter;
	
	public ConcatMap(final String delimeter) {
		mDelimeter = delimeter;
	}
	
	@Override
	public String put(final String key, String value) {

		final String existing = get(key);
		if (existing != null) {
			value = existing + mDelimeter + value;
		}

		return super.put(key, value);
	}
	
}
