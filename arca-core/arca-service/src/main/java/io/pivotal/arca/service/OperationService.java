/* 
 * Copyright (C) 2014 Pivotal Software, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pivotal.arca.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import io.pivotal.arca.utils.Logger;

public class OperationService extends Service implements OperationHandler.OnStateChangeListener {

	public static enum Action {
		START, CANCEL
	}

	protected static interface Extras {
		public static final String ACTION = "action";
		public static final String OPERATION = "operation";
	}

	public static boolean start(final Context context, final Operation operation) {
		return startService(context, operation, Action.START);
	}

	public static boolean cancel(final Context context, final Operation operation) {
		return startService(context, operation, Action.CANCEL);
	}

	private static boolean startService(final Context context, final Operation operation, final Action action) {
		if (context != null && operation != null) {
			final Intent intent = new Intent(context, OperationService.class);
			intent.putExtra(Extras.OPERATION, operation);
			intent.putExtra(Extras.ACTION, action);
			context.startService(intent);
			return true;
		} else {
			return false;
		}
	}

	private OperationHandler mHandler;
	private int mLatestStartId;

	@Override
	public void onCreate() {
		super.onCreate();
		Logger.v("Created service %s", this.getClass());

		mHandler = onCreateOperationHandler();
	}

	protected OperationHandler onCreateOperationHandler() {
		final Context context = getApplicationContext();
		final RequestExecutor executor = new RequestExecutor.ThreadedRequestExecutor();
		final OperationHandler handler = new OperationHandler(context, executor);
		handler.setOnStateChangeListener(this);
		return handler;
	}

	@Override
	public void onDestroy() {
		Logger.v("Destroyed service %s", this.getClass());
		super.onDestroy();
	}

	@Override
	public IBinder onBind(final Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(final Intent intent, final int flags, final int startId) {
		Logger.v("Started service %s", this.getClass());
		final int start = super.onStartCommand(intent, flags, startId);
		mLatestStartId = startId;
		handleIntent(intent);
		return start;
	}

	protected void handleIntent(final Intent intent) {
		if (intent != null && intent.hasExtra(Extras.OPERATION)) {
			final Action action = (Action) intent.getSerializableExtra(Extras.ACTION);
			final Operation operation = intent.getParcelableExtra(Extras.OPERATION);
			handleAction(action, operation);
		}
	}

	protected void handleAction(final Action action, final Operation operation) {
		if (action == Action.START) {
            handleStart(operation);
        } else if (action == Action.CANCEL) {
            handleCancel(operation);
		} else {
			throw new UnsupportedOperationException("This action has not yet been implemented.");
		}
	}

	protected void handleStart(final Operation operation) {
		mHandler.start(operation);
	}

    protected void handleCancel(final Operation operation) {
        mHandler.cancel(operation);
    }

	@Override
	public void onStateChanged(final OperationHandler.HandlerState state) {
		if (state == OperationHandler.HandlerState.IDLE) {
			stopSelf(mLatestStartId);
		}
	}

}
