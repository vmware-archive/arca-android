package io.pivotal.arca.dispatcher;

import android.os.Parcel;
import android.test.AndroidTestCase;

import java.util.Locale;

public class ErrorTest extends AndroidTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testErrorToString() {
		final int code = -1;
		final String message = "message";
		final String expected = String.format(Locale.getDefault(), "[%d] %s", code, message);
		final io.pivotal.arca.dispatcher.Error error = new Error(code, message);
		assertEquals(expected, error.toString());
	}

	public void testErrorNullMessage() {
		final String message = null;
		final Error error = new Error(-1, message);
		assertEquals(message, error.getMessage());
	}

	public void testErrorEmptyMessage() {
		final String message = "";
		final Error error = new Error(-1, message);
		assertEquals(message, error.getMessage());
	}

	public void testErrorCustomMessage() {
		final String message = "error";
		final Error error = new Error(-1, message);
		assertEquals(message, error.getMessage());
	}

	public void testErrorNegativeCode() {
		final int code = -1;
		final Error error = new Error(code, null);
		assertEquals(code, error.getCode());
	}

	public void testErrorZeroCode() {
		final int code = 0;
		final Error error = new Error(code, null);
		assertEquals(code, error.getCode());
	}

	public void testErrorPositiveCode() {
		final int code = 1;
		final Error error = new Error(code, null);
		assertEquals(code, error.getCode());
	}

	public void testErrorParcelableDescribeContents() {
		final Error error = new Error(-1, "");
		assertEquals(0, error.describeContents());
	}

	public void testErrorParcelableCreatorArray() {
		final Error[] error = Error.CREATOR.newArray(1);
		assertEquals(1, error.length);
	}

	public void testErrorParcelableCreator() {
		final int code = 1;
		final String message = "message";
		final Error error = new Error(code, message);

		final Parcel parcel = Parcel.obtain();
		error.writeToParcel(parcel, 0);
		parcel.setDataPosition(0);

		final Error parceled = Error.CREATOR.createFromParcel(parcel);
		assertEquals(code, parceled.getCode());
		assertEquals(message, parceled.getMessage());

		parcel.recycle();
	}

}
