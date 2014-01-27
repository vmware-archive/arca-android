package com.xtreme.rest.provider;

import android.content.ContentValues;
import android.net.Uri;

public abstract class SQLView extends SQLDataset {
	
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
		throw new UnsupportedOperationException("\"Bulk Insert\" cannot be called on a view!");
	}

	@Override
	public Uri insert(final Uri uri, final ContentValues values) {
		throw new UnsupportedOperationException("\"Insert\" cannot be called on a view!");
	}

}
