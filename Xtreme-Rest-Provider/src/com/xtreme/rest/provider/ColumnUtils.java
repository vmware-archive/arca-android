package com.xtreme.rest.provider;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ColumnUtils {

	public static String toString(final Class<?> klass) {
		final Column[] columns = all(klass);
		final StringBuilder builder = new StringBuilder();
		for (final Column column : columns) {
			builder.append(String.format("%s %s,", column.name, column.type));
		}
		if (builder.length() > 0) {
			builder.deleteCharAt(builder.length() - 1);
		}
		return builder.toString();
	}
	
	public static Column[] all(final Class<?> klass) {
		try {
			final Field[] fields = klass.getFields();
			return filter(fields);
		} catch (final IllegalAccessException e) {
			return new Column[0];
		}
	}

	private static Column[] filter(final Field[] fields) throws IllegalAccessException {
		final List<Column> filtered = filter(fields, Column.class);
		return filtered.toArray(new Column[filtered.size()]);
	}

	private static <T> List<T> filter(final Field[] fields, final Class<T> klass) throws IllegalAccessException {
		final List<T> filtered = new ArrayList<T>();
		for (final Field field : fields) {
			if (field.getType().isAssignableFrom(klass)) {
				filtered.add(klass.cast(field.get(null)));
			}
		}
		return filtered;
	}
}