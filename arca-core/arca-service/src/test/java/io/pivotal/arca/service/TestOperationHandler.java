/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.service;

import android.content.Context;
import android.os.Message;

import io.pivotal.arca.service.OperationHandler;
import io.pivotal.arca.service.RequestExecutor;

public class TestOperationHandler extends OperationHandler {

	public TestOperationHandler(final Context context, final RequestExecutor executor) {
		super(context, executor);
	}

	@Override
	public boolean sendMessageAtTime(final Message msg, final long uptimeMillis) {
		handleMessage(msg);
		return true;
	}

}
