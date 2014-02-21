package com.arca.dispatcher;

import android.content.Context;

public class SupportUpdateLoader extends SupportResultLoader<UpdateResult> {

	public SupportUpdateLoader(final Context context, final RequestExecutor executor, final Update request) {
		super(context, executor, request);
	}

	@Override
	public UpdateResult loadInBackground() {
		final Update update = (Update) getContentRequest();
		final RequestExecutor executor = getRequestExecutor();
		return executor.execute(update);
	}

	@Override
	public UpdateResult getErrorResult(final Error error) {
		return new UpdateResult(error);
	}

}
