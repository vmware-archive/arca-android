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
package com.arca.dispatcher;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
abstract class ModernLoader<T> extends AsyncTaskLoader<T> implements ErrorListener {

	private final ErrorReceiver mReceiver;
	private final RequestExecutor mExecutor;
	private final Request<?> mRequest;

	public ModernLoader(final Context context, final RequestExecutor executor, final Request<?> request) {
		super(context);
		mReceiver = new ErrorReceiver(this);
		mExecutor = executor;
		mRequest = request;
		register(request);
	}

	private void register(final Request<?> request) {
		if (request != null) {
			mReceiver.register(request.getUri());
		}
	}

	@Override
	public abstract T loadInBackground();

	protected abstract T getErrorResult(final Error error);

	protected abstract T getResult();

	protected abstract void clearResult();

	public RequestExecutor getRequestExecutor() {
		return mExecutor;
	}

	public Request<?> getContentRequest() {
		return mRequest;
	}

	@Override
	protected void onStartLoading() {
		forceLoad();
	}

    @Override
    @SuppressLint("NewApi")
	protected void onStopLoading() {
		cancelLoad();
	}

	@Override
	public void onRequestError(final Error error) {
		final T result = getErrorResult(error);
		deliverResult(result);
	}

	@Override
	protected void onReset() {
		super.onReset();
		onStopLoading();
		clearResult();
	}

	@Override
	public void reset() {
		super.reset();
		mReceiver.unregister();
	}
}