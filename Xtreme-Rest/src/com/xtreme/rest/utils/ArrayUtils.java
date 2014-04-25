package com.xtreme.rest.utils;

import java.util.Arrays;
import java.util.List;

public class ArrayUtils {
	
	public static <T> T[] append(final T[] first, final T[] second) {
		if (first == null) return second;
		if (second == null) return first;
		
		final T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
	
	public static boolean isEmpty(final List<?> items) {
		return items == null || items.size() == 0;
	}
	
	public static boolean isNotEmpty(final List<?> items) {
		return items != null && items.size() > 0;
	}
	
	public static <T> boolean isEmpty(final T[] items) {
		return items == null || items.length == 0;
	}
	
	public static <T> boolean isNotEmpty(final T[] items) {
		return items != null && items.length > 0;
	}
	
}
