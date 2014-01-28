package com.rottentomatoes.app.datasets;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.xtreme.rest.provider.SQLView;
import com.xtreme.rest.utils.ArrayUtils;
import com.xtreme.rest.utils.StringUtils;

public class MovieTypeView extends SQLView {

	public static interface Columns extends MovieTable.Columns, MovieTypeTable.Columns {}

	@Override
	public void onCreate(final SQLiteDatabase db) {
		final String movieTableId = String.format("%s.%s", MovieTable.class.getSimpleName(), MovieTable.Columns.ID);
		final String movieTypeTableId = String.format("%s.%s", MovieTypeTable.class.getSimpleName(), MovieTable.Columns.ID);
		final String create = String.format("CREATE VIEW IF NOT EXISTS %s AS SELECT * FROM (%s LEFT JOIN %s ON %s = %s);", getName(), MovieTable.class.getSimpleName(), MovieTypeTable.class.getSimpleName(), movieTableId, movieTypeTableId);
		db.execSQL(create);
	}
	
	@Override
	public void onDrop(final SQLiteDatabase db) {
		final String drop = String.format("DROP VIEW IF EXISTS %s;", getName());
		db.execSQL(drop);
	}
	
	@Override
	public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
		if (uri.getPathSegments().size() > 1) { 
			final String selectionWithType = StringUtils.append(selection, Columns.TYPE + "=?", " AND ");
			final String[] selectionArgsWithType = ArrayUtils.append(selectionArgs, new String[] { uri.getLastPathSegment() });
			return super.query(uri, projection, selectionWithType, selectionArgsWithType, sortOrder);
		} else {
			return super.query(uri, projection, selection, selectionArgs, sortOrder);
		}
	}

}
