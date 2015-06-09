/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.service;

public interface RequestObserver {
	public void onNetworkingRequestComplete(NetworkingRequest<?> request);

	public void onNetworkingRequestCancelled(NetworkingRequest<?> request);

	public void onProcessingRequestComplete(ProcessingRequest<?> request);

	public void onProcessingRequestCancelled(ProcessingRequest<?> request);
}
