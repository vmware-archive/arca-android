package com.xtreme.rest.provider.test;

import java.util.ArrayList;
import java.util.Collection;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.xtreme.rest.provider.DatabaseConfiguration;
import com.xtreme.rest.provider.DatabaseConfiguration.DefaultDatabaseConfiguration;
import com.xtreme.rest.provider.DatabaseHelper;
import com.xtreme.rest.provider.SQLDataset;
import com.xtreme.rest.provider.SQLTable;

public class SQLDatasetTest extends AndroidTestCase {

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
	
	public void testSQLDatasetUpgrade() {
		assertTableHasRecords();
		TABLE.onUpgrade(mDatabase, 0, 1);
		assertTableIsEmpty();
	}

	public void testSQLDatasetDowngrade() {
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
