package com.xtreme.rest.service.test.mock;

import com.xtreme.rest.service.RequestExecutor.DefaultRequestExecutor;
import com.xtreme.threading.AuxiliaryExecutor;

public class TestDefaultRequestExecutor extends DefaultRequestExecutor {
	
	@Override
	protected AuxiliaryExecutor onCreateNetworkExecutor() {
		return new TestAuxiliaryExecutor(this);
	}

	@Override
	protected AuxiliaryExecutor onCreateProcessingExecutor() {
		return new TestAuxiliaryExecutor(this);
	}
}
