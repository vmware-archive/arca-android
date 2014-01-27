package com.xtreme.rest.provider.test.cases;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.xtreme.rest.provider.Dataset;
import com.xtreme.rest.provider.DatasetMatcher;
import com.xtreme.rest.provider.DatasetMatcher.DefaultMatcher;
import com.xtreme.rest.provider.SQLTable;

public class DefaultMatcherTest extends AndroidTestCase {

	private static final String AUTHORITY = "com.test.authority";

	private static final class Paths {
		public static final String PATH_1 = "path1";
		public static final String PATH_2 = "path2";
	}
	
	private static final class Uris {
		public static final Uri URI_1 = Uri.parse("content://" + AUTHORITY + "/" + Paths.PATH_1);
		public static final Uri URI_2 = Uri.parse("content://" + AUTHORITY + "/" + Paths.PATH_2);
	}

	public void testDatasetMatcherWithNoMatches() {
		final DatasetMatcher matcher = new DefaultMatcher();
		final Dataset dataset = matcher.matchDataset(Uris.URI_1);
		assertNull(dataset);
	}
	
	public void testDatasetMatcherWithIncorrectMatch() {
		final DatasetMatcher matcher = new DefaultMatcher();
		matcher.register(AUTHORITY, Paths.PATH_1, TestTable1.class);
		final Dataset dataset = matcher.matchDataset(Uris.URI_2);
		assertNull(dataset);
	}
	
	public void testDatasetMatcherWithCorrectMatch() {
		final DatasetMatcher matcher = new DefaultMatcher();
		matcher.register(AUTHORITY, Paths.PATH_1, TestTable1.class);
		final Dataset dataset = matcher.matchDataset(Uris.URI_1);
		assertNotNull(dataset);
	}
	
	public void testDatasetMatcherSameClassInstance() {
		final DatasetMatcher matcher = new DefaultMatcher();
		matcher.register(AUTHORITY, Paths.PATH_1, TestTable1.class);
		matcher.register(AUTHORITY, Paths.PATH_2, TestTable1.class);
		final Dataset dataset1 = matcher.matchDataset(Uris.URI_1);
		final Dataset dataset2 = matcher.matchDataset(Uris.URI_2);
		assertSame(dataset1, dataset2);
	}
	
	public void testDatasetMatcherDifferentClassInstances() {
		final DatasetMatcher matcher = new DefaultMatcher();
		matcher.register(AUTHORITY, Paths.PATH_1, TestTable1.class);
		matcher.register(AUTHORITY, Paths.PATH_2, TestTable2.class);
		final Dataset dataset1 = matcher.matchDataset(Uris.URI_1);
		final Dataset dataset2 = matcher.matchDataset(Uris.URI_2);
		assertNotSame(dataset1, dataset2);
	}
	
	public static class TestTable1 extends SQLTable {
		
		@Override
		public void onCreate(final SQLiteDatabase db) {}
		
		@Override
		public void onDrop(final SQLiteDatabase db) {}

	}
	
	public static class TestTable2 extends SQLTable {
		
		@Override
		public void onCreate(final SQLiteDatabase db) {}
		
		@Override
		public void onDrop(final SQLiteDatabase db) {}

	}
	
}
