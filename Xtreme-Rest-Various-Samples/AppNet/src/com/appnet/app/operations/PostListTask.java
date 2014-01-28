package com.appnet.app.operations;

import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.appnet.app.application.AppNetRequests;
import com.appnet.app.datasets.PostTable;
import com.appnet.app.models.Post;
import com.appnet.app.models.PostsResponse;
import com.appnet.app.providers.AppNetContentProvider;
import com.xtreme.rest.service.Task;
import com.xtreme.threading.RequestIdentifier;

public class PostListTask extends Task<List<Post>> {

	public PostListTask() {}

	@Override
	public RequestIdentifier<?> onCreateIdentifier() {
		return new RequestIdentifier<String>("post_list");
	}
	
	@Override
	public List<Post> onExecuteNetworkRequest(final Context context) throws Exception {
		final PostsResponse response = AppNetRequests.getPostList(100);
		return response.getData();
	}

	@Override
	public void onExecuteProcessingRequest(final Context context, final List<Post> data) throws Exception {
		final ContentValues[] values = PostTable.getContentValues(data);
		final ContentResolver resolver = context.getContentResolver();
		resolver.bulkInsert(AppNetContentProvider.Uris.POSTS_URI, values);
	}
}
