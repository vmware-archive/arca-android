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

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

public class Error implements Parcelable {

	private final int mErrorCode;
	private final String mErrorMessage;

	public Error(final int errorCode, final String errorMessage) {
		mErrorCode = errorCode;
		mErrorMessage = errorMessage;
	}

	public Error(final Parcel in) {
		mErrorCode = in.readInt();
		mErrorMessage = in.readString();
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		dest.writeInt(mErrorCode);
		dest.writeString(mErrorMessage);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public String getMessage() {
		return mErrorMessage;
	}

	public int getCode() {
		return mErrorCode;
	}

	@Override
	public String toString() {
		return String.format(Locale.getDefault(), "[%d] %s", mErrorCode, mErrorMessage);
	}

	public static final Creator<Error> CREATOR = new Creator<Error>() {
		@Override
		public Error createFromParcel(final Parcel in) {
			return new Error(in);
		}

		@Override
		public Error[] newArray(final int size) {
			return new Error[size];
		}
	};

}
