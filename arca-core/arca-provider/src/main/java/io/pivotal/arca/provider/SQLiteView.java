/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.provider;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public abstract class SQLiteView extends SQLiteDataset {

	@Override
	public void onCreate(final SQLiteDatabase db) {
		final String select = DatasetUtils.getSelect(this);
		final String create = String.format("CREATE VIEW IF NOT EXISTS %s AS %s;", getName(), select);
		db.execSQL(create);
	}

	@Override
	public void onDrop(final SQLiteDatabase db) {
		final String drop = String.format("DROP VIEW IF EXISTS %s;", getName());
		db.execSQL(drop);
	}

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
