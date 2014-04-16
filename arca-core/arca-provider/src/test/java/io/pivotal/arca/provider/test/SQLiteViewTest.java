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
import io.pivotal.arca.provider.DatabaseHelper;
import io.pivotal.arca.provider.SQLiteDataset;
import io.pivotal.arca.provider.SQLiteTable;
import io.pivotal.arca.provider.SQLiteView;
import io.pivotal.arca.provider.Select;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Collection;

public class SQLiteViewTest extends AndroidTestCase {

	private static final Uri URI = Uri.EMPTY;
	private static final ContentValues VALUES = new ContentValues();
	private static final TestSQLiteTable TABLE = new TestSQLiteTable();
	private static final TestSQLiteView VIEW = new TestSQLiteView();
	private static final Collection<SQLiteDataset> DATASETS = new ArrayList<SQLiteDataset>();

	static {
		VALUES.put("id", "test");
	}

	static {
		DATASETS.add(TABLE);
		DATASETS.add(VIEW);
	}

	private SQLiteDatabase mDatabase;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		createDatabase();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		closeDatabase();
		deleteDatabase();
	}

	public void testSQLiteViewQuery() {
		TABLE.setDatabase(mDatabase);
		final Uri uri = TABLE.insert(URI, VALUES);
		assertNotNull(uri);

		VIEW.setDatabase(mDatabase);
		final Cursor cursor = VIEW.query(URI, null, null, null, null);
		assertEquals(1, cursor.getCount());
		cursor.getColumnIndexOrThrow("id");
		cursor.close();
	}

	public void testSQLiteViewQueryThrowsExceptionWithoutDatabase() {
		try {
			VIEW.setDatabase(null);
			VIEW.query(null, null, null, null, null);
			Assert.fail();
		} catch (final Exception e) {
			assertEquals("Database is null.", e.getLocalizedMessage());
		}
	}

	public void testSQLiteViewUpdateThrowsException() {
		try {
			VIEW.update(null, null, null, null);
			Assert.fail();
		} catch (final Exception e) {
			assertEquals("A SQLiteView does not support update operations.", e.getLocalizedMessage());
		}
	}

	public void testSQLiteViewInsertThrowsException() {
		try {
			VIEW.insert(null, null);
			Assert.fail();
		} catch (final UnsupportedOperationException e) {
			assertEquals("A SQLiteView does not support insert operations.", e.getLocalizedMessage());
		}
	}

	public void testSQLiteViewBulkInsertThrowsException() {
		try {
			VIEW.bulkInsert(null, null);
			Assert.fail();
		} catch (final UnsupportedOperationException e) {
			assertEquals("A SQLiteView does not support bulk insert operations.", e.getLocalizedMessage());
		}
	}

	public void testSQLiteViewDeleteThrowsException() {
		try {
			VIEW.delete(null, null, null);
			Assert.fail();
		} catch (final UnsupportedOperationException e) {
			assertEquals("A SQLiteView does not support delete operations.", e.getLocalizedMessage());
		}
	}

	// ====================================

	public void createDatabase() {
		final DatabaseConfiguration config = new DatabaseConfiguration.DefaultDatabaseConfiguration(getContext());
		final DatabaseHelper helper = DatabaseHelper.create(getContext(), config, DATASETS);
		mDatabase = helper.getWritableDatabase();
	}

	public void deleteDatabase() {
		final DatabaseConfiguration config = new DatabaseConfiguration.DefaultDatabaseConfiguration(getContext());
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

	public static final class TestSQLiteView extends SQLiteView {

		@Select("SELECT * FROM (TestSQLiteTable)")
		public static interface Columns extends TestSQLiteTable.Columns {
		}

		@Override
		public void setDatabase(final SQLiteDatabase db) {
			super.setDatabase(db);
		}
	}
}
