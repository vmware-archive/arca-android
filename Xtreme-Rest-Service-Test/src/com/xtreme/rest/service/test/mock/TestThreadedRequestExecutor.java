package com.xtreme.rest.service.test.mock;

import com.xtreme.rest.service.RequestExecutor.ThreadedRequestExecutor;
import com.xtreme.threading.AuxiliaryExecutor;

public class TestThreadedRequestExecutor extends ThreadedRequestExecutor {
	
	@Override
	protected AuxiliaryExecutor onCreateNetworkExecutor() {
		return new TestAuxiliaryExecutor(this);
	}

	@Override
	protected AuxiliaryExecutor onCreateProcessingExecutor() {
		return new TestAuxiliaryExecutor(this);
	}
}
