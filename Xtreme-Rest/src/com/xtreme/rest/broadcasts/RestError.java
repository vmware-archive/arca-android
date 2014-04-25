package com.xtreme.rest.broadcasts;

import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A wrapper around the error that occurred while loading data.
 */
public class RestError implements Parcelable {

	private final int mErrorCode;
	private final String mErrorMessage;
	
	public RestError(final int errorCode, final String errorMessage) {
		mErrorCode = errorCode;
		mErrorMessage = errorMessage;
	}

	public RestError(final Parcel in) {
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

	public int getErrorCode() {
		return mErrorCode;
	}
	
	@Override
	public String toString() {
		return String.format(Locale.getDefault(), "[%d] %s", mErrorCode, mErrorMessage);
	}
	
	public static final Parcelable.Creator<RestError> CREATOR = new Parcelable.Creator<RestError>() {
		@Override
		public RestError createFromParcel(final Parcel in) {
			return new RestError(in);
		}
	
		@Override
		public RestError[] newArray(final int size) {
			return new RestError[size];
		}
	};

}
