package com.rottentomatoes.app.operations;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncStats;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.xtreme.rest.service.Operation;
import com.xtreme.rest.service.ServiceError;
import com.xtreme.rest.service.Task;
import com.xtreme.rest.sync.RestSyncBroadcaster;

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
	public Set<Task<?>> onCreateTasks() {
		final Set<Task<?>> set = new HashSet<Task<?>>();
		final String type = getUri().getLastPathSegment();
		set.add(new MovieListTask(type));
		return set;
	}

	@Override
	public void onSuccess(final Context context, final List<Task<?>> completed) {
		final ContentResolver resolver = context.getContentResolver();
		resolver.notifyChange(getUri(), null, false);
		
		RestSyncBroadcaster.broadcast(context, getUri(), new SyncStats());
	}

	@Override
	public void onFailure(final Context context, final ServiceError error) {
		final SyncStats stats = new SyncStats();
		stats.numIoExceptions++;
		
		RestSyncBroadcaster.broadcast(context, getUri(), stats);
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
