package com.rottentomatoes.app.datasets;

import java.util.List;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.arca.provider.Column;
import com.arca.provider.Column.Type;
import com.arca.provider.ColumnUtils;
import com.arca.provider.SQLiteTable;
import com.rottentomatoes.app.models.Movie;

public class MovieTable extends SQLiteTable {
	
	public static interface Columns extends SQLiteTable.Columns {
        public static final Column ID = Type.TEXT.newColumn("id");
        public static final Column TITLE = Type.TEXT.newColumn("title");
        public static final Column YEAR = Type.TEXT.newColumn("year");
        public static final Column MPAA_RATING = Type.TEXT.newColumn("mpaa_rating");
        public static final Column RUNTIME = Type.TEXT.newColumn("runtime");
        public static final Column CRITICS_CONSENSUS = Type.TEXT.newColumn("critics_consensus");
        public static final Column SYNOPSIS = Type.TEXT.newColumn("synopsis");
        public static final Column IMAGE_URL = Type.TEXT.newColumn("image_url");
	};
	
	@Override
	public void onCreate(final SQLiteDatabase db) {
		final String columns = ColumnUtils.toString(Columns.class);
		final String unique = String.format("UNIQUE (%s) ON CONFLICT REPLACE", Columns.ID);
		db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (%s, %s);", getName(), columns, unique));
	}
	
	@Override
	public void onDrop(final SQLiteDatabase db) {
		db.execSQL(String.format("DROP TABLE IF EXISTS %s;", getName()));
	}
	
	public static ContentValues[] getContentValues(final List<Movie> list) {
		final ContentValues[] values = new ContentValues[list.size()];
		for (int i = 0; i < values.length; i++) {
			values[i] = getContentValues(list.get(i));
		}
		return values;
    }
	
	public static ContentValues getContentValues(final Movie item) {
		final ContentValues value = new ContentValues();
        value.put(Columns.ID.name, item.getId());
        value.put(Columns.TITLE.name, item.getTitle());
        value.put(Columns.YEAR.name, item.getYear());
        value.put(Columns.MPAA_RATING.name, item.getMpaaRating());
        value.put(Columns.RUNTIME.name, item.getRuntime());
        value.put(Columns.CRITICS_CONSENSUS.name, item.getCriticsConsensus());
        value.put(Columns.SYNOPSIS.name, item.getSynopsis());
        value.put(Columns.IMAGE_URL.name, item.getImageUrl());
        return value;
    }
}

