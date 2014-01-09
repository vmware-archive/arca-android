package com.xtreme.rest.utils;

import java.util.Map;

public class SQLUtils {

	public static String generateTableCreateStatement(final String name, final Map<String, String> mapping, final String constraint) {
		final String columnString = generateTableColumnString(mapping);
		final String uniqueString = generateTableUniqueString(constraint);
		return String.format("CREATE TABLE IF NOT EXISTS %s ( %s%s );", name, columnString, uniqueString);
	}
	
	private static String generateTableColumnString(final Map<String, String> mapping) {
		final StringBuilder builder = new StringBuilder();
		for (final String key : mapping.keySet()) {
			builder.append(String.format("%s %s,", key, mapping.get(key)));
		}
		if (builder.length() > 0) {
			builder.deleteCharAt(builder.length() -1);
		}
		return builder.toString();
	}
	
	private static String generateTableUniqueString(final String constraint) {
		return constraint != null ? String.format(", %s", constraint) : "";
	}

	public static String generateTableDropStatement(final String name) {
		return String.format("DROP TABLE IF EXISTS %s;", name);
	}

	public static String generateViewCreateStatement(final String name, final String selectStatement) {
		return String.format("CREATE VIEW IF NOT EXISTS %s AS SELECT * FROM (%s);", name, selectStatement);
	}

	public static String generateViewDropStatement(final String name) {
		return String.format("DROP VIEW IF EXISTS %s;", name);
	}
	
}
