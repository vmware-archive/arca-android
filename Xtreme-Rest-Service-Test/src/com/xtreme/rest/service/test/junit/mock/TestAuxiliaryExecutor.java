package com.xtreme.rest.service.test.junit.mock;

import java.util.concurrent.BlockingQueue;

import com.xtreme.threading.AuxiliaryExecutor;
import com.xtreme.threading.AuxiliaryExecutorObserver;
import com.xtreme.threading.PrioritizableRequest;
import com.xtreme.threading.RequestIdentifier;

public class TestAuxiliaryExecutor implements AuxiliaryExecutor {

	private final AuxiliaryExecutorObserver mObserver;
	
	public TestAuxiliaryExecutor(final AuxiliaryExecutorObserver observer) {
		mObserver = observer;
	}
	
	@Override
	public void execute(final Runnable command) {
		
		// Tell the request to run synchronously
		command.run();
		
		// Notify Immediately
		mObserver.onComplete((PrioritizableRequest) command);
	}

	@Override
	public void notifyRequestComplete(final RequestIdentifier<?> identifier) {
		throw new UnsupportedOperationException();
	}

	@Override
	public BlockingQueue<Runnable> getQueue() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getActiveCount() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(final Runnable task) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void cancelAll() {
		throw new UnsupportedOperationException();
	}

}
