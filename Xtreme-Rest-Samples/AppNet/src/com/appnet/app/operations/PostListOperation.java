package com.appnet.app.operations;

import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.appnet.app.providers.AppNetUriCache;
import com.xtreme.rest.broadcasts.RestBroadcaster;
import com.xtreme.rest.service.Operation;
import com.xtreme.rest.service.ServiceError;
import com.xtreme.rest.service.Task;

public class PostListOperation extends Operation {


	public PostListOperation(final Uri uri) {
		super(uri);
	}

	public PostListOperation(final Parcel in) {
		super(in);
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
	}

	@Override
	public void onCreateTasks() {
		executeTask(new PostListTask());
	}

	@Override
	public void onSuccess(final Context context, final List<Task<?>> completed) {
		final Uri uri = getUri();
		AppNetUriCache.add(uri);
		
		final ContentResolver resolver = context.getContentResolver();
		resolver.notifyChange(uri, null);
	}

	@Override
	public void onFailure(final Context context, final ServiceError error) {
		final int errorCode = error.getCode();
		final String errorMessage = error.getMessage();
		RestBroadcaster.broadcast(context, getUri(), errorCode, errorMessage);
	}
	
	public static final Parcelable.Creator<PostListOperation> CREATOR = new Parcelable.Creator<PostListOperation>() {
		@Override
		public PostListOperation createFromParcel(final Parcel in) {
			return new PostListOperation(in);
		}

		@Override
		public PostListOperation[] newArray(final int size) {
			return new PostListOperation[size];
		}
	};

}
