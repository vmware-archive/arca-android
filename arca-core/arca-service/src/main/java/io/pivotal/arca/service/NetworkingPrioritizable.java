/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.service;

import io.pivotal.arca.threading.Identifier;
import io.pivotal.arca.threading.Prioritizable;
import io.pivotal.arca.utils.Logger;

public class NetworkingPrioritizable<T> extends Prioritizable {

	private final NetworkingTask<T> mTask;

	private ServiceError mError;
	private T mData;

	public NetworkingPrioritizable(final NetworkingTask<T> task) {
		mTask = task;
	}

	@Override
	public Identifier<?> getIdentifier() {
		return mTask.getIdentifier();
	}

	@Override
	public void execute() {
		try {
			mData = mTask.executeNetworking();
		} catch (final ServiceException e) {
			Logger.ex(e);
			mError = e.getError();
		} catch (final Exception e) {
			Logger.ex(e);
			mError = new ServiceError(e);
		}
	}

	public Object getData() {
		return mData;
	}

	public ServiceError getError() {
		return mError;
	}

}