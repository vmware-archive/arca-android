package com.arca.provider;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import android.content.ContentValues;

import com.arca.utils.Logger;

public class DataUtils {

	public static ContentValues[] getContentValues(final List<?> list) {
		final ContentValues[] values = new ContentValues[list.size()];
		for (int i = 0; i < values.length; i++) {
			values[i] = getContentValues(list.get(i));
		}
		return values;
	}

	public static ContentValues getContentValues(final Object object) {
		final Class<?> klass = object.getClass();
		final ContentValues values = new ContentValues();
		try {
			getContentValuesFromFields(object, klass, values);
			getContentValuesFromMethods(object, klass, values);
		} catch (final Exception e) {
			Logger.ex(e);
		}
		return values;
	}

	private static void getContentValuesFromMethods(final Object object, final Class<?> klass, final ContentValues values) throws Exception {
		final Method[] methods = klass.getDeclaredMethods();
		for (final Method method : methods) {
			final ColumnName annotation = method.getAnnotation(ColumnName.class);
			if (annotation != null) {
				method.setAccessible(true);
				final Object value = method.invoke(object);
				addToValues(values, annotation.value(), value);
			}
		}

		final Class<?> superKlass = klass.getSuperclass();
		if (superKlass != null) {
			getContentValuesFromMethods(object, superKlass, values);
		}
	}

	private static void getContentValuesFromFields(final Object object, final Class<?> klass, final ContentValues values) throws Exception {
		final Field[] fields = klass.getDeclaredFields();
		for (final Field field : fields) {
			final ColumnName annotation = field.getAnnotation(ColumnName.class);
			if (annotation != null) {
				field.setAccessible(true);
				final Object value = field.get(object);
				addToValues(values, annotation.value(), value);
			}
		}

		final Class<?> superKlass = klass.getSuperclass();
		if (superKlass != null) {
			getContentValuesFromFields(object, superKlass, values);
		}
	}

	private static void addToValues(final ContentValues values, final String key, final Object value) throws Exception {
		if (value != null) {
			final Class<?> klass = values.getClass();
			final Method method = klass.getMethod("put", String.class, value.getClass());
			method.invoke(values, key, value);
		}
	}
}
