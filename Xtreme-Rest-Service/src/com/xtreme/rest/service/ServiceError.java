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
		public static final String UNKNOWN = "An unknown error occured.";
	}

	private final int mCode;
	private final String mMessage;
	private final String mType;

	/**
	 * Creates a {@link ServiceError} with the provided info.
	 * 
	 * @param code The associated code
	 * @param type The associated type
	 * @param message The associated message
	 */
	public ServiceError(final int code, final String type, final String message) {
		mCode = code;
		mMessage = message;
		mType = type;
	}
	
	/**
	 * Creates a {@link ServiceError} with a null error type.
	 * 
	 * @param code The code associated with this {@link ServiceError}
	 * @param message The message associated with this {@link ServiceError}
	 */
	public ServiceError(final int code, final String message) {
		this(code, null, message);
	}
	
	/**
	 * Creates a {@link ServiceError} with {@link Codes#UNKNOWN} as the error code and a null error type.
	 * 
	 * @param message The message associated with this {@link ServiceError}
	 */
	public ServiceError(final String message) {
		this(Codes.UNKNOWN, message);
	}

	/**
	 * A convenience constructor that takes an {@link Exception} and then calls {@link #ServiceError(String)} 
	 * with the localized message from the exception.
	 * 
	 * @param exception The exception whose localized message to use.
	 */
	public ServiceError(final Exception exception) {
		this(exception.getLocalizedMessage());
	}

	public ServiceError(final Parcel in) {
		mCode = in.readInt();
		mMessage = in.readString();
		mType = in.readString();
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		dest.writeInt(mCode);
		dest.writeString(mMessage);
		dest.writeString(mType);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	public String getMessage() {
		return mMessage;
	}

	public int getCode() {
		return mCode;
	}
	
	public String getType() {
		return mType;
	}
	
	@Override
	public String toString() {
		return String.format(Locale.getDefault(), "[%d] %s", mCode, mMessage);
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
