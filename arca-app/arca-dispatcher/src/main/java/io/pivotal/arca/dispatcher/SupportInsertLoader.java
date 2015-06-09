/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.dispatcher;

import android.content.Context;

public class SupportInsertLoader extends SupportResultLoader<InsertResult> {

	public SupportInsertLoader(final Context context, final RequestExecutor executor, final Insert request) {
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
