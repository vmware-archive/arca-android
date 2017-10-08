package io.pivotal.arca.provider;

import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.Map;

import io.pivotal.arca.utils.Logger;

public class DatasetUtils {

    public static String getTableDefinition(final SQLiteTable table) {
        final String select = getSelect(table);

        if (!TextUtils.isEmpty(select)) {
            return "AS " + select;
        }

        return getColumns(table);
    }

    public static String getViewDefinition(final SQLiteView view) {
        final String select = getSelect(view);

        if (!TextUtils.isEmpty(select)) {
            return "AS " + select;
        }

        return "";
    }


    // =================================================


	public static String getSelect(final SQLiteDataset dataset) {
		final Class<?> klass = dataset.getClass();
		try {
			return getSelect(klass);
		} catch (final Exception e) {
			Logger.ex(e);
			return "";
		}
	}

	private static String getSelect(final Class<?> klass) throws Exception {
		final SelectFrom from = klass.getAnnotation(SelectFrom.class);
		if (from != null) {
            return getSelectString(klass, from);
		}

		final Class<?>[] klasses = klass.getDeclaredClasses();
        for (final Class<?> declared : klasses) {
            final String select = getSelect(declared);
            if (!TextUtils.isEmpty(select)) {
                return select;
            }
        }

		return "";
	}

    private static String getSelectString(final Class<?> klass, final SelectFrom from) {
        final String columnString = getColumnString(klass);
        final String fromString = getFromString(klass, from);

        final StringBuilder selectBuilder = new StringBuilder();
        selectBuilder.append(String.format("SELECT %s FROM (%s)", columnString, fromString));

        final SelectAs as = klass.getAnnotation(SelectAs.class);
        if (as != null) {
            selectBuilder.append(String.format(" AS %s", as.value()));
        }

        final Where where = klass.getAnnotation(Where.class);
        if (where != null) {
            selectBuilder.append(String.format(" WHERE %s", where.value()));
        }

        final GroupBy groupBy = klass.getAnnotation(GroupBy.class);
        if (groupBy != null) {
            selectBuilder.append(String.format(" GROUP BY %s", groupBy.value()));
        }

        final OrderBy orderBy = klass.getAnnotation(OrderBy.class);
        if (orderBy != null) {
            selectBuilder.append(String.format(" ORDER BY %s", orderBy.value()));
        }

        return selectBuilder.toString();
    }

    private static String getColumnString(final Class<?> klass) {
        final StringBuilder builder = new StringBuilder();

        try {
            getSelectColumns(klass, builder);
        } catch (final Exception e) {
            Logger.ex(e);
        }

        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        } else {
            builder.append("*");
        }

        return builder.toString();
    }

    private static String getFromString(final Class<?> klass, final SelectFrom from) {
        final StringBuilder fromBuilder = new StringBuilder(from.value());

        final Joins joins = klass.getAnnotation(Joins.class);
        if (joins != null) {
            for (final String join : joins.value()) {
                fromBuilder.append(" " + join);
            }
        }

        return fromBuilder.toString();
    }

    private static void getSelectColumns(final Class<?> klass, final StringBuilder builder) throws Exception {
        final Field[] fields = klass.getFields();
        for (final Field field : fields) {
            getSelectColumn(field, builder);
        }

        final Class<?>[] klasses = klass.getDeclaredClasses();
        for (int i = 0; i < klasses.length; i++) {
            getSelectColumns(klasses[i], builder);
        }
    }

    private static void getSelectColumn(final Field field, final StringBuilder builder) throws Exception {
        final Select select = field.getAnnotation(Select.class);
        if (select != null) {

            final String columnName = (String) field.get(null);
            builder.append(String.format("%s as %s,", select.value(), columnName));
        }
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

		return "(" + builder.toString() + ")";
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
        final Column columnType = field.getAnnotation(Column.class);
        if (columnType != null) {

            final String columnName = (String) field.get(null);

            final ColumnOptions columnOptions = field.getAnnotation(ColumnOptions.class);
            if (columnOptions != null) {
                builder.append(String.format("%s %s %s,", columnName, columnType.value().name(), columnOptions.value()));
            } else {
                builder.append(String.format("%s %s,", columnName, columnType.value().name()));
            }

            final Unique unique = field.getAnnotation(Unique.class);
            if (unique != null) {
                uniqueMap.put(unique.value().name(), columnName);
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
