package io.pivotal.arca.dispatcher;

import android.content.Context;

public class SupportBatchLoader extends SupportResultLoader<BatchResult> {

	public SupportBatchLoader(final Context context, final RequestExecutor executor, final Batch request) {
		super(context, executor, request);
	}

	@Override
	public BatchResult loadInBackground() {
		final Batch request = (Batch) getContentRequest();
		final RequestExecutor executor = getRequestExecutor();
		return executor.execute(request);
	}

	@Override
	public BatchResult getErrorResult(final Error error) {
		return new BatchResult(error);
	}

}
