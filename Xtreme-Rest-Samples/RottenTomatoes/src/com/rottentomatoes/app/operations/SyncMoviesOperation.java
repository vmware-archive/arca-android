package com.rottentomatoes.app.operations;

import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncStats;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.xtreme.rest.broadcasts.RestBroadcaster;
import com.xtreme.rest.service.Operation;
import com.xtreme.rest.service.ServiceError;
import com.xtreme.rest.service.Task;

public class SyncMoviesOperation extends Operation {

	public SyncMoviesOperation(final Uri uri) {
		super(uri);
	}

	public SyncMoviesOperation(final Parcel in) {
		super(in);
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
	}
	
	@Override
	public void onCreateTasks() {
		final String type = getUri().getLastPathSegment();
		executeTask(new MovieListTask(type));
	}

	@Override
	public void onSuccess(final Context context, final List<Task<?>> completed) {
		final ContentResolver resolver = context.getContentResolver();
		resolver.notifyChange(getUri(), null, false);
		
		RestBroadcaster.broadcast(context, getUri(), new SyncStats());
	}

	@Override
	public void onFailure(final Context context, final ServiceError error) {
		final SyncStats stats = new SyncStats();
		stats.numIoExceptions++;
		
		RestBroadcaster.broadcast(context, getUri(), stats);
	}

	public static final Parcelable.Creator<SyncMoviesOperation> CREATOR = new Parcelable.Creator<SyncMoviesOperation>() {
		@Override
		public SyncMoviesOperation createFromParcel(final Parcel in) {
			return new SyncMoviesOperation(in);
		}

		@Override
		public SyncMoviesOperation[] newArray(final int size) {
			return new SyncMoviesOperation[size];
		}
	};
}
