/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public interface Dataset {

	public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder);

	public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs);

	public Uri insert(final Uri uri, final ContentValues values);

	public int bulkInsert(final Uri uri, final ContentValues[] values);

	public int delete(final Uri uri, final String selection, final String[] selectionArgs);

}
