package com.xtreme.rest.providers;

import com.xtreme.rest.utils.SQLUtils;

import android.content.ContentValues;
import android.net.Uri;

public abstract class SQLView extends SQLDataset {
	
	protected abstract String onCreateSelectStatement();
	
	@Override
	public void onCreate() {
		final String selectStatement = onCreateSelectStatement();
		if (selectStatement == null)
			throw new IllegalStateException("Please override onCreateSelectStatement and supply a valid SQLite select statement.");

		final String query = SQLUtils.generateCreateViewStatement(getName(), selectStatement);
		getDatabase().execSQL(query);
	}
	
	@Override
	public void onDrop() {
		final String query = SQLUtils.generateDropViewStatement(getName());
		getDatabase().execSQL(query);
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
