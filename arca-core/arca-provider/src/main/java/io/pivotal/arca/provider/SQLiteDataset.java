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

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public abstract class SQLiteDataset implements Dataset {

	public abstract void onCreate(final SQLiteDatabase db);

	public abstract void onDrop(final SQLiteDatabase db);

	private SQLiteDatabase mDatabase;

	protected SQLiteDatabase getDatabase() {
		return mDatabase;
	}

	protected void setDatabase(final SQLiteDatabase db) {
		mDatabase = db;
	}

	public String getName() {
		final Class<? extends SQLiteDataset> klass = getClass();
		final DatasetName name = klass.getAnnotation(DatasetName.class);
		if (name != null) {
			return name.value();
		} else {
			return klass.getSimpleName();
		}
	}

	public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		onDrop(db);
		onCreate(db);
	}

	public void onDowngrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		onDrop(db);
		onCreate(db);
	}

	@Override
	public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
		final SQLiteDatabase database = getDatabase();
		if (database != null) {
			return database.query(getName(), projection, selection, selectionArgs, null, null, sortOrder);
		} else {
			throw new IllegalStateException("Database is null.");
		}
	}

}
