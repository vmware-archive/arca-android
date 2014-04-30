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

public class NetworkingRequest<T> extends PrioritizableRequest {

	private final NetworkingPrioritizableObserver<T> mObserver;

	public NetworkingRequest(final NetworkingPrioritizable<?> prioritizable, final int accessorIndex, final NetworkingPrioritizableObserver<T> observer) {
		super(prioritizable, accessorIndex);
		mObserver = observer;
	}

	@SuppressWarnings("unchecked")
	public void notifyComplete(final Object data, final ServiceError error) {
		if (error == null) {
			mObserver.onNetworkingComplete((T) data);
		} else {
			mObserver.onNetworkingFailure(error);
		}
	}

	@Override
	public NetworkingPrioritizable<?> getPrioritizable() {
		return (NetworkingPrioritizable<?>) super.getPrioritizable();
	}

	public Object getData() {
		return getPrioritizable().getData();
	}

	public ServiceError getError() {
		return getPrioritizable().getError();
	}
}
