package com.xtreme.rest.provider.test;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.xtreme.rest.provider.ColumnUtils;
import com.xtreme.rest.provider.SQLTable;

public class ColumnUtilsTest extends AndroidTestCase {

	public void testColumnUtilsDefaultConversion() {
		final String columns = ColumnUtils.toString(TestSQLTable.Columns.class);
		final String expected = "_id INTEGER PRIMARY KEY AUTOINCREMENT,_state INTEGER DEFAULT 0";
		assertEquals(expected, columns);
	}
	
	
	// ====================================
	
	
	private static final class TestSQLTable extends SQLTable {
	
		public static interface Columns extends SQLTable.Columns {}

		@Override
		public void onCreate(final SQLiteDatabase db) {}

		@Override
		public void onDrop(final SQLiteDatabase db) {}
	}
}
