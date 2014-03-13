package com.arca.provider;

import java.lang.reflect.Method;
import java.util.List;

import android.content.ContentValues;

public class DataUtils {

	public static <T> ContentValues[] getContentValues(final Class<T> klass, final List<?> list) {
		final ContentValues[] values = new ContentValues[list.size()];
		for (int i = 0; i < values.length; i++) {
			values[i] = getContentValues(klass, list.get(i));
		}
		return values;
    }

	public static <T> ContentValues getContentValues(final Class<T> klass, final Object item) {
		try {
			final Method method = klass.getMethod("getContentValues", item.getClass());
			return (ContentValues) method.invoke(null, item);	
		} catch (final Exception e) {
			return null;
		}
	}
}
