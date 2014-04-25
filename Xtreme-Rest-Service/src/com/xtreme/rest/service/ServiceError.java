package com.xtreme.rest.service;

import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A {@link Parcelable} wrapper around an error consisting of: error code, type, and message.
 */
public class ServiceError implements Parcelable {

	public static class Codes {		
		public static final int UNKNOWN = 100;
	}

	public static class Messages {		
		public static final String UNKNOWN = "Unknown Error";
	}

	private final int mErrorCode;
	private final String mErrorMessage;
	private final String mErrorType;

	/**
	 * Creates a {@link ServiceError} with the provided info.
	 * 
	 * @param errorCode The associated code
	 * @param errorType The associated type
	 * @param errorMessage The associated message
	 */
	public ServiceError(final int errorCode, final String errorType, final String errorMessage) {
		mErrorCode = errorCode;
		mErrorMessage = errorMessage;
		mErrorType = errorType;
	}
	
	/**
	 * Creates a {@link ServiceError} with a null error type.
	 * 
	 * @param errorCode The code associated with this {@link ServiceError}
	 * @param errorMessage The message associated with this {@link ServiceError}
	 */
	public ServiceError(final int errorCode, final String errorMessage) {
		this(errorCode, null, errorMessage);
	}
	
	/**
	 * Creates a {@link ServiceError} with {@link Codes#UNKNOWN} as the error code and a null error type.
	 * 
	 * @param errorMessage The message associated with this {@link ServiceError}
	 */
	public ServiceError(final String errorMessage) {
		this(Codes.UNKNOWN, errorMessage);
	}

	/**
	 * A convenience constructor that takes creates a formatted message from the exception and then calls {@link #ServiceError(String)}.
	 * 
	 * @param e The exception whose info is used for the formatted message.
	 */
	public ServiceError(final Exception e) {
		this(String.format("Exception class: %s, Exception message: %s",  e.getClass().getName(), e.getLocalizedMessage()));
	}

	public ServiceError(final Parcel in) {
		mErrorCode = in.readInt();
		mErrorMessage = in.readString();
		mErrorType = in.readString();
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		dest.writeInt(mErrorCode);
		dest.writeString(mErrorMessage);
		dest.writeString(mErrorType);
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
	
	public String getType() {
		return mErrorType;
	}
	
	@Override
	public String toString() {
		return String.format(Locale.getDefault(), "[%d] %s", mErrorCode, mErrorMessage);
	}
	
	public static final Parcelable.Creator<ServiceError> CREATOR = new Parcelable.Creator<ServiceError>() {
		@Override
		public ServiceError createFromParcel(final Parcel in) {
			return new ServiceError(in);
		}
	
		@Override
		public ServiceError[] newArray(final int size) {
			return new ServiceError[size];
		}
	};

}
