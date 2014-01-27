package com.rottentomatoes.app.datasets;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.rottentomatoes.app.models.Movie;
import com.xtreme.rest.provider.Column;
import com.xtreme.rest.provider.Column.Type;
import com.xtreme.rest.provider.ColumnUtils;
import com.xtreme.rest.provider.SQLTable;
import com.xtreme.rest.utils.ArrayUtils;
import com.xtreme.rest.utils.StringUtils;

public class MovieTable extends SQLTable {
	
	public static interface Columns extends SQLTable.Columns {
        public static final Column ID = new Column("id", Type.TEXT);
        public static final Column TITLE = new Column("title", Type.TEXT);
        public static final Column YEAR = new Column("year", Type.TEXT);
        public static final Column MPAA_RATING = new Column("mpaa_rating", Type.TEXT);
        public static final Column RUNTIME = new Column("runtime", Type.TEXT);
        public static final Column CRITICS_CONSENSUS = new Column("critics_consensus", Type.TEXT);
        public static final Column SYNOPSIS = new Column("synopsis", Type.TEXT);
        public static final Column IMAGE_URL = new Column("image_url", Type.TEXT);
	};
	
	@Override
	public void onCreate(final SQLiteDatabase db) {
		final String columns = ColumnUtils.toString(Columns.class);
		final String constraint = "UNIQUE (" + Columns.ID + ") ON CONFLICT REPLACE";
		db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (%s, %s);", getName(), columns, constraint));
	}
	
	@Override
	public void onDrop(final SQLiteDatabase db) {
		db.execSQL(String.format("DROP TABLE IF EXISTS %s;", getName()));
	}
	
	@Override
	public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
		if (uri.getPathSegments().size() > 1) { 
			final String selectionWithId = StringUtils.append(selection, Columns.ID + "=?", " AND ");
			final String[] selectionArgsWithId = ArrayUtils.append(selectionArgs, new String[] { uri.getLastPathSegment() });
			return super.query(uri, projection, selectionWithId, selectionArgsWithId, sortOrder);
		} else {
			return super.query(uri, projection, selection, selectionArgs, sortOrder);
		}
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

