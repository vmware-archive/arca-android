package com.rottentomatoes.app.datasets;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.xtreme.rest.provider.SQLView;
import com.xtreme.rest.utils.ArrayUtils;
import com.xtreme.rest.utils.StringUtils;

public class MovieItemView extends SQLView {
	
	public static interface Columns extends MovieTable.Columns {}
	
	@Override
	public void onCreate(final SQLiteDatabase db) {
		final String create = String.format("CREATE VIEW IF NOT EXISTS %s AS SELECT * FROM %s;", getName(), MovieTable.class.getSimpleName());
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
			final String selectionWithId = StringUtils.append(selection, Columns.ID + "=?", " AND ");
			final String[] selectionArgsWithId = ArrayUtils.append(selectionArgs, new String[] { uri.getLastPathSegment() });
			return super.query(uri, projection, selectionWithId, selectionArgsWithId, sortOrder);
		} else {
			return super.query(uri, projection, selection, selectionArgs, sortOrder);
		}
	}
}

