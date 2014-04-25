package com.crunchbase.app.operations;

import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.xtreme.rest.broadcasts.RestBroadcaster;
import com.xtreme.rest.service.Operation;
import com.xtreme.rest.service.ServiceError;
import com.xtreme.rest.service.Task;

public class CompanyOperation extends Operation {

	public CompanyOperation(final Uri uri) {
		super(uri);
	}

	public CompanyOperation(final Parcel in) {
		super(in);
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
	}

	@Override
	public void onCreateTasks() {
		final String id = getUri().getLastPathSegment();
		executeTask(new CompanyTask(id));
	}

	@Override
	public void onSuccess(final Context context, final List<Task<?>> completed) {
		final ContentResolver resolver = context.getContentResolver();
		resolver.notifyChange(getUri(), null);
	}

	@Override
	public void onFailure(final Context context, final ServiceError error) {
		final int errorCode = error.getCode();
		final String errorMessage = error.getMessage();
		RestBroadcaster.broadcast(context, getUri(), errorCode, errorMessage);
	}
	
	public static final Parcelable.Creator<CompanyOperation> CREATOR = new Parcelable.Creator<CompanyOperation>() {
		@Override
		public CompanyOperation createFromParcel(final Parcel in) {
			return new CompanyOperation(in);
		}

		@Override
		public CompanyOperation[] newArray(final int size) {
			return new CompanyOperation[size];
		}
	};

}
