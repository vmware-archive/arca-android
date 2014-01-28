package com.crunchbase.app.operations;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.xtreme.rest.dispatcher.ContentBroadcaster;
import com.xtreme.rest.service.Operation;
import com.xtreme.rest.service.ServiceError;
import com.xtreme.rest.service.Task;

public class CompanyListOperation extends Operation {

	public CompanyListOperation(final Uri uri) {
		super(uri);
	}

	public CompanyListOperation(final Parcel in) {
		super(in);
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
	}

	@Override
	public Set<Task<?>> onCreateTasks() {
		final Set<Task<?>> set = new HashSet<Task<?>>();
		set.add(new CompanyListTask(1));
		return set;
	}
	
	@Override
	public void onTaskComplete(final Task<?> task) {
		super.onTaskComplete(task);

		if (task instanceof CompanyListTask) {
			final CompanyListTask listTask = (CompanyListTask) task;
			final int page = listTask.getNextPage();
			if (page > 0) {
				executeTask(new CompanyListTask(page));
			}
		}
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
		ContentBroadcaster.broadcast(context, getUri(), errorCode, errorMessage);
	}
	
	public static final Parcelable.Creator<CompanyListOperation> CREATOR = new Parcelable.Creator<CompanyListOperation>() {
		@Override
		public CompanyListOperation createFromParcel(final Parcel in) {
			return new CompanyListOperation(in);
		}

		@Override
		public CompanyListOperation[] newArray(final int size) {
			return new CompanyListOperation[size];
		}
	};

}
