package com.xtreme.rest;

public class ClassUtils {

	public static <T> T getInstance(final Class<T> klass) {
		try {
			final Object object = klass.getMethod("getInstance").invoke(null);
			return klass.cast(object);
		} catch (final Exception e) {
			return createInstance(klass);
		}
	}
	
	private static <T> T createInstance(final Class<T> klass) {
		try {
			return klass != null ? klass.newInstance() : null;
		} catch (final InstantiationException e) {
			throw new RuntimeException(e);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
}
