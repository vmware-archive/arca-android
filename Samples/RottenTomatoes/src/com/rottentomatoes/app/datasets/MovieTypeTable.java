package com.rottentomatoes.app.datasets;

import java.util.List;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.arca.provider.Column;
import com.arca.provider.Column.Type;
import com.arca.provider.ColumnUtils;
import com.arca.provider.SQLTable;
import com.rottentomatoes.app.models.Movie;

public class MovieTypeTable extends SQLTable {
	
	public static interface Columns extends SQLTable.Columns {
		public static final Column ID = Type.TEXT.newColumn("id");
        public static final Column TYPE = Type.TEXT.newColumn("type");
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

