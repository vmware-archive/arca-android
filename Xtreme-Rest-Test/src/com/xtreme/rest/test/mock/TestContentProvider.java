package com.xtreme.rest.test.mock;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.xtreme.rest.providers.Configuration;
import com.xtreme.rest.providers.Configuration.DefaultConfiguration;
import com.xtreme.rest.providers.RestContentProvider;

public class TestContentProvider extends RestContentProvider {
	
	public static final String AUTHORITY = "com.xtreme.rest.mock.TestContentProvider";
	
	public static final class Uris {
		public static final Uri TEST_TABLE1 = Uri.parse("content://" + AUTHORITY + "/" + Paths.TEST_TABLE1);
		public static final Uri TEST_TABLE2 = Uri.parse("content://" + AUTHORITY + "/" + Paths.TEST_TABLE2);
		public static final Uri TEST_VIEW = Uri.parse("content://" + AUTHORITY + "/" + Paths.TEST_VIEW);
	}
	
	private static final class Paths {
		private static final String TEST_TABLE1 = "test_table1";
		private static final String TEST_TABLE2 = "test_table2";
		private static final String TEST_VIEW = "test_view";
	}
	
	@Override
	public boolean onCreate() {
		registerDataset(AUTHORITY, Paths.TEST_TABLE1, TestTable1.class);
		registerDataset(AUTHORITY, Paths.TEST_TABLE2, TestTable2.class);
		registerDataset(AUTHORITY, Paths.TEST_VIEW, TestView.class);
		return true;
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
	public Configuration onCreateConfiguration() {
		return new TestConfiguration(getContext());
	}
	
	public static final class TestConfiguration extends DefaultConfiguration {

		private static final String DATABASE_NAME = "test.db";
		private static final int DATABASE_VERSION = 1;
		
		public TestConfiguration(Context context) {
			super(context);
		}

		@Override
		public String getDatabaseName() {
			return DATABASE_NAME;
		}
		
		@Override
		public int getDatabaseVersion() {
			return DATABASE_VERSION;
		}
	}
}
