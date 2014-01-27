package com.rottentomatoes.app.datasets;

import java.util.List;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.rottentomatoes.app.models.Movie;
import com.xtreme.rest.provider.Column;
import com.xtreme.rest.provider.Column.Type;
import com.xtreme.rest.provider.ColumnUtils;
import com.xtreme.rest.provider.SQLTable;

public class MovieTypeTable extends SQLTable {
	
	public static interface Columns extends SQLTable.Columns {
		public static final Column ID = new Column("id", Type.TEXT);
        public static final Column TYPE = new Column("type", Type.TEXT);
	}
	
	@Override
	public void onCreate(final SQLiteDatabase db) {
		final String columns = ColumnUtils.toString(Columns.class);
		final String constraint = "UNIQUE (" + Columns.ID + "," + Columns.TYPE + ") ON CONFLICT REPLACE";
		db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (%s, %s);", getName(), columns, constraint));
	}
	
	@Override
	public void onDrop(final SQLiteDatabase db) {
		db.execSQL(String.format("DROP TABLE IF EXISTS %s;", getName()));
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
        value.put(Columns.ID.name, item.getId());
        value.put(Columns.TYPE.name, type);
        return value;
    }
}

