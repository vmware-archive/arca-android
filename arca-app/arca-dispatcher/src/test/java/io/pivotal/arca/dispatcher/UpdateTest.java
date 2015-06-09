/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.dispatcher;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Parcel;
import android.test.AndroidTestCase;

import java.util.Arrays;

public class UpdateTest extends AndroidTestCase {

	public void testUpdateParcelableDescribeContents() {
		final Uri uri = Uri.parse("content://empty");
		final ContentValues values = new ContentValues();
		final Update request = new Update(uri, values);
		assertEquals(0, request.describeContents());
	}

	public void testUpdateParcelableCreatorArray() {
		final Update[] request = Update.CREATOR.newArray(1);
		assertEquals(1, request.length);
	}

	public void testUpdateParcelableCreator() {
		final Uri uri = Uri.parse("content://empty");
		final ContentValues values = new ContentValues();
		final String where = "test = ?";
		final String[] whereArgs = { "true" };

		final Update request = new Update(uri, values);
		request.setWhere(where, whereArgs);

		final Parcel parcel = Parcel.obtain();
		request.writeToParcel(parcel, 0);
		parcel.setDataPosition(0);

		final Update parceled = Update.CREATOR.createFromParcel(parcel);
		assertEquals(uri, parceled.getUri());
		assertEquals(values, parceled.getContentValues());
		assertEquals(where, parceled.getWhereClause());
		assertTrue(Arrays.deepEquals(whereArgs, parceled.getWhereArgs()));

		parcel.recycle();
	}
}
