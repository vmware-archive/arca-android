package com.xtreme.rest.service.test.cases;

import java.util.Locale;

import android.os.Parcel;
import android.test.AndroidTestCase;

import com.xtreme.rest.service.ServiceError;

public class ServiceErrorTest extends AndroidTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testServiceErrorToString() {
		final int code = ServiceError.Codes.UNKNOWN;
		final String message = ServiceError.Messages.UNKNOWN;
		final String expected = String.format(Locale.getDefault(), "[%d] %s", code, message);
		final ServiceError error = new ServiceError(code, message);
		assertEquals(expected, error.toString());
	}
	
	public void testServiceErrorCodes() {
		assertNotNull(new ServiceError.Codes());
		assertEquals(100, ServiceError.Codes.UNKNOWN);
	}
	
	public void testServiceErrorMessages() {
		assertNotNull(new ServiceError.Messages());
		assertEquals("An unknown error occured.", ServiceError.Messages.UNKNOWN);
	}
	
	public void testServiceErrorNullMessage() {
		final String message = null;
		final ServiceError error = new ServiceError(message);
		assertEquals(message, error.getMessage());
	}
	
	public void testServiceErrorEmptyMessage() {
		final String message = "";
		final ServiceError error = new ServiceError(message);
		assertEquals(message, error.getMessage());
	}
	
	public void testServiceErrorCustomMessage() {
		final String message = "error";
		final ServiceError error = new ServiceError(message);
		assertEquals(message, error.getMessage());
	}
	
	public void testServiceErrorNegativeCode() {
		final int code = -1;
		final ServiceError error = new ServiceError(code, null);
		assertEquals(code, error.getCode());
	}
	
	public void testServiceErrorZeroCode() {
		final int code = 0;
		final ServiceError error = new ServiceError(code, null);
		assertEquals(code, error.getCode());
	}
	
	public void testServiceErrorPositiveCode() {
		final int code = 1;
		final ServiceError error = new ServiceError(code, null);
		assertEquals(code, error.getCode());
	}
	
	public void testServiceErrorNullType() {
		final String type = null;
		final ServiceError error = new ServiceError(-1, type, null);
		assertEquals(type, error.getType());
	}
	
	public void testServiceErrorEmptyType() {
		final String type = "";
		final ServiceError error = new ServiceError(-1, type, null);
		assertEquals(type, error.getType());
	}
	
	public void testServiceErrorCustomType() {
		final String type = "type";
		final ServiceError error = new ServiceError(-1, type, null);
		assertEquals(type, error.getType());
	}
	
	public void testServiceErrorParcelableDescribeContents() {
		final ServiceError error = new ServiceError("");
		assertEquals(0, error.describeContents());
	}
	
	public void testServiceErrorParcelableCreatorArray() {
		final ServiceError[] error = ServiceError.CREATOR.newArray(1);
		assertEquals(1, error.length);
	}
	
	public void testServiceErrorParcelableCreator() {
		final int code = 1;
		final String type = "type";
		final String message = "message";
		final ServiceError error = new ServiceError(code, type, message);
		
		final Parcel parcel = Parcel.obtain();
		error.writeToParcel(parcel, 0);
		parcel.setDataPosition(0);
		
		final ServiceError parceled = ServiceError.CREATOR.createFromParcel(parcel);
		assertEquals(code, parceled.getCode());
		assertEquals(type, parceled.getType());
		assertEquals(message, parceled.getMessage());
		
		parcel.recycle();
	}
	
}
