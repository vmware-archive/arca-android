package com.xtreme.rest.dispatcher;

import android.content.Context;

public class SupportUpdateLoader extends SupportIntegerLoader<UpdateResult> {

	public SupportUpdateLoader(Context context, RequestExecutor executor, Update request) {
		super(context, executor, request);
	}

	@Override
	public UpdateResult loadInBackground() {
		final Update update = (Update) getContentRequest();
		final RequestExecutor executor = getRequestExecutor();
		return executor.execute(update);
	}

	@Override
	public UpdateResult getErrorResult(ContentError error) {
		return new UpdateResult(error);
	}

}
