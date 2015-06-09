/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.service;

public interface NetworkingPrioritizableObserver<T> {
	public void onNetworkingComplete(T data);

	public void onNetworkingFailure(ServiceError error);
}
