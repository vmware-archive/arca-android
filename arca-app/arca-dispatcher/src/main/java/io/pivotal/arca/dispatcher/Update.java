/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.dispatcher;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Parcel;

import io.pivotal.arca.utils.ArrayUtils;
import io.pivotal.arca.utils.StringUtils;

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

	public void addWhere(final String whereClause, final String... whereArgs) {
		mWhereClause = StringUtils.append(mWhereClause, whereClause, " AND ");
		mWhereArgs = ArrayUtils.append(mWhereArgs, whereArgs);
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

	public static final Creator<Update> CREATOR = new Creator<Update>() {
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
