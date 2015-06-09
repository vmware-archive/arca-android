/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.dispatcher;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ModernDeleteLoader extends ModernResultLoader<DeleteResult> {

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
	public DeleteResult getErrorResult(final Error error) {
		return new DeleteResult(error);
	}

}
