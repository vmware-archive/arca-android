package com.arca.provider.test;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import android.annotation.TargetApi;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.test.AndroidTestCase;

import com.arca.provider.Column;
import com.arca.provider.Column.Type;
import com.arca.provider.DatabaseConfiguration;
import com.arca.provider.DatabaseConfiguration.DefaultDatabaseConfiguration;
import com.arca.provider.DatabaseHelper;
import com.arca.provider.SQLiteDataset;
import com.arca.provider.SQLiteTable;
import com.arca.provider.SQLiteView;
import com.arca.provider.Select;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DatabaseHelperTest extends AndroidTestCase {

	
	public void testDatabaseHelperUpgradeDataset() {
		final AssertionLatch latch = new AssertionLatch(1);
		final ArrayList<SQLiteDataset> datasets = new ArrayList<SQLiteDataset>();
		datasets.add(new TestSQLiteTable() {
			
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
		final ArrayList<SQLiteDataset> datasets = new ArrayList<SQLiteDataset>();
		datasets.add(new TestSQLiteTable() {
			
			@Override
			public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
				latch.countDown();
				
				assertEquals(0, oldVersion);
				assertEquals(1, newVersion);
			}
			
		});
		datasets.add(new TestSQLiteView() {
			
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
		final ArrayList<SQLiteDataset> datasets = new ArrayList<SQLiteDataset>();
		datasets.add(new TestSQLiteTable() {

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
		final ArrayList<SQLiteDataset> datasets = new ArrayList<SQLiteDataset>();
		datasets.add(new TestSQLiteTable() {

			@Override
			public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				latch.countDown();
				
				assertEquals(1, oldVersion);
				assertEquals(0, newVersion);
			}
			
		});
		datasets.add(new TestSQLiteView() {

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
	
	
	public static class TestSQLiteTable extends SQLiteTable {
		
		public static interface Columns {
			public static final Column ID = Type.TEXT.newColumn("id");
		}
	}
	
	public static class TestSQLiteView extends SQLiteView {

		@Select("SELECT * FROM (TestSQLiteTable)")
		public static interface Columns extends TestSQLiteTable.Columns{}
		
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
