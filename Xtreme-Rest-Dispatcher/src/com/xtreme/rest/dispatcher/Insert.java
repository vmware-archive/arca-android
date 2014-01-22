package com.xtreme.rest.dispatcher;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

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

	public static final Parcelable.Creator<Insert> CREATOR = new Parcelable.Creator<Insert>() {
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
