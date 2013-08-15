package com.xtreme.rest.test.junit.tests;

import java.util.Map.Entry;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import com.xtreme.rest.test.junit.mock.providers.TestContentProvider;
import com.xtreme.rest.test.junit.mock.providers.TestTable1;
import com.xtreme.rest.test.junit.mock.providers.TestTable2;
import com.xtreme.rest.test.junit.mock.providers.TestView;

public class RestContentProviderTest extends ProviderTestCase2<TestContentProvider> {

	private static final int WRONG_ITEM_ID = 1239183702;

	private MockContentResolver mMockResolver;
	private SQLiteDatabase mDatabase;

	public RestContentProviderTest() {
		super(TestContentProvider.class, TestContentProvider.AUTHORITY);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		mMockResolver = getMockContentResolver();
		mDatabase = getProvider().getDatabase();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		getProvider().destroyDatabase();
	}

	public void testInitilization() {
		assertNotNull(mDatabase);
	}

	public void testTableQuery() {
		assertTableIsEmpty();
		
		final ContentValues[] values = TestTable1.CONTENT_ARRAY;
		
		bulkInsertIntoTable(values);
		
		final ContentValues testValue = values[0];
		final int testId = testValue.getAsInteger(TestTable1.Columns.ID);

		// ======== TEST QUERY ITEM ID ==============
		
		final Cursor idCursor = queryItemFromTable(testId);
		assertNotNull(idCursor);
		assertTrue(idCursor.getCount() == 1);
		assertTrue(idCursor.moveToFirst());
		assertValuesEqualsCursor(testValue, idCursor);
		idCursor.close();

		// ======== TEST QUERY WRONG ITEM ID ==============
		
		final Cursor wrongIdCursor = queryItemFromTable(WRONG_ITEM_ID);
		assertNotNull(wrongIdCursor);
		assertTrue(wrongIdCursor.getCount() == 0);
		wrongIdCursor.close();
		
		// ======== TEST QUERY ALL ITEMS ==============
		
		final Cursor allCursor = queryAllItemsFromTable();
		assertNotNull(allCursor);
		assertTrue(allCursor.getCount() == values.length);

		for (int i = 0; i < values.length; i++) {
			assertTrue(allCursor.moveToPosition(i));
			assertValuesEqualsCursor(values[i], allCursor);
		}

		allCursor.close();
	}

	public void testTableInsert() {
		assertTableIsEmpty();

		final ContentValues values = TestTable1.CONTENT_VALUES;

		// ======== TEST INSERT ITEM ==============
		
		final Uri firstUri = insertIntoTable(values);
		final long firstId = ContentUris.parseId(firstUri);
		assertNotNull(firstUri);
		assertTrue(firstId > 0);

		final Cursor firstCursor = queryAllItemsFromTable();
		assertTrue(firstCursor.getCount() == 1);
		assertTrue(firstCursor.moveToFirst());
		assertCursorEqualsValues(firstCursor, values);
		firstCursor.close();
		
		// ======== TEST INSERT SAME ITEM ==============
		
		/**
		 * Note: this results in only one item left in 
		 * the database because of our unique constraint. 
		 */
		
		final Uri secondUri = insertIntoTable(values);
		final long secondId = ContentUris.parseId(secondUri);
		assertNotNull(secondUri);
		assertTrue(secondId > 0);
		
		final Cursor secondCursor = queryAllItemsFromTable();
		assertTrue(secondCursor.getCount() == 1);
		assertTrue(secondCursor.moveToFirst());
		assertCursorEqualsValues(secondCursor, values);
		secondCursor.close();
	}

	public void testTableBulkInsert() {
		assertTableIsEmpty();

		final ContentValues[] values = TestTable1.CONTENT_ARRAY;

		// ======== TEST BULK INSERT ITEMS ==============
		
		final long firstCount = bulkInsertIntoTable(values);
		assertTrue(firstCount == values.length);

		final Cursor firstCursor = queryAllItemsFromTable();
		assertTrue(firstCursor.getCount() == values.length);

		for (int i = 0; i < values.length; i++) {
			assertTrue(firstCursor.moveToPosition(i));
			assertCursorEqualsValues(firstCursor, values[i]);
		}

		firstCursor.close();
		
		// ======== TEST BULK INSERT SAME ITEMS ==============
		
		/**
		 * Note: this results in the same items left in 
		 * the database because of our unique constraint. 
		 */
		
		final long secondCount = bulkInsertIntoTable(values);
		assertTrue(secondCount == values.length);

		final Cursor secondCursor = queryAllItemsFromTable();
		assertTrue(secondCursor.getCount() == values.length);

		for (int i = 0; i < values.length; i++) {
			assertTrue(secondCursor.moveToPosition(i));
			assertCursorEqualsValues(secondCursor, values[i]);
		}

		secondCursor.close();
	}

