package com.arca.service.test.mock;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.arca.threading.AuxiliaryExecutor;
import com.arca.threading.AuxiliaryExecutorObserver;
import com.arca.threading.PrioritizableRequest;
import com.arca.threading.Identifier;

public class TestAuxiliaryExecutor implements AuxiliaryExecutor {

	private final Set<Identifier<?>> mIdentifiers = new HashSet<Identifier<?>>();
	private final AuxiliaryExecutorObserver mObserver;
	
	public TestAuxiliaryExecutor(final AuxiliaryExecutorObserver observer) {
		mObserver = observer;
	}
	
	@Override
	public void execute(final Runnable command) {
		
		final PrioritizableRequest request = (PrioritizableRequest) command;
		final Identifier<?> identifier = request.getIdentifier();
		
		if (identifier == null) {
			throw new IllegalStateException("Identifier cannot be null.");
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
	public void notifyRequestComplete(final Identifier<?> identifier) {
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
