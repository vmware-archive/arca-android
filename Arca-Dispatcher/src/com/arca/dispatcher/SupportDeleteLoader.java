package com.arca.dispatcher;

import android.content.Context;

public class SupportDeleteLoader extends SupportResultLoader<DeleteResult> {

	public SupportDeleteLoader(final Context context, final RequestExecutor executor, final Delete request) {
		super(context, executor, request);
	}

	@Override
	public DeleteResult loadInBackground() {
		final Delete delete = (Delete) getContentRequest();
		final RequestExecutor executor = getRequestExecutor();
		return executor.execute(delete);
	}

	@Override
	public DeleteResult getErrorResult(final Error error) {
		return new DeleteResult(error);
	}

}
