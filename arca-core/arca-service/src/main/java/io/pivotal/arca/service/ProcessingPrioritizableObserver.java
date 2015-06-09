/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.service;

public interface ProcessingPrioritizableObserver<T> {
	public void onProcessingComplete();

	public void onProcessingFailure(ServiceError error);
}
