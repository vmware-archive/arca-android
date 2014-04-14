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
package com.arca.dispatcher.test;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Parcel;
import android.test.AndroidTestCase;

import com.arca.dispatcher.Insert;

import java.util.Arrays;

public class InsertTest extends AndroidTestCase {

	public void testInsertParcelableDescribeContents() {
		final Uri uri = Uri.parse("content://empty");
		final ContentValues values = new ContentValues();
		final Insert request = new Insert(uri, values);
		assertEquals(0, request.describeContents());
	}

	public void testInsertParcelableCreatorArray() {
		final Insert[] request = Insert.CREATOR.newArray(1);
		assertEquals(1, request.length);
	}

	public void testInsertParcelableCreator() {
		final Uri uri = Uri.parse("content://empty");
		final ContentValues[] values = { new ContentValues() };

		final Insert request = new Insert(uri, values);

		final Parcel parcel = Parcel.obtain();
		request.writeToParcel(parcel, 0);
		parcel.setDataPosition(0);

		final Insert parceled = Insert.CREATOR.createFromParcel(parcel);
		assertEquals(uri, parceled.getUri());
		assertTrue(Arrays.deepEquals(values, parceled.getContentValues()));

		parcel.recycle();
	}
}
