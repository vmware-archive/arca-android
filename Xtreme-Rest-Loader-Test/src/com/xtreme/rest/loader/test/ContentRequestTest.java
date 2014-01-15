package com.xtreme.rest.loader.test;

import java.util.Arrays;

import android.net.Uri;
import android.os.Parcel;
import android.test.AndroidTestCase;

import com.xtreme.rest.loader.ContentRequest;

public class ContentRequestTest extends AndroidTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		
	}
	
	public void test() {
		
	}
	
	// =============================================
	
	
	public void testContentRequestParcelableDescribeContents() {
		final Uri uri = Uri.parse("http://empty");
		final ContentRequest request = new ContentRequest(uri);
		assertEquals(0, request.describeContents());
	}
	
	public void testContentRequestParcelableCreatorArray() {
		final ContentRequest[] request = ContentRequest.CREATOR.newArray(1);
		assertEquals(1, request.length);
	}
	
	public void testContentRequestParcelableCreator() {
		final Uri uri = Uri.parse("http://empty");
		final String[] projection = { "_id" };
		final String where = "test = ?";
		final String[] whereArgs = { "true" };
		final String sortOrder = "_id desc";
		final boolean forceUpdate = true;
		
		final ContentRequest request = new ContentRequest(uri);
		request.setProjection(projection);
		request.setWhere(where, whereArgs);
		request.setSortOrder(sortOrder);
		request.setForceUpdate(forceUpdate);
		
		final Parcel parcel = Parcel.obtain();
		request.writeToParcel(parcel, 0);
		parcel.setDataPosition(0);
		
		final ContentRequest parceled = ContentRequest.CREATOR.createFromParcel(parcel);
		assertEquals(uri, parceled.getContentUri());
		assertTrue(Arrays.deepEquals(projection, parceled.getProjection()));
		assertEquals(where, parceled.getWhereClause());
		assertTrue(Arrays.deepEquals(whereArgs, parceled.getWhereArgs()));
		assertEquals(sortOrder, parceled.getSortOrder());
		assertEquals(forceUpdate, parceled.shouldForceUpdate());
		
		parcel.recycle();
	}
}
