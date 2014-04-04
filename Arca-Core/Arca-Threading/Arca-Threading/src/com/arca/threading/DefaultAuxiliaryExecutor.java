package com.arca.threading;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DefaultAuxiliaryExecutor extends ThreadPoolExecutor implements AuxiliaryExecutor {

	private final QueuingMaps mQueuingMaps = new QueuingMaps();

	private final AuxiliaryExecutorObserver mObserver;

	public DefaultAuxiliaryExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final TimeUnit unit, final AuxiliaryBlockingQueue queue,
			final AuxiliaryExecutorObserver observer) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, queue);
		mObserver = observer;
	}

	@Override
	public void execute(final Runnable command) {
		final PrioritizableRequest request = (PrioritizableRequest) command;
		if (request.isCancelled()) {
			return;
		}

		if (!isShutdown()) {
			mQueuingMaps.put(request);
		}
		super.execute(request);
	}

	@Override
	public void notifyRequestComplete(final Identifier<?> identifier) {
		mQueuingMaps.onComplete(identifier);
	}

	@Override
	public boolean remove(final Runnable task) {
		final PrioritizableRequest request = (PrioritizableRequest) task;
		mQueuingMaps.cancel(request);
		return super.remove(task); // TODO need to actually make this work
	}

	@Override
	protected final void beforeExecute(final Thread t, final Runnable r) {
		notifyBeforeExecuteCalled(r);
		super.beforeExecute(t, r);
	}

	@Override
	public void cancelAll() {
		mQueuingMaps.cancelAll();
	}

	@Override
	public List<Runnable> shutdownNow() {
		mQueuingMaps.cancelAll();
		return super.shutdownNow();
	}

	@Override
	protected final void afterExecute(final Runnable r, final Throwable t) {
		super.afterExecute(r, t);
		final PrioritizableRequest request = (PrioritizableRequest) r;
		notifyReferenceManager(request);
	}

	private void notifyReferenceManager(final PrioritizableRequest request) {
		if (mObserver != null) {
			if (!request.isCancelled()) {
				mObserver.onComplete(request);
			} else {
				mObserver.onCancelled(request);
			}
		}
	}

	private void notifyBeforeExecuteCalled(final Runnable r) {
		final PrioritizableRequest request = (PrioritizableRequest) r;
		if (!request.isCancelled())
			mQueuingMaps.notifyExecuting(request);
	}
}