	public void testTableUpdate() {
		assertTableIsEmpty();

		final ContentValues[] originalValues = TestTable1.CONTENT_ARRAY;
		final ContentValues fullyUpdatedValues = TestTable1.UPDATE_VALUES_FULL;
		final ContentValues partiallyUpdatedValues = TestTable1.UPDATE_VALUES_PARTIAL;
		
		final ContentValues testValue = originalValues[0];
		final int testId = testValue.getAsInteger(TestTable1.Columns.ID);
		
		// ======== TEST NO ITEM TO UPDATE ==============
		
		final long emptyUpdatedCount = updateItemFromTable(partiallyUpdatedValues, testId);
		assertTrue(emptyUpdatedCount == 0);
		
		bulkInsertIntoTable(originalValues);
		
		// ======== TEST PARTIAL UPDATE OF 1 ITEM ==============
		
		final long itemPartiallyUpdatedCount = updateItemFromTable(partiallyUpdatedValues, testId);
		assertTrue(itemPartiallyUpdatedCount == 1);

		final Cursor itemPartiallyUpdateCursor = queryItemFromTable(testId);
		assertTrue(itemPartiallyUpdateCursor.moveToFirst());
		assertCursorEqualsValues(itemPartiallyUpdateCursor, partiallyUpdatedValues);
		itemPartiallyUpdateCursor.close();

		// ======== TEST FULL UPDATE OF 1 ITEM ==============
		
		final long itemFullyUpdatedCount = updateItemFromTable(fullyUpdatedValues, testId);
		assertTrue(itemFullyUpdatedCount == 1);

		final Cursor itemFullyUpdatedCursor = queryItemFromTable(testId);
		assertTrue(itemFullyUpdatedCursor.moveToFirst());
		assertCursorEqualsValues(itemFullyUpdatedCursor, fullyUpdatedValues);
		itemFullyUpdatedCursor.close();
		
		// ======== TEST PARTIAL UPDATE OF ALL ITEMS ==============

		final long allPartiallyUpdatedCount = updateAllItemsFromTable(partiallyUpdatedValues);
		assertTrue(allPartiallyUpdatedCount == originalValues.length);

		final Cursor allPartiallyUpdatedCursor = queryAllItemsFromTable();
		for (int i = 0; i < originalValues.length; i++) {
			assertTrue(allPartiallyUpdatedCursor.moveToPosition(i));
			assertCursorEqualsValues(allPartiallyUpdatedCursor, partiallyUpdatedValues);
		}
		
		allPartiallyUpdatedCursor.close();
		
		// ======== TEST FULL UPDATE OF ALL ITEMS ==============
		
		/**
		 * Note: this results in only one item left in 
		 * the database because of our unique constraint. 
		 */

		final long allFullyUpdatedCount = updateAllItemsFromTable(fullyUpdatedValues);
		assertTrue(allFullyUpdatedCount == originalValues.length);
		
		final Cursor allFullyUpdatedCursor = queryAllItemsFromTable();
		assertTrue(allFullyUpdatedCursor.getCount() == 1);
		assertTrue(allFullyUpdatedCursor.moveToFirst());
		assertCursorEqualsValues(allFullyUpdatedCursor, fullyUpdatedValues);
		allFullyUpdatedCursor.close();
	}

