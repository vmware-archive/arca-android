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
import android.os.Parcelable;

public class Update extends Request<Integer> {

	private final ContentValues mValues;

	private String mWhereClause;
	private String[] mWhereArgs;

	public Update(final Uri uri, final ContentValues values) {
		super(uri);
		mValues = values;
	}

	public Update(final Parcel in) {
		super(in);
		mValues = in.readParcelable(ContentValues.class.getClassLoader());
		mWhereClause = in.readString();
		mWhereArgs = in.createStringArray();
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
		dest.writeParcelable(mValues, flags);
		dest.writeString(mWhereClause);
		dest.writeStringArray(mWhereArgs);
	}

	public void setWhere(final String whereClause, final String... whereArgs) {
		mWhereClause = whereClause;
		mWhereArgs = whereArgs;
	}

	public ContentValues getContentValues() {
		return mValues;
	}

	public String getWhereClause() {
		return mWhereClause;
	}

	public String[] getWhereArgs() {
		return mWhereArgs;
	}

	public static final Parcelable.Creator<Update> CREATOR = new Parcelable.Creator<Update>() {
		@Override
		public Update createFromParcel(final Parcel in) {
			return new Update(in);
		}

		@Override
		public Update[] newArray(final int size) {
			return new Update[size];
		}
	};

}
