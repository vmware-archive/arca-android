package com.xtreme.rest.providers;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public abstract class SQLView extends Dataset {
	
	private static final String EXCEPTION_MESSAGE = "Please override onCreateSelectStatement and supply a valid SQLite select statement.";
	
	private static final String CREATE_VIEW = "CREATE VIEW IF NOT EXISTS %s AS SELECT * FROM ( %s );";
	private static final String DROP_VIEW = "DROP VIEW IF EXISTS %s;";

	@Override
	public void onCreate(final SQLiteDatabase db) {
		final String selectStatement = onCreateSelectStatement();
		if (selectStatement == null)
			throw new IllegalStateException(EXCEPTION_MESSAGE);

		final String query = String.format(CREATE_VIEW, getName(), selectStatement);
		db.execSQL(query);
	}

	protected abstract String onCreateSelectStatement();
	
	@Override
	public void drop(final SQLiteDatabase db) {
		final String query = String.format(DROP_VIEW, getName());
		db.execSQL(query);
	}

	@Override
	public int delete(final SQLiteDatabase database, final Uri uri, final String selection, final String[] selectionArgs) {
		throw new UnsupportedOperationException("\"Delete\" cannot be called on a view!");
	}

	@Override
	public int delete(final SQLiteDatabase database, final String selection, final String[] selectionArgs) {
		return delete(database, null, selection, selectionArgs);
	}

	@Override
	public int update(final SQLiteDatabase database, final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
		throw new UnsupportedOperationException("\"Update\" cannot be called on a view!");
	}

	@Override
	public int update(final SQLiteDatabase database, final ContentValues values, final String selection, final String[] selectionArgs) {
		return update(database, null, values, selection, selectionArgs);
	}

	@Override
	public int bulkInsert(final SQLiteDatabase database, final Uri uri, final ContentValues[] values) {
		throw new UnsupportedOperationException("\"BulkInsert\" cannot be called on a view!");
	}

	@Override
	public int bulkInsert(final SQLiteDatabase database, final ContentValues[] values) {
		return bulkInsert(database, null, values);
	}

	@Override
	public long insert(final SQLiteDatabase database, final Uri uri, final ContentValues values) {
		throw new UnsupportedOperationException("\"Insert\" cannot be called on a view!");
	}

	@Override
	public long insert(final SQLiteDatabase database, final ContentValues values) {
		return insert(database, null, values);
	}
}
