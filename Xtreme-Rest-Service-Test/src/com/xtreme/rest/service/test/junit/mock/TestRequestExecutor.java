package com.xtreme.rest.service.test.junit.mock;

import com.xtreme.rest.service.RequestExecutor;
import com.xtreme.threading.AuxiliaryExecutor;

public class TestRequestExecutor extends RequestExecutor {
	
	@Override
	protected AuxiliaryExecutor onCreateNetworkExecutor() {
		return new TestAuxiliaryExecutor(this);
	}

	@Override
	protected AuxiliaryExecutor onCreateProcessingExecutor() {
		return new TestAuxiliaryExecutor(this);
	}
}
