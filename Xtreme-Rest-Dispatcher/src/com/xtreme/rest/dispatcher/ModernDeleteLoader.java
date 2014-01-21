package com.xtreme.rest.dispatcher;

import android.content.Context;

public class ModernDeleteLoader extends ModernIntegerLoader<DeleteResult> {

	public ModernDeleteLoader(final Context context, final RequestExecutor executor, final Delete request) {
		super(context, executor, request);
	}

	@Override
	public DeleteResult loadInBackground() {
		final Delete delete = (Delete) getContentRequest();
		final RequestExecutor executor = getRequestExecutor();
		return executor.execute(delete);
	}

	@Override
	public DeleteResult getErrorResult(final ContentError error) {
		return new DeleteResult(error);
	}

}
