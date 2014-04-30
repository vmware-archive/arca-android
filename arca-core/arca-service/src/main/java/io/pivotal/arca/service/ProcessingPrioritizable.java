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

import io.pivotal.arca.threading.Identifier;
import io.pivotal.arca.threading.Prioritizable;
import io.pivotal.arca.utils.Logger;

public class ProcessingPrioritizable<T> extends Prioritizable {

	private final ProcessingTask<T> mTask;
	private final T mData;

	private ServiceError mError;

	public ProcessingPrioritizable(final ProcessingTask<T> task, final T data) {
		mTask = task;
		mData = data;
	}

	@Override
	public Identifier<?> getIdentifier() {
		return mTask.getIdentifier();
	}

	@Override
	public void execute() {
		try {
			mTask.executeProcessing(mData);
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