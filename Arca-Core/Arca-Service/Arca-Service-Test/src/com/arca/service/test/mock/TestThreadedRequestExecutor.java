package com.arca.service.test.mock;

import com.arca.service.RequestExecutor.ThreadedRequestExecutor;
import com.xtreme.threading.AuxiliaryExecutor;

public class TestThreadedRequestExecutor extends ThreadedRequestExecutor {
	
	@Override
	protected AuxiliaryExecutor onCreateNetworkingExecutor() {
		return new TestAuxiliaryExecutor(this);
	}

	@Override
	protected AuxiliaryExecutor onCreateProcessingExecutor() {
		return new TestAuxiliaryExecutor(this);
	}
}
