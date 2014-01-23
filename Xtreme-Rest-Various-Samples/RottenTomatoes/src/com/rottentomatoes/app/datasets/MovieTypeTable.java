package com.rottentomatoes.app.datasets;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.provider.BaseColumns;

import com.rottentomatoes.app.models.Movie;
import com.xtreme.rest.provider.SQLTable;

public class MovieTypeTable extends SQLTable {
	
	public static final String TABLE_NAME = "movie_types";
	
	public static final class Columns {
        public static final String ID = "id";
        public static final String TYPE = "type";
	}
	
	@Override
	public String getName() {
		return TABLE_NAME;
	}

	@Override
	protected Map<String, String> onCreateColumnMapping() {
		final Map<String, String> map = new LinkedHashMap<String, String>();
		map.put(BaseColumns._ID, "INTEGER PRIMARY KEY AUTOINCREMENT");
        map.put(Columns.ID, "TEXT");
        map.put(Columns.TYPE, "TEXT");
		return map;
	}
	
	@Override
	protected String onCreateUniqueConstraint() {
		return "UNIQUE (" + Columns.ID + "," + Columns.TYPE + ") ON CONFLICT REPLACE";
	}
	
	public static ContentValues[] getContentValues(final List<Movie> list, final String type) {
		final ContentValues[] values = new ContentValues[list.size()];
		for (int i = 0; i < values.length; i++) {
			values[i] = getContentValues(list.get(i), type);
		}
		return values;
    }
	
	public static ContentValues getContentValues(final Movie item, final String type) {
		final ContentValues value = new ContentValues();
        value.put(Columns.ID, item.getId());
        value.put(Columns.TYPE, type);
        return value;
    }
}

