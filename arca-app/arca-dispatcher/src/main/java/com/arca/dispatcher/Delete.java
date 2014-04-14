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

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Delete extends Request<Integer> {

	private String mWhereClause;
	private String[] mWhereArgs;

	public Delete(final Uri uri) {
		super(uri);
	}

	public Delete(final Parcel in) {
		super(in);
		mWhereClause = in.readString();
		mWhereArgs = in.createStringArray();
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(mWhereClause);
		dest.writeStringArray(mWhereArgs);
	}

	public void setWhere(final String whereClause, final String... whereArgs) {
		mWhereClause = whereClause;
		mWhereArgs = whereArgs;
	}

	public String getWhereClause() {
		return mWhereClause;
	}

	public String[] getWhereArgs() {
		return mWhereArgs;
	}

	public static final Parcelable.Creator<Delete> CREATOR = new Parcelable.Creator<Delete>() {
		@Override
		public Delete createFromParcel(final Parcel in) {
			return new Delete(in);
		}

		@Override
		public Delete[] newArray(final int size) {
			return new Delete[size];
		}
	};

}
