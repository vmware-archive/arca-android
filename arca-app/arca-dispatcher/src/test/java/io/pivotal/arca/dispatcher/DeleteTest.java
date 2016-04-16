package io.pivotal.arca.dispatcher;

import android.net.Uri;
import android.os.Parcel;
import android.test.AndroidTestCase;

import java.util.Arrays;

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

	public void testDeleteAddWhere() {
		final Uri uri = Uri.parse("content://empty");
		final Delete request = new Delete(uri);

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
