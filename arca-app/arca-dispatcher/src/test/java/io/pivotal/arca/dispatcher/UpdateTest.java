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

	public void testUpdateAddWhere() {
		final Uri uri = Uri.parse("content://empty");
		final Update request = new Update(uri, null);

		final String where1 = "test1 = ?";
		final String[] whereArgs1 = { "true" };
		final String where2 = "test2 = ?";
		final String[] whereArgs2 = { "false" };

		assertEquals(null, request.getWhereArgs());
		assertEquals(null, request.getWhereClause());

		request.addWhere(where1, whereArgs1);

		assertEquals("test1 = ?", request.getWhereClause());
		assertTrue(Arrays.deepEquals(new String[] { "true" }, request.getWhereArgs()));

		request.addWhere(where2, whereArgs2);

		assertEquals("test1 = ? AND test2 = ?", request.getWhereClause());
		assertTrue(Arrays.deepEquals(new String[] { "true", "false" }, request.getWhereArgs()));
	}
}
