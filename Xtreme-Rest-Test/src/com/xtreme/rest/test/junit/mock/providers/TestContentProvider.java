package com.xtreme.rest.test.junit.mock.providers;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.xtreme.rest.providers.RestContentProvider;

public class TestContentProvider extends RestContentProvider {

	public static final String AUTHORITY = "com.xtreme.rest.mock.TestContentProvider";
	
	public static final Uri TEST_TABLE1_URI = Uri.parse("content://" + AUTHORITY + "/" + Paths.TEST_TABLE1);
	public static final Uri TEST_TABLE2_URI = Uri.parse("content://" + AUTHORITY + "/" + Paths.TEST_TABLE2);
	public static final Uri TEST_VIEW_URI = Uri.parse("content://" + AUTHORITY + "/" + Paths.TEST_VIEW);
	
	private static final class Paths {
		private static final String TEST_TABLE1 = "test_table1";
		private static final String TEST_TABLE2 = "test_table2";
		private static final String TEST_VIEW = "test_view";
	}
	
	@Override
	public SQLiteDatabase getDatabase() {
		return super.getDatabase();
	}
	
	@Override
	public void destroyDatabase() {
		super.destroyDatabase();
	}
	
	@Override
	public boolean onCreate() {
		registerDataset(AUTHORITY, Paths.TEST_TABLE1, TestTable1.class);
		registerDataset(AUTHORITY, Paths.TEST_TABLE2, TestTable2.class);
		registerDataset(AUTHORITY, Paths.TEST_VIEW, TestView.class);
		return true;
	}

}
