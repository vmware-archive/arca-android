package com.xtreme.rest.service.test.mock;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.xtreme.threading.AuxiliaryExecutor;
import com.xtreme.threading.AuxiliaryExecutorObserver;
import com.xtreme.threading.PrioritizableRequest;
import com.xtreme.threading.RequestIdentifier;

public class TestAuxiliaryExecutor implements AuxiliaryExecutor {

	private final Set<RequestIdentifier<?>> mIdentifiers = new HashSet<RequestIdentifier<?>>();
	private final AuxiliaryExecutorObserver mObserver;
	
	public TestAuxiliaryExecutor(final AuxiliaryExecutorObserver observer) {
		mObserver = observer;
	}
	
	@Override
	public void execute(final Runnable command) {
		
		final PrioritizableRequest request = (PrioritizableRequest) command;
		final RequestIdentifier<?> identifier = request.getRequestIdentifier();
		
		if (identifier == null) {
			throw new IllegalStateException("RequestIdentifier cannot be null.");
		}
		
		if (!mIdentifiers.contains(identifier)) {
			mIdentifiers.add(identifier);
			
			// Run synchronously
			request.run();
			
			// Notify immediately
			if (request.isCancelled()) {
				mObserver.onCancelled(request);
			} else {
				mObserver.onComplete(request);
			}
		}
	}

	@Override
	public void notifyRequestComplete(final RequestIdentifier<?> identifier) {
		mIdentifiers.remove(identifier);
	}

	@Override
	public BlockingQueue<Runnable> getQueue() {
		return new ArrayBlockingQueue<Runnable>(1);
	}

	@Override
	public int getActiveCount() {
		return mIdentifiers.size();
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
