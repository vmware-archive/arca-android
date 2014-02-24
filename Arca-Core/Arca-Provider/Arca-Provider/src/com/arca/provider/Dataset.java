package com.arca.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public interface Dataset {

	public Uri insert(final Uri uri, final ContentValues values);

	public int bulkInsert(final Uri uri, final ContentValues[] values);

	public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs);
	
	public int delete(final Uri uri, final String selection, final String[] selectionArgs);

	public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder);
	
}
