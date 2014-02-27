package com.arca.provider;

import android.content.ContentValues;
import android.net.Uri;

public abstract class SQLiteView extends SQLiteDataset {
	
	@Override
	public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
		throw new UnsupportedOperationException("A SQLiteView does not support delete operations.");
	}

	@Override
	public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
		throw new UnsupportedOperationException("A SQLiteView does not support update operations.");
	}

	@Override
	public int bulkInsert(final Uri uri, final ContentValues[] values) {
		throw new UnsupportedOperationException("A SQLiteView does not support bulk insert operations.");
	}

	@Override
	public Uri insert(final Uri uri, final ContentValues values) {
		throw new UnsupportedOperationException("A SQLiteView does not support insert operations.");
	}

}
