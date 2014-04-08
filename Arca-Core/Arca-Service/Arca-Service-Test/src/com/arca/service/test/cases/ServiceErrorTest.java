/* 
 * Copyright (C) 2014 Pivotal Software, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arca.service.test.cases;

import java.util.Locale;

import android.os.Parcel;
import android.test.AndroidTestCase;

import com.arca.service.ServiceError;

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
		assertEquals(100, ServiceError.Codes.UNKNOWN);
	}

	public void testServiceErrorMessages() {
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
