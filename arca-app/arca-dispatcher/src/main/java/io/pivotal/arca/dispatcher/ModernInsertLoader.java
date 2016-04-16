package io.pivotal.arca.dispatcher;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
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
	public InsertResult getErrorResult(final Error error) {
		return new InsertResult(error);
	}

}