	public void testTableDelete() {
		assertTableIsEmpty();

		final ContentValues[] originalValues = TestTable1.CONTENT_ARRAY;

		final ContentValues testValue = originalValues[0];
		final int testId = testValue.getAsInteger(TestTable1.Columns.ID);
		
		// ======== TEST DELETE WHEN EMPTY ==============
		
		final long emptyDeleted = deleteItemFromTable(testId);
		assertTrue(emptyDeleted == 0);
		
		bulkInsertIntoTable(originalValues);
		
		// ======== TEST DELETE ITEM ID ==============
		
		final long idDeletedCount = deleteItemFromTable(testId);
		assertTrue(idDeletedCount == 1);

		final Cursor idCursor = queryItemFromTable(testId);
		assertNotNull(idCursor);
		assertTrue(idCursor.getCount() == 0);
		idCursor.close();
		
		// ======== TEST DELETE WRONG ITEM ID ==============
		
		final long wrongIdDeletedCount = deleteItemFromTable(WRONG_ITEM_ID);
		assertTrue(wrongIdDeletedCount == 0);
		
		// ======== TEST DELETE ALL ITEMS ==============
		
		final long deleteCount = deleteAllItemsFromTable();
		assertTrue(deleteCount == (originalValues.length - idDeletedCount));

		assertTableIsEmpty();
	}

	
	// ================================================

	
	
	public void testViewQuery() {
		assertViewIsEmpty();
		
		final ContentValues[] values1 = TestTable1.CONTENT_ARRAY;
		final ContentValues[] values2 = TestTable2.CONTENT_ARRAY;

		bulkInsertIntoTable(TestContentProvider.TEST_TABLE1_URI, values1);
		bulkInsertIntoTable(TestContentProvider.TEST_TABLE2_URI, values2);
		
		final ContentValues[] values = TestView.CONTENT_ARRAY;

		final ContentValues testValue = values[0];
		final int testId = testValue.getAsInteger(TestTable1.Columns.ID);

		// ======== TEST QUERY ITEM ID ==============
		
		final Cursor idCursor = queryItemFromView(testId);
		assertNotNull(idCursor);
		assertTrue(idCursor.getCount() == 1);
		assertTrue(idCursor.moveToFirst());
		assertValuesEqualsCursor(testValue, idCursor);
		idCursor.close();
		
		// ======== TEST QUERY WRONG ITEM ID ==============
		
		final Cursor wrongIdCursor = queryItemFromView(WRONG_ITEM_ID);
		assertNotNull(wrongIdCursor);
		assertTrue(wrongIdCursor.getCount() == 0);
		wrongIdCursor.close();
		
		// ======== TEST QUERY ALL ITEMS ==============
		
		final Cursor allCursor = queryAllItemsFromView();
		assertNotNull(allCursor);
		assertTrue(allCursor.getCount() == values.length);

		for (int i = 0; i < values.length; i++) {
			assertTrue(allCursor.moveToPosition(i));
			assertValuesEqualsCursor(values[i], allCursor);
		}

		allCursor.close();
	}

	public void testViewInsertThrowsException() {
		try {
			insertContentValuesIntoView();
			fail();
		} catch (final UnsupportedOperationException e) {
			assertNotNull(e);
		}
	}

	public void testViewBulkInsertThrowsException() {
		try {
			bulkInsertContentArrayIntoView();
			fail();
		} catch (final UnsupportedOperationException e) {
			assertNotNull(e);
		}
	}

	public void testViewDeleteThrowsException() {
		try {
			deleteAllItemsFromView();
			fail();
		} catch (final UnsupportedOperationException e) {
			assertNotNull(e);
		}
	}

	public void testViewUpdateThrowsException() {
		try {
			updateAllItemsFromView();
			fail();
		} catch (final UnsupportedOperationException e) {
			assertNotNull(e);
		}
	}

	
	// ================================================

	
	private MockContentResolver getContentResolver() {
		return mMockResolver;
	}

	private void assertTableIsEmpty() {
		final Cursor cursor = queryAllItemsFromTable();
		assertNotNull(cursor);
		assertTrue(cursor.getCount() == 0);
		cursor.close();
	}

	private Cursor queryAllItemsFromTable() {
		final Uri uri = TestContentProvider.TEST_TABLE1_URI;
		final Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		return cursor;
	}

	private Cursor queryItemFromTable(final long id) {
		final String where = TestTable1.Columns.ID + " = ?";
		final String[] whereArgs = new String[] { String.valueOf(id) };
		final Uri uri = TestContentProvider.TEST_TABLE1_URI;
		final Cursor cursor = getContentResolver().query(uri, null, where, whereArgs, null);
		return cursor;
	}

