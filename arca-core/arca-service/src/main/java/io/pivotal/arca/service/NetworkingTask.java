/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.service;

import io.pivotal.arca.threading.Identifier;

public interface NetworkingTask<T> {
	public Identifier<?> getIdentifier();

	public T executeNetworking() throws Exception;
}
