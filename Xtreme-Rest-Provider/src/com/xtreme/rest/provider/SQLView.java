package com.xtreme.rest.provider;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public abstract class SQLView extends SQLDataset {
	
	protected abstract String onCreateSelectStatement();
	
	@Override
	public void onCreate(final SQLiteDatabase db) {
		final String selectStatement = onCreateSelectStatement();
		if (selectStatement == null)
			throw new IllegalStateException("Please override onCreateSelectStatement and supply a valid SQLite select statement.");

		final String query = SQLUtils.generateCreateViewStatement(getName(), selectStatement);
		db.execSQL(query);
	}
	
	@Override
	public void onDrop(final SQLiteDatabase db) {
		final String query = SQLUtils.generateDropViewStatement(getName());
		db.execSQL(query);
	}

	@Override
	public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
		throw new UnsupportedOperationException("\"Delete\" cannot be called on a view!");
	}

	@Override
	public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
		throw new UnsupportedOperationException("\"Update\" cannot be called on a view!");
	}

	@Override
	public int bulkInsert(final Uri uri, final ContentValues[] values) {
		throw new UnsupportedOperationException("\"BulkInsert\" cannot be called on a view!");
	}

	@Override
	public Uri insert(final Uri uri, final ContentValues values) {
		throw new UnsupportedOperationException("\"Insert\" cannot be called on a view!");
	}

}
