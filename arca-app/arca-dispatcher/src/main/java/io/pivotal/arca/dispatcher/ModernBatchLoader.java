package io.pivotal.arca.dispatcher;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ModernBatchLoader extends ModernResultLoader<BatchResult> {

	public ModernBatchLoader(final Context context, final RequestExecutor executor, final Batch request) {
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
