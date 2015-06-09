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
public class ModernUpdateLoader extends ModernResultLoader<UpdateResult> {

	public ModernUpdateLoader(Context context, RequestExecutor executor, Update request) {
		super(context, executor, request);
	}

	@Override
	public UpdateResult loadInBackground() {
		final Update update = (Update) getContentRequest();
		final RequestExecutor executor = getRequestExecutor();
		return executor.execute(update);
	}

	@Override
	public UpdateResult getErrorResult(Error error) {
		return new UpdateResult(error);
	}

}
