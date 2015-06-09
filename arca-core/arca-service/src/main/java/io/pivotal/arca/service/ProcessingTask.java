/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.service;

import io.pivotal.arca.threading.Identifier;

public interface ProcessingTask<T> {
	public Identifier<?> getIdentifier();

	public void executeProcessing(T data) throws Exception;
}
