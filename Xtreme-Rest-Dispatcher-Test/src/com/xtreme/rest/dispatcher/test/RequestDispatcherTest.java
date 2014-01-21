package com.xtreme.rest.dispatcher.test;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Build;
import android.test.AndroidTestCase;
import android.test.mock.MockContentProvider;
import android.test.mock.MockContentResolver;

import com.xtreme.rest.dispatcher.ContentResult;
import com.xtreme.rest.dispatcher.Delete;
import com.xtreme.rest.dispatcher.Insert;
import com.xtreme.rest.dispatcher.ModernRequestDispatcher;
import com.xtreme.rest.dispatcher.Query;
import com.xtreme.rest.dispatcher.RequestDispatcher;
import com.xtreme.rest.dispatcher.RequestExecutor;
import com.xtreme.rest.dispatcher.RequestExecutor.DefaultRequestExecutor;
import com.xtreme.rest.dispatcher.Update;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RequestDispatcherTest extends AndroidTestCase {
	
	private static final String AUTHORITY = "com.test";
	
	private static final Uri TEST_URI = Uri.parse("content://" + AUTHORITY + "/empty");

	public void testSychronousQuery() {
		final Query query = new Query(TEST_URI);
		final ContentResult<Cursor> response = getDispatcher().execute(query);
		final Cursor result = response.getResult();
		assertNotNull(result);
		result.close();
	}
	
	public void testSychronousInsert() {
		final ContentValues values = new ContentValues();
		final Insert insert = new Insert(TEST_URI, values);
		final ContentResult<Integer> response = getDispatcher().execute(insert);
		final Integer count = response.getResult();
		assertEquals(Integer.valueOf(1), count);
	}
	
	public void testSychronousUpdate() {
		final ContentValues values = new ContentValues();
		final Update update = new Update(TEST_URI, values);
		final ContentResult<Integer> response = getDispatcher().execute(update);
		final Integer count = response.getResult();
		assertEquals(Integer.valueOf(1), count);
	}
	
	public void testSychronousDelete() {
		final Delete delete = new Delete(TEST_URI);
		final ContentResult<Integer> response = getDispatcher().execute(delete);
		final Integer count = response.getResult();
		assertEquals(Integer.valueOf(1), count);
	}
	
	private RequestDispatcher getDispatcher() {
		final MockContentResolver resolver = new MockContentResolver();
		resolver.addProvider(AUTHORITY, mProvider);
		final RequestExecutor executor = new DefaultRequestExecutor(resolver);
		return new ModernRequestDispatcher(executor, getContext(), null);
	}
	
	private final MockContentProvider mProvider = new MockContentProvider(getContext()) {
		@Override
		public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
			return new MatrixCursor(new String[] {});
		};
		
		@Override
		public int bulkInsert(final Uri uri, final ContentValues[] values) {
			return values.length;
		};
		
		@Override
		public Uri insert(final Uri uri, final ContentValues values) {
			return uri;
		};
		
		@Override
		public int delete(Uri uri, String selection, String[] selectionArgs) {
			return 1;
		};
		
		@Override
		public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
			return 1;
		};
	};
}
