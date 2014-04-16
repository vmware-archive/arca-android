/* 
 * Copyright (C) 2014 Pivotal Software, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pivotal.arca.provider;

import android.text.TextUtils;

import io.pivotal.arca.utils.Logger;

import java.lang.reflect.Field;
import java.util.Map;

public class DatasetUtils {

	public static String getSelect(final SQLiteView view) {
		final Class<?> klass = view.getClass();
		try {
			final String clause = getSelect(klass);
			return clause;
		} catch (final Exception e) {
			Logger.ex(e);
			return "";
		}
	}

	private static String getSelect(final Class<?> klass) throws Exception {
		final Select select = klass.getAnnotation(Select.class);
		if (select != null) {
			return select.value();
		}

		final Class<?>[] klasses = klass.getDeclaredClasses();
		for (int i = 0; i < klasses.length; i++) {
			final String declared = getSelect(klasses[i]);
			if (!TextUtils.isEmpty(declared)) {
				return declared;
			}
		}

		return "";
	}

	// =================================================

	public static String getColumns(final SQLiteTable table) {
		final Class<?> klass = table.getClass();

		final StringBuilder builder = new StringBuilder();
		final ConcatMap uniqueMap = new ConcatMap(",");

		try {
			getColumns(klass, builder, uniqueMap);
		} catch (final Exception e) {
			Logger.ex(e);
		}

		builder.append(getUnique(uniqueMap));

		if (builder.length() > 0) {
			builder.deleteCharAt(builder.length() - 1);
		}

		return builder.toString();
	}

	private static void getColumns(final Class<?> klass, final StringBuilder builder, final ConcatMap uniqueMap) throws Exception {
		final Field[] fields = klass.getFields();
		for (final Field field : fields) {
			getColumn(field, builder, uniqueMap);
		}

		final Class<?>[] klasses = klass.getDeclaredClasses();
		for (int i = 0; i < klasses.length; i++) {
			getColumns(klasses[i], builder, uniqueMap);
		}
	}

	private static void getColumn(final Field field, final StringBuilder builder, final ConcatMap uniqueMap) throws Exception {
		if (field.getType().isAssignableFrom(Column.class)) {

			final Column column = (Column) field.get(null);

			final Unique annotation = field.getAnnotation(Unique.class);
			if (annotation != null) {
				uniqueMap.put(annotation.value().name(), column.name);
			}

			if (TextUtils.isEmpty(column.options)) {
				builder.append(String.format("%s %s,", column.name, column.type));
			} else {
				builder.append(String.format("%s %s %s,", column.name, column.type, column.options));
			}
		}
	}

	private static String getUnique(final Map<String, String> uniqueMap) {
		final StringBuilder builder = new StringBuilder();
		for (final String key : uniqueMap.keySet()) {
			final String format = "UNIQUE (%s) ON CONFLICT %s,";
			builder.append(String.format(format, uniqueMap.get(key), key));
		}
		return builder.toString();
	}

}