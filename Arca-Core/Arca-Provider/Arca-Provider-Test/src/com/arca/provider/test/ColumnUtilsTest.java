package com.arca.provider.test;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.arca.provider.ColumnUtils;
import com.arca.provider.SQLiteTable;

public class ColumnUtilsTest extends AndroidTestCase {

	public void testColumnUtilsDefaultConversion() {
		final String columns = ColumnUtils.toString(TestSQLiteTable.Columns.class);
		final String expected = "_id INTEGER PRIMARY KEY AUTOINCREMENT,_state INTEGER DEFAULT 0";
		assertEquals(expected, columns);
	}
	
	
	// ====================================
	
	
	private static final class TestSQLiteTable extends SQLiteTable {
	
		public static interface Columns extends SQLiteTable.Columns {}

		@Override
		public void onCreate(final SQLiteDatabase db) {}

		@Override
		public void onDrop(final SQLiteDatabase db) {}
	}
}
