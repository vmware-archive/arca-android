package com.xtreme.rest.utils;

import java.util.Collection;


public class StringUtils {
	
	public static boolean isEmpty(final String input) {
		return input == null || input.length() <= 0;
	}

	public static boolean isNotEmpty(final String input) {
		return !isEmpty(input);
	}
	
	public static String left(final String s, final int n) {
		if (s != null && n >= 0) {
			return s.substring(0, Math.min(s.length(), n));
		} else {
			return "";
		}
	}
	
	public static String right(final String s, final int n) {
		if (s != null && n >= 0) {
			final int length = s.length();
			return s.substring(length - Math.min(length, n), length);
		} else {
			return "";
		}
	}
	
	public static String append(final String string, final String append, final String delimeter) {
		if (string == null) {
			return append;
		} else {
			final StringBuilder builder = new StringBuilder(string);
			if (delimeter != null) builder.append(delimeter);
			if (append != null) builder.append(append);
			return builder.toString();
		}
	}

	public static <T> String join(final Collection<T> s, final String delimiter) {
		if (s == null)
			return "";

		final StringBuffer buffer = new StringBuffer();
		for (final T t : s) {
			if (buffer.length() > 0 && delimiter != null) {
				buffer.append(delimiter);
			}
			buffer.append(t.toString());
		}
		return buffer.toString();
	}
	
	public static <T> String join(final T[] s, final String delimiter) {
		if (s == null)
			return "";

		final StringBuffer buffer = new StringBuffer();
		for (final T t : s) {
			if (buffer.length() > 0 && delimiter != null) {
				buffer.append(delimiter);
			}
			buffer.append(t.toString());
		}
		return buffer.toString();
	}
	
}
