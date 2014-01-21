package com.xtreme.rest.dispatcher;

import android.content.Context;

public class ModernInsertLoader extends ModernResultLoader<InsertResult> {

	public ModernInsertLoader(final Context context, final RequestExecutor executor, final Insert request) {
		super(context, executor, request);
	}

	@Override
	public InsertResult loadInBackground() {
		final Insert insert = (Insert) getContentRequest();
		final RequestExecutor executor = getRequestExecutor();
		return executor.execute(insert);
	}

	@Override
	public InsertResult getErrorResult(final ContentError error) {
		return new InsertResult(error);
	}

}
