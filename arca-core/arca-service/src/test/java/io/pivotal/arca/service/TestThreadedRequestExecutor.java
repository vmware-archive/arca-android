/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.service;

import io.pivotal.arca.threading.AuxiliaryExecutor;

public class TestThreadedRequestExecutor extends RequestExecutor.ThreadedRequestExecutor {

	@Override
	protected AuxiliaryExecutor onCreateNetworkingExecutor() {
		return new TestAuxiliaryExecutor(this);
	}

	@Override
	protected AuxiliaryExecutor onCreateProcessingExecutor() {
		return new TestAuxiliaryExecutor(this);
	}
}
