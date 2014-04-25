package com.xtreme.rest.utils;

public class ClassUtils {

	public static Object getInstanceFromClass(final Class<?> klass) {
		try {
			final Object object = klass.getMethod("getInstance").invoke(null);
			return klass.cast(object);
		} catch (final Exception e) {
			return createInstanceFromClass(klass);
		}
	}
	
	private static Object createInstanceFromClass(final Class<?> klass) {
		try {
			return klass != null ? klass.newInstance() : null;
		} catch (final InstantiationException e) {
			throw new RuntimeException(e);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
}
