package com.arca.provider.test;

import java.util.ArrayList;
import java.util.Collection;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.arca.provider.DatabaseConfiguration;
import com.arca.provider.Column.Type;
import com.arca.provider.DatabaseConfiguration.DefaultDatabaseConfiguration;
import com.arca.provider.Column;
import com.arca.provider.DatabaseHelper;
import com.arca.provider.SQLiteDataset;
import com.arca.provider.SQLiteTable;

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
			public static final Column ID = Type.TEXT.newColumn("id");
		}
		
		@Override
		public void setDatabase(final SQLiteDatabase db) {
			super.setDatabase(db);
		}
	}
}
