package com.xtreme.rest.provider.test;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.xtreme.rest.provider.DatabaseConfiguration;
import com.xtreme.rest.provider.DatabaseConfiguration.DefaultDatabaseConfiguration;
import com.xtreme.rest.provider.DatabaseHelper;
import com.xtreme.rest.provider.SQLDataset;
import com.xtreme.rest.provider.SQLTable;
import com.xtreme.rest.provider.SQLView;

public class DatabaseHelperTest extends AndroidTestCase {

	
	public void testDatabaseHelperUpgradeDataset() {
		final AssertionLatch latch = new AssertionLatch(1);
		final ArrayList<SQLDataset> datasets = new ArrayList<SQLDataset>();
		datasets.add(new TestSQLTable() {
			
			@Override
			public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
				latch.countDown();
				
				assertEquals(0, oldVersion);
				assertEquals(1, newVersion);
			}
			
		});
		final DatabaseConfiguration config = new DefaultDatabaseConfiguration(getContext());
		final DatabaseHelper helper = DatabaseHelper.create(getContext(), config, datasets);
		helper.onUpgrade(null, 0, 1);
		latch.assertComplete();
	}
	
	public void testDatabaseHelperUpgradeMultipleDatasets() {
		final AssertionLatch latch = new AssertionLatch(2);
		final ArrayList<SQLDataset> datasets = new ArrayList<SQLDataset>();
		datasets.add(new TestSQLTable() {
			
			@Override
			public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
				latch.countDown();
				
				assertEquals(0, oldVersion);
				assertEquals(1, newVersion);
			}
			
		});
		datasets.add(new TestSQLView() {
			
			@Override
			public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
				latch.countDown();
				
				assertEquals(0, oldVersion);
				assertEquals(1, newVersion);
			}
			
		});
		final DatabaseConfiguration config = new DefaultDatabaseConfiguration(getContext());
		final DatabaseHelper helper = DatabaseHelper.create(getContext(), config, datasets);
		helper.onUpgrade(null, 0, 1);
		latch.assertComplete();
	}
	
	
	public void testDatabaseHelperDowngradeDataset() {
		final AssertionLatch latch = new AssertionLatch(1);
		final ArrayList<SQLDataset> datasets = new ArrayList<SQLDataset>();
		datasets.add(new TestSQLTable() {

			@Override
			public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				latch.countDown();
				
				assertEquals(1, oldVersion);
				assertEquals(0, newVersion);
			}
			
		});
		final DatabaseConfiguration config = new DefaultDatabaseConfiguration(getContext());
		final DatabaseHelper helper = DatabaseHelper.create(getContext(), config, datasets);
		helper.onDowngrade(null, 1, 0);
		latch.assertComplete();
	}
	
	public void testDatabaseHelperDowngradeMultipleDatasets() {
		final AssertionLatch latch = new AssertionLatch(2);
		final ArrayList<SQLDataset> datasets = new ArrayList<SQLDataset>();
		datasets.add(new TestSQLTable() {

			@Override
			public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				latch.countDown();
				
				assertEquals(1, oldVersion);
				assertEquals(0, newVersion);
			}
			
		});
		datasets.add(new TestSQLView() {

			@Override
			public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				latch.countDown();
				
				assertEquals(1, oldVersion);
				assertEquals(0, newVersion);
			}
			
		});
		final DatabaseConfiguration config = new DefaultDatabaseConfiguration(getContext());
		final DatabaseHelper helper = DatabaseHelper.create(getContext(), config, datasets);
		helper.onDowngrade(null, 1, 0);
		latch.assertComplete();
	}
	
	
	// =======================================
	
	
	private static class TestSQLTable extends SQLTable {
		
		@Override
		public void onCreate(final SQLiteDatabase db) {
			db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (id TEXT);", getName()));
		}
		
		@Override
		public void onDrop(final SQLiteDatabase db) {
			db.execSQL(String.format("DROP TABLE IF EXISTS %s;", getName()));
		}
	}
	
	private static class TestSQLView extends SQLView {

		@Override
		public void onCreate(final SQLiteDatabase db) {
			db.execSQL(String.format("CREATE VIEW IF NOT EXISTS %s AS SELECT * FROM %s;", getName(), TestSQLTable.class.getSimpleName()));
		}
		
		@Override
		public void onDrop(final SQLiteDatabase db) {
			db.execSQL(String.format("DROP VIEW IF EXISTS %s;", getName()));
		}
		
	}
	
	
	// =======================================
	
	
	public class AssertionLatch extends CountDownLatch {

		public AssertionLatch(final int count) {
			super(count);
		}
		
		@Override
		public void countDown() {
			final long count = getCount();
			if (count == 0) {
				Assert.fail("This latch has already finished.");
			} else {
				super.countDown();
			}
		}
		
		public void assertComplete() {
			try {
				Assert.assertTrue(await(0, TimeUnit.SECONDS));
			} catch (final InterruptedException e) {
				Assert.fail();
			}
		}
	}
}
