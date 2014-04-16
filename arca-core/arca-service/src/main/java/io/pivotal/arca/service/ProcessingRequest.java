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

import io.pivotal.arca.threading.PrioritizableRequest;

public class ProcessingRequest<T> extends PrioritizableRequest {

	private final ProcessingPrioritizableObserver<T> mObserver;

	public ProcessingRequest(final ProcessingPrioritizable<?> prioritizable, final int accessorIndex, final ProcessingPrioritizableObserver<T> observer) {
		super(prioritizable, accessorIndex);
		mObserver = observer;
	}

	public void notifyComplete(final ServiceError error) {
		if (error == null) {
			mObserver.onProcessingComplete();
		} else {
			mObserver.onProcessingFailure(error);
		}
	}

	@Override
	public ProcessingPrioritizable<?> getPrioritizable() {
		return (ProcessingPrioritizable<?>) super.getPrioritizable();
	}

	public Object getData() {
		return getPrioritizable().getData();
	}

	public ServiceError getError() {
		return getPrioritizable().getError();
	}

}
