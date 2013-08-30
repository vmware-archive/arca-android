package com.xtreme.rest.service.test.junit.mock;

import com.xtreme.rest.service.NetworkRequest;
import com.xtreme.rest.service.ProcessingRequest;
import com.xtreme.rest.service.RequestExecutor;
import com.xtreme.threading.AuxiliaryExecutor;
import com.xtreme.threading.AuxiliaryExecutorObserver;
import com.xtreme.threading.PrioritizableRequest;

public class TestRequestExecutor extends RequestExecutor {
	
	@Override
	protected AuxiliaryExecutor onCreateNetworkExecutor() {
		return new TestAuxiliaryExecutor(new AuxiliaryExecutorObserver() {

			@Override
			public void onComplete(final PrioritizableRequest request) {
				onNetworkRequestComplete((NetworkRequest<?>) request);
			}

			@Override
			public void onCancelled(final PrioritizableRequest request) {
				onNetworkRequestCancelled((NetworkRequest<?>) request);
			}
		});
	}

	@Override
	protected AuxiliaryExecutor onCreateProcessingExecutor() {
		return new TestAuxiliaryExecutor(new AuxiliaryExecutorObserver() {

			@Override
			public void onComplete(final PrioritizableRequest request) {
				onProcessingRequestComplete((ProcessingRequest<?>) request);
			}

			@Override
			public void onCancelled(final PrioritizableRequest request) {
				onProcessingRequestCancelled((ProcessingRequest<?>) request);
			}
		});
	}
}
