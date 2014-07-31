/* 
 * Copyright (C) 2014 Pivotal Software, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pivotal.arca.provider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public abstract class SQLiteTable extends SQLiteDataset {

    protected static interface Columns {
        @Column(Column.Type.INTEGER)
        @ColumnOptions("PRIMARY KEY AUTOINCREMENT")
        public static final String _ID = "_id";

        @Column(Column.Type.INTEGER)
        @ColumnOptions("DEFAULT 0")
        public static final String _STATE = "_state";
    }

	@Override
	public void onCreate(final SQLiteDatabase db) {
		final String columns = DatasetUtils.getColumns(this);
		final String create = String.format("CREATE TABLE IF NOT EXISTS %s (%s)", getName(), columns);
		db.execSQL(create);
	}

	@Override
	public void onDrop(final SQLiteDatabase db) {
		final String drop = String.format("DROP TABLE IF EXISTS %s", getName());
		db.execSQL(drop);
	}

	@Override
	public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
		final SQLiteDatabase database = getDatabase();
		if (database != null) {
			return database.update(getName(), values, selection, selectionArgs);
		} else {
			throw new IllegalStateException("Database is null.");
		}
	}

	@Override
	public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
		final SQLiteDatabase database = getDatabase();
		if (database != null) {
			return database.delete(getName(), selection, selectionArgs);
		} else {
			throw new IllegalStateException("Database is null.");
		}
	}

	@Override
	public Uri insert(final Uri uri, final ContentValues values) {
		final SQLiteDatabase database = getDatabase();
		if (database != null) {
			final long id = database.insert(getName(), null, values);
			return ContentUris.withAppendedId(uri, id);
		} else {
			throw new IllegalStateException("Database is null.");
		}
	}

	@Override
	public int bulkInsert(final Uri uri, final ContentValues[] values) {
		final SQLiteDatabase database = getDatabase();
		if (database != null) {
			return insertWithTransaction(database, getName(), values);
		} else {
			throw new IllegalStateException("Database is null.");
		}
	}

	private static int insertWithTransaction(final SQLiteDatabase database, final String name, final ContentValues[] values) {
		database.beginTransaction();
		try {
			final int inserted = insert(database, name, values);
			database.setTransactionSuccessful();
			return inserted;
		} finally {
			database.endTransaction();
		}
	}

	private static int insert(final SQLiteDatabase database, final String name, final ContentValues[] values) {
		int inserted = 0;
		for (final ContentValues value : values) {
			if (database.insert(name, null, value) > -1) {
				inserted++;
			}
		}
		return inserted;
	}
}
