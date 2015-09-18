/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.dispatcher;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import io.pivotal.arca.utils.ArrayUtils;
import io.pivotal.arca.utils.StringUtils;

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

	public void addWhere(final String whereClause, final String... whereArgs) {
		mWhereClause = StringUtils.append(mWhereClause, whereClause, " AND ");
		mWhereArgs = ArrayUtils.append(mWhereArgs, whereArgs);
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
