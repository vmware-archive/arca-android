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
package com.arca.dispatcher;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Parcel;

public class Insert extends Request<Integer> {

	private final ContentValues[] mValues;

	public Insert(final Uri uri, final ContentValues values) {
		super(uri);
		mValues = new ContentValues[] { values };
	}

	public Insert(final Uri uri, final ContentValues[] values) {
		super(uri);
		mValues = values;
	}

	public Insert(final Parcel in) {
		super(in);
		mValues = in.createTypedArray(ContentValues.CREATOR);
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
		dest.writeTypedArray(mValues, flags);
	}

	public ContentValues[] getContentValues() {
		return mValues;
	}

	public static final Creator<Insert> CREATOR = new Creator<Insert>() {
		@Override
		public Insert createFromParcel(final Parcel in) {
			return new Insert(in);
		}

		@Override
		public Insert[] newArray(final int size) {
			return new Insert[size];
		}
	};

}
