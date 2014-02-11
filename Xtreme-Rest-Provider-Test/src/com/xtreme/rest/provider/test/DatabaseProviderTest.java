package com.xtreme.rest.provider.test;

import java.util.ArrayList;

import junit.framework.Assert;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.ProviderTestCase2;

import com.xtreme.rest.provider.DatabaseConfiguration;
import com.xtreme.rest.provider.DatabaseConfiguration.DefaultDatabaseConfiguration;
import com.xtreme.rest.provider.DatabaseProvider;
import com.xtreme.rest.provider.Dataset;
import com.xtreme.rest.provider.SQLTable;
import com.xtreme.rest.provider.test.DatabaseProviderTest.TestDatabaseProvider;
import com.xtreme.rest.provider.test.DatabaseProviderTest.TestDatabaseProvider.Uris;

public class DatabaseProviderTest extends ProviderTestCase2<TestDatabaseProvider> {

	public DatabaseProviderTest() {
		super(TestDatabaseProvider.class, TestDatabaseProvider.AUTHORITY);
	}
	
	public void testDatabaseExists() {
		final TestDatabaseProvider provider = getProvider();
		final SQLiteDatabase database = provider.getDatabase();
		assertNotNull(database);
	}
	
	public void testDatasetIsGivenTheDatabase() {
		final TestDatabaseProvider provider = getProvider();
		final Dataset dataset = provider.getDatasetOrThrowException(Uris.URI_1);
		final SQLiteDatabase database = ((TestTable1) dataset).getDatabase();
		assertNotNull(database);
	}
	
	public void testDatabaseApplyBatchNullOperations() {
		try {
			getProvider().applyBatch(null);
			Assert.fail();
		} catch (final Exception e) {
			assertNotNull(e);
		}
	}
	
	public void testDatabaseApplyBatchEmptyOperations() {
		try {
			final ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
			getProvider().applyBatch(operations);
		} catch (final Exception e) {
			Assert.fail();
		}
	}
	
	public void testDatabaseDefaultConfiguration() {
		final DatabaseProvider provider = new DatabaseProvider() {
			@Override 
			public boolean onCreate() { 
				return true; 
			}
		};
		final DatabaseConfiguration config = provider.onCreateDatabaseConfiguration();
		assertNotNull(config);
	}
	
	
	// ==================================
	
	
	public static class TestDatabaseProvider extends DatabaseProvider {

		public static final String AUTHORITY = "com.test.authority";
		
		private static final class Paths {
			public static final String PATH_1 = "path1";
			public static final String PATH_2 = "path2";
		}
		
		public static final class Uris {
			public static final Uri URI_1 = Uri.parse("content://" + AUTHORITY + "/" + Paths.PATH_1);
			public static final Uri URI_2 = Uri.parse("content://" + AUTHORITY + "/" + Paths.PATH_2);
		}
		
		@Override
		public DatabaseConfiguration onCreateDatabaseConfiguration() {
			return new TestDatabaseConfiguration(getContext());
		}
		
		@Override
		public boolean onCreate() {
			registerDataset(AUTHORITY, Paths.PATH_1, TestTable1.class);
			registerDataset(AUTHORITY, Paths.PATH_2, TestTable2.class);
			return true;
		}
		
		@Override
		public Dataset getDatasetOrThrowException(final Uri uri) {
			return super.getDatasetOrThrowException(uri);
		}
		
		@Override
		public SQLiteDatabase getDatabase() {
			return super.getDatabase();
		}
	}
	
	public static class TestDatabaseConfiguration extends DefaultDatabaseConfiguration {

		public TestDatabaseConfiguration(final Context context) {
			super(context);
		}

		@Override
		public String getDatabaseName() {
			return "test.db";
		}

		@Override
		public int getDatabaseVersion() {
			return 1;
		}
	}
	
	public static class TestTable1 extends SQLTable {

		@Override
		public void onCreate(final SQLiteDatabase db) {
			db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (id TEXT);", getName()));
		}
		
		@Override
		public void onDrop(final SQLiteDatabase db) {
			db.execSQL(String.format("DROP TABLE IF EXISTS %s;", getName()));
		}

		@Override
		public SQLiteDatabase getDatabase() {
			return super.getDatabase();
		}

	}
	
	public static class TestTable2 implements Dataset {

		@Override
		public Uri insert(final Uri uri, final ContentValues values) {
			return null;
		}

		@Override
		public int bulkInsert(final Uri uri, final ContentValues[] values) {
			return 0;
		}

		@Override
		public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
			return 0;
		}

		@Override
		public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
			return 0;
		}

		@Override
		public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
			return null;
		}
	}
}
