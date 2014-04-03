package com.arca.provider.test;

import android.test.AndroidTestCase;

import com.arca.provider.DatasetUtils;
import com.arca.provider.SQLiteTable;

public class ColumnUtilsTest extends AndroidTestCase {

	public void testColumnUtilsDefaultConversion() {
		final String columns = DatasetUtils.getColumns(new TestSQLiteTable());
		final String expected = "_id INTEGER PRIMARY KEY AUTOINCREMENT,_state INTEGER DEFAULT 0";
		assertEquals(expected, columns);
	}
	
	
	// ====================================
	
	
	public static final class TestSQLiteTable extends SQLiteTable {
	
		public static interface Columns extends SQLiteTable.Columns {}
	}
}