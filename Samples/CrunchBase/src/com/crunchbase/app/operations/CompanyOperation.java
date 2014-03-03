package com.crunchbase.app.operations;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.arca.dispatcher.ErrorBroadcaster;
import com.arca.service.Operation;
import com.arca.service.ServiceError;
import com.arca.service.Task;

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
	public Set<Task<?>> onCreateTasks() {
		final Set<Task<?>> set = new HashSet<Task<?>>();
		final String id = getUri().getLastPathSegment();
		set.add(new CompanyTask(id));
		return set;
	}

	@Override
	public void onSuccess(final Context context, final List<Task<?>> completed) {
		final ContentResolver resolver = context.getContentResolver();
		resolver.notifyChange(getUri(), null);
	}

	@Override
	public void onFailure(final Context context, final ServiceError error) {
		ErrorBroadcaster.broadcast(context, getUri(), error.getCode(), error.getMessage());
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
