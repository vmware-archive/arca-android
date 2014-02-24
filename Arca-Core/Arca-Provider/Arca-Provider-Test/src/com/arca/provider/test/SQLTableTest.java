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
import com.arca.provider.SQLDataset;
import com.arca.provider.SQLTable;

public class SQLTableTest extends AndroidTestCase {

	private static final Uri URI = Uri.EMPTY;
	private static final ContentValues VALUES = new ContentValues();
	private static final TestSQLTable TABLE = new TestSQLTable();
	private static final Collection<SQLDataset> DATASETS = new ArrayList<SQLDataset>();
	
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
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		closeDatabase();
		deleteDatabase();
	}
	
	public void testSQLTableQuery() {
		testSQLTableInsert();
		
		TABLE.setDatabase(mDatabase);
		final Cursor cursor = TABLE.query(URI, null, null, null, null);
		assertEquals(1, cursor.getCount());
		cursor.getColumnIndexOrThrow("id");
		cursor.close();
	}

	public void testSQLTableUpdate() {
		testSQLTableInsert();
		
		TABLE.setDatabase(mDatabase);
		final int updated = TABLE.update(URI, VALUES, null, null);
		assertEquals(1, updated);
	}

	public void testSQLTableInsert() {
		TABLE.setDatabase(mDatabase);
		final Uri uri = TABLE.insert(URI, VALUES);
		assertNotNull(uri);
	}

	public void testSQLTableBulkInsert() {
		TABLE.setDatabase(mDatabase);
		final ContentValues[] values = new ContentValues[] { VALUES };
		final int inserted = TABLE.bulkInsert(URI, values);
		assertEquals(1, inserted);
	}

	public void testSQLTableDelete() {
		testSQLTableInsert();
		
		TABLE.setDatabase(mDatabase);
		final int deleted = TABLE.delete(URI, null, null);
		assertEquals(1, deleted);
	}
	
	public void testSQLTableQueryThrowsExceptionWithoutDatabase() {
		try {
			TABLE.setDatabase(null);
			TABLE.query(null, null, null, null, null);
			Assert.fail();
		} catch(final IllegalStateException e) {
			assertEquals("Database is null.", e.getLocalizedMessage());
		}
	}
	
	public void testSQLTableUpdateThrowsExceptionWithoutDatabase() {
		try {
			TABLE.setDatabase(null);
			TABLE.update(null, null, null, null);
			Assert.fail();
		} catch(final IllegalStateException e) {
			assertEquals("Database is null.", e.getLocalizedMessage());
		}
	}
	
	public void testSQLTableInsertThrowsExceptionWithoutDatabase() {
		try {
			TABLE.setDatabase(null);
			TABLE.insert(null, null);
			Assert.fail();
		} catch(final IllegalStateException e) {
			assertEquals("Database is null.", e.getLocalizedMessage());
		}
	}
	
	public void testSQLTableBulkInsertThrowsExceptionWithoutDatabase() {
		try {
			TABLE.setDatabase(null);
			TABLE.bulkInsert(null, null);
			Assert.fail();
		} catch(final IllegalStateException e) {
			assertEquals("Database is null.", e.getLocalizedMessage());
		}
	}
	
	public void testSQLTableDeleteThrowsExceptionWithoutDatabase() {
		try {
			TABLE.setDatabase(null);
			TABLE.delete(null, null, null);
			Assert.fail();
		} catch(final IllegalStateException e) {
			assertEquals("Database is null.", e.getLocalizedMessage());
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
			mDatabase = null;
		}
	}
	

	// ====================================

	private static final class TestSQLTable extends SQLTable {

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
}
