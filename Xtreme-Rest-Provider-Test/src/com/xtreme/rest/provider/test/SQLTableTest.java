package com.xtreme.rest.provider.test;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.xtreme.rest.provider.DatabaseConfiguration;
import com.xtreme.rest.provider.DatabaseConfiguration.DefaultDatabaseConfiguration;
import com.xtreme.rest.provider.DatabaseHelper;
import com.xtreme.rest.provider.SQLDataset;
import com.xtreme.rest.provider.SQLTable;

public class SQLTableTest extends AndroidTestCase {

	private SQLiteDatabase mDatabase;
	private static final TestSQLTable TABLE = new TestSQLTable();
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		final Collection<SQLDataset> datasets = new ArrayList<SQLDataset>();
		datasets.add(TABLE);
		
		final DatabaseConfiguration configuration = new DefaultDatabaseConfiguration(getContext());
		final DatabaseHelper helper = DatabaseHelper.create(getContext(), configuration, datasets);
		mDatabase = helper.getWritableDatabase();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		
		mDatabase.close();
	}
	
	public void testSQLTableQuery() {
		TABLE.setDatabase(mDatabase);
		final Cursor cursor = TABLE.query(null, null, null, null, null);
		assertNotNull(cursor);
		cursor.getColumnIndexOrThrow("id");
		cursor.close();
	}

	public void testSQLTableUpdate() {
		TABLE.setDatabase(mDatabase);
		final ContentValues values = new ContentValues();
		values.put("id", "test");
		final int updated = TABLE.update(null, values, null, null);
		assertEquals(0, updated);
	}

	public void testSQLTableInsert() {

	}

	public void testSQLTableBulkInsert() {

	}

	public void testSQLTableDelete() {

	}
	
	public void testSQLTableQueryThrowsExceptionWithoutDatabase() {
		try {
			TABLE.query(null, null, null, null, null);
			Assert.fail();
		} catch(final Exception e) {
			assertNotNull(e);
		}
	}
	
	public void testSQLTableUpdateThrowsExceptionWithoutDatabase() {
		try {
			TABLE.update(null, null, null, null);
			Assert.fail();
		} catch(final Exception e) {
			assertNotNull(e);
		}
	}
	
	public void testSQLTableInsertThrowsExceptionWithoutDatabase() {
		try {
			TABLE.insert(null, null);
			Assert.fail();
		} catch(final Exception e) {
			assertNotNull(e);
		}
	}
	
	public void testSQLTableBulkInsertThrowsExceptionWithoutDatabase() {
		try {
			TABLE.bulkInsert(null, null);
			Assert.fail();
		} catch(final Exception e) {
			assertNotNull(e);
		}
	}
	
	public void testSQLTableDeleteThrowsExceptionWithoutDatabase() {
		try {
			TABLE.delete(null, null, null);
			Assert.fail();
		} catch(final Exception e) {
			assertNotNull(e);
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
