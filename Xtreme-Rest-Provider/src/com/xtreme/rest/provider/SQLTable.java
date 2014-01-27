package com.xtreme.rest.provider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public abstract class SQLTable extends SQLDataset {
	
	@Override
	public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
		return getDatabase().update(getName(), values, selection, selectionArgs);
	}

	@Override
	public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
		return getDatabase().delete(getName(), selection, selectionArgs);
	}

	@Override
	public Uri insert(final Uri uri, final ContentValues values) {
		final long id = getDatabase().insert(getName(), null, values);
		return ContentUris.withAppendedId(uri, id);
	}
	
	@Override
	public int bulkInsert(final Uri uri, final ContentValues[] values) {
		final SQLiteDatabase database = getDatabase();
		database.beginTransaction();
		try {
			final String name = getName();
			final int numInserted = insert(database, name, values);
			database.setTransactionSuccessful();
			return numInserted;
		} finally {
			database.endTransaction();
		}
	}

	private static int insert(final SQLiteDatabase database, final String name, final ContentValues[] values) {
		int numInserted = 0;
		for (final ContentValues value : values) {
			if (database.insert(name, null, value) > -1) {
				numInserted++;
			}
		}
		return numInserted;
	}
}
