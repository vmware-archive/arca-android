package com.arca.provider.test;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.arca.provider.DatabaseConfiguration;
import com.arca.provider.DatabaseConfiguration.DefaultDatabaseConfiguration;
import com.arca.provider.DatabaseHelper;
import com.arca.provider.SQLiteDataset;
import com.arca.provider.SQLiteTable;
import com.arca.provider.SQLiteView;

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
		} catch(final Exception e) {
			assertEquals("Database is null.", e.getLocalizedMessage());
		}
	}
	
	public void testSQLiteViewUpdateThrowsException() {
		try {
			VIEW.update(null, null, null, null);
			Assert.fail();
		} catch(final Exception e) {
			assertEquals("A SQLiteView does not support update operations.", e.getLocalizedMessage());
		}
	}
	
	public void testSQLiteViewInsertThrowsException() {
		try {
			VIEW.insert(null, null);
			Assert.fail();
		} catch(final UnsupportedOperationException e) {
			assertEquals("A SQLiteView does not support insert operations.", e.getLocalizedMessage());
		}
	}
	
	public void testSQLiteViewBulkInsertThrowsException() {
		try {
			VIEW.bulkInsert(null, null);
			Assert.fail();
		} catch(final UnsupportedOperationException e) {
			assertEquals("A SQLiteView does not support bulk insert operations.", e.getLocalizedMessage());
		}
	}
	
	public void testSQLiteViewDeleteThrowsException() {
		try {
			VIEW.delete(null, null, null);
			Assert.fail();
		} catch(final UnsupportedOperationException e) {
			assertEquals("A SQLiteView does not support delete operations.", e.getLocalizedMessage());
		}
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

	private static final class TestSQLiteTable extends SQLiteTable {

		@Override
		public void onCreate(final SQLiteDatabase db) {
			db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (id TEXT);", getName()));
		}

		@Override
		public void onDrop(final SQLiteDatabase db) {
			db.execSQL(String.format("DROP TABLE IF EXISTS %s;", getName()));
		}
		
		@Override
		public void setDatabase(final SQLiteDatabase db) {
			super.setDatabase(db);
		}
	}
	
	private static final class TestSQLiteView extends SQLiteView {

		@Override
		public void onCreate(final SQLiteDatabase db) {
			db.execSQL(String.format("CREATE VIEW IF NOT EXISTS %s AS SELECT * FROM %s;", getName(), TestSQLiteTable.class.getSimpleName()));
		}

		@Override
		public void onDrop(final SQLiteDatabase db) {
			db.execSQL(String.format("DROP VIEW IF EXISTS %s;", getName()));
		}
		
		@Override
		public void setDatabase(final SQLiteDatabase db) {
			super.setDatabase(db);
		}
	}
}
