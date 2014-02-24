package com.arca.dispatcher.test;

import java.util.Arrays;

import android.net.Uri;
import android.os.Parcel;
import android.test.AndroidTestCase;

import com.arca.dispatcher.Delete;

public class DeleteTest extends AndroidTestCase {
	
	
	public void testDeleteParcelableDescribeContents() {
		final Uri uri = Uri.parse("content://empty");
		final Delete request = new Delete(uri);
		assertEquals(0, request.describeContents());
	}
	
	public void testDeleteParcelableCreatorArray() {
		final Delete[] request = Delete.CREATOR.newArray(1);
		assertEquals(1, request.length);
	}
	
	public void testDeleteParcelableCreator() {
		final Uri uri = Uri.parse("content://empty");
		final String where = "test = ?";
		final String[] whereArgs = { "true" };
		
		final Delete request = new Delete(uri);
		request.setWhere(where, whereArgs);
		
		final Parcel parcel = Parcel.obtain();
		request.writeToParcel(parcel, 0);
		parcel.setDataPosition(0);
		
		final Delete parceled = Delete.CREATOR.createFromParcel(parcel);
		assertEquals(uri, parceled.getUri());
		assertEquals(where, parceled.getWhereClause());
		assertTrue(Arrays.deepEquals(whereArgs, parceled.getWhereArgs()));
		
		parcel.recycle();
	}
}