	private Uri insertIntoTable(final ContentValues values) {
		final Uri uri = TestContentProvider.TEST_TABLE1_URI;
		final Uri inserted = getContentResolver().insert(uri, values);
		return inserted;
	}

	private long bulkInsertIntoTable(final ContentValues[] values) {
		final Uri uri = TestContentProvider.TEST_TABLE1_URI;
		return bulkInsertIntoTable(uri, values);
	}
	
	private long bulkInsertIntoTable(final Uri uri, final ContentValues[] values) {
		final long count = getContentResolver().bulkInsert(uri, values);
		return count;
	}

	private long updateAllItemsFromTable(final ContentValues values) {
		final Uri uri = TestContentProvider.TEST_TABLE1_URI;
		final long count = getContentResolver().update(uri, values, null, null);
		return count;
	}

	private long updateItemFromTable(final ContentValues values, final int id) {
		final String where = TestTable1.Columns.ID + " = ?";
		final String[] whereArgs = new String[] { String.valueOf(id) };
		final Uri uri = TestContentProvider.TEST_TABLE1_URI;
		final long count = getContentResolver().update(uri, values, where, whereArgs);
		return count;
	}

	private long deleteAllItemsFromTable() {
		final Uri uri = TestContentProvider.TEST_TABLE1_URI;
		final long count = getContentResolver().delete(uri, null, null);
		return count;
	}

	private long deleteItemFromTable(final int id) {
		final String where = TestTable1.Columns.ID + " = ?";
		final String[] whereArgs = new String[] { String.valueOf(id) };
		final Uri uri = TestContentProvider.TEST_TABLE1_URI;
		final long count = getContentResolver().delete(uri, where, whereArgs);
		return count;
	}
	
	
	// ================================================
	
	
	private void assertViewIsEmpty() {
		final Cursor cursor = queryAllItemsFromView();
		assertNotNull(cursor);
		assertTrue(cursor.getCount() == 0);
		cursor.close();
	}

	private Cursor queryAllItemsFromView() {
		final Uri uri = TestContentProvider.TEST_VIEW_URI;
		final Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		return cursor;
	}
	
	private Cursor queryItemFromView(final int id) {
		final String where = TestTable1.Columns.ID + " = ?";
		final String[] whereArgs = new String[] { String.valueOf(id) };
		final Uri uri = TestContentProvider.TEST_VIEW_URI;
		final Cursor cursor = getContentResolver().query(uri, null, where, whereArgs, null);
		return cursor;
	}

	private Uri insertContentValuesIntoView() {
		final Uri uri = TestContentProvider.TEST_VIEW_URI;
		final Uri inserted = getContentResolver().insert(uri, null);
		return inserted;
	}

	private long bulkInsertContentArrayIntoView() {
		final Uri uri = TestContentProvider.TEST_VIEW_URI;
		final long count = getContentResolver().bulkInsert(uri, null);
		return count;
	}

	private long deleteAllItemsFromView() {
		final Uri uri = TestContentProvider.TEST_VIEW_URI;
		final long count = getContentResolver().delete(uri, null, null);
		return count;
	}

	private long updateAllItemsFromView() {
		final Uri uri = TestContentProvider.TEST_VIEW_URI;
		final long count = getContentResolver().update(uri, null, null, null);
		return count;
	}

	
	// ================================================
	
	
	private static void assertValuesEqualsCursor(final ContentValues values, final Cursor cursor) {
		for (final String columnName : cursor.getColumnNames()) {
			if (columnName.contains(BaseColumns._ID)) {
				final long actual = cursor.getLong(cursor.getColumnIndex(columnName));
				assertTrue(actual >= 0);
			} else {
				final String expected = values.getAsString(columnName);
				final String actual = cursor.getString(cursor.getColumnIndex(columnName));
				assertTrue((actual == expected) || actual.equals(expected));
			}
		}
	}

	private static void assertCursorEqualsValues(final Cursor cursor, final ContentValues values) {
		for (final Entry<String, Object> entry : values.valueSet()) {
			final String columnName = entry.getKey();
			final String expected = String.valueOf(entry.getValue());
			final String actual = cursor.getString(cursor.getColumnIndex(columnName));
			assertTrue(actual.equals(expected));
		}
	}

}
