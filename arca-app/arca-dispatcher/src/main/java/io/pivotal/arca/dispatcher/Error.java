package io.pivotal.arca.dispatcher;

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
