package com.xtreme.rest.service.test.junit.mock;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.xtreme.threading.AuxiliaryExecutor;
import com.xtreme.threading.AuxiliaryExecutorObserver;
import com.xtreme.threading.PrioritizableRequest;
import com.xtreme.threading.RequestIdentifier;

public class TestAuxiliaryExecutor implements AuxiliaryExecutor {

	private final AuxiliaryExecutorObserver mObserver;
	private Runnable mCurrent;
	
	public TestAuxiliaryExecutor(final AuxiliaryExecutorObserver observer) {
		mObserver = observer;
	}
	
	@Override
	public void execute(final Runnable command) {
		
		mCurrent = command;
		
		// Tell the request to run synchronously
		command.run();
		
		mCurrent = null;
		
		// Notify Immediately
		mObserver.onComplete((PrioritizableRequest) command);
	}

	@Override
	public void notifyRequestComplete(final RequestIdentifier<?> identifier) {
		// do nothing
	}

	@Override
	public BlockingQueue<Runnable> getQueue() {
		return new ArrayBlockingQueue<Runnable>(1);
	}

	@Override
	public int getActiveCount() {
		return mCurrent != null ? 1 : 0;
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
