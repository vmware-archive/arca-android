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

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint("ParcelCreator")
public abstract class Request<T> implements Parcelable {

	private static int sID = 0;

	private final int mIdentifier;
	private final Uri mUri;

	public Request(final Uri uri, final int identifier) {
		if (uri == null) {
			throw new IllegalArgumentException("The uri cannot be null.");
		}

		if (identifier < 1000) {
			throw new IllegalArgumentException("Custom identifiers cannot be less than 1000.");
		}

		mIdentifier = identifier;
		mUri = uri;
	}

	public Request(final Uri uri) {
		if (uri == null) {
			throw new IllegalArgumentException("The uri cannot be null.");
		}
		mIdentifier = sID++;
		mUri = uri;
	}

	public Request(final Parcel in) {
		mIdentifier = in.readInt();
		mUri = in.readParcelable(Uri.class.getClassLoader());
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		dest.writeInt(mIdentifier);
		dest.writeParcelable(mUri, flags);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public int getIdentifier() {
		return mIdentifier;
	}

	public Uri getUri() {
		return mUri;
	}
}
