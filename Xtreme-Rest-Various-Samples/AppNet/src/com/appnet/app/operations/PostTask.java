package com.appnet.app.operations;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.appnet.app.datasets.PostTable;
import com.appnet.app.models.Post;
import com.appnet.app.providers.AppNetContentProvider;
import com.google.gson.Gson;
import com.xtreme.rest.service.Task;
import com.xtreme.threading.RequestIdentifier;

public class PostTask extends Task<String> {

	private final String mId;
	
	public PostTask(final String id) {
		mId = id;
	}

	@Override
	public RequestIdentifier<?> onCreateIdentifier() {
		// TODO this needs to be a unique identifier across all tasks
		return new RequestIdentifier<String>(String.format("post::%s", mId));
	}
	
	@Override
	public String onExecuteNetworkRequest(final Context context) throws Exception {
		// TODO make network request to fetch object(s) 
		// return AppNetRequests.getPost(mId);
		throw new Exception("Override this method to return a json string for a Post.");
	}

	@Override
	public void onExecuteProcessingRequest(final Context context, final String data) throws Exception {
		// TODO parse the response and insert into content provider
		final Post item = new Gson().fromJson(data, Post.class);
		final ContentValues values = PostTable.getInstance().getContentValues(item);
		final ContentResolver resolver = context.getContentResolver();
		resolver.insert(AppNetContentProvider.Uris.POSTS_URI, values);
	}
}
