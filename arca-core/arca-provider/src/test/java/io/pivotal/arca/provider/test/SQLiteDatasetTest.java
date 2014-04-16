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
package io.pivotal.arca.provider.test;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

import io.pivotal.arca.provider.Column;
import io.pivotal.arca.provider.DatabaseConfiguration;
import io.pivotal.arca.provider.DatabaseConfiguration.DefaultDatabaseConfiguration;
import io.pivotal.arca.provider.DatabaseHelper;
import io.pivotal.arca.provider.SQLiteDataset;
import io.pivotal.arca.provider.SQLiteTable;

import java.util.ArrayList;
import java.util.Collection;

public class SQLiteDatasetTest extends AndroidTestCase {

	private static final Uri URI = Uri.EMPTY;
	private static final ContentValues VALUES = new ContentValues();
	private static final TestSQLiteTable TABLE = new TestSQLiteTable();
	private static final Collection<SQLiteDataset> DATASETS = new ArrayList<SQLiteDataset>();

	static {
		VALUES.put("id", "test");
	}

	static {
		DATASETS.add(TABLE);
	}

	private SQLiteDatabase mDatabase;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		createDatabase();
		insertRecordsIntoTable();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		closeDatabase();
		deleteDatabase();
	}

	private void insertRecordsIntoTable() {
		TABLE.setDatabase(mDatabase);
		final Uri uri = TABLE.insert(URI, VALUES);
		assertNotNull(uri);
	}

	private static void assertTableHasRecords() {
		final Cursor cursor = TABLE.query(URI, null, null, null, null);
		assertTrue(cursor.getCount() > 0);
		cursor.close();
	}

	private static void assertTableIsEmpty() {
		final Cursor cursor = TABLE.query(URI, null, null, null, null);
		assertEquals(0, cursor.getCount());
		cursor.close();
	}

	public void testSQLiteDatasetUpgrade() {
		assertTableHasRecords();
		TABLE.onUpgrade(mDatabase, 0, 1);
		assertTableIsEmpty();
	}

	public void testSQLiteDatasetDowngrade() {
		assertTableHasRecords();
		TABLE.onDowngrade(mDatabase, 1, 0);
		assertTableIsEmpty();
	}

	// ====================================

	public void createDatabase() {
		final DatabaseConfiguration config = new DefaultDatabaseConfiguration(getContext());
		final DatabaseHelper helper = DatabaseHelper.create(getContext(), config, DATASETS);
		mDatabase = helper.getWritableDatabase();
	}

	public void deleteDatabase() {
		final DatabaseConfiguration config = new DefaultDatabaseConfiguration(getContext());
		getContext().deleteDatabase(config.getDatabaseName());
	}

	public void closeDatabase() {
		if (mDatabase.isOpen()) {
			mDatabase.close();
		}
	}

	// ====================================

	public static final class TestSQLiteTable extends SQLiteTable {

		public static interface Columns {
			public static final Column ID = Column.Type.TEXT.newColumn("id");
		}

		@Override
		public void setDatabase(final SQLiteDatabase db) {
			super.setDatabase(db);
		}
	}
}
