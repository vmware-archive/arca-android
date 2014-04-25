package com.xtreme.rest.service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.xtreme.threading.AuxiliaryExecutor;
import com.xtreme.threading.AuxiliaryExecutorObserver;
import com.xtreme.threading.PrioritizableRequest;
import com.xtreme.threading.RequestIdentifier;

public class Executor implements PrioritizableHandler {

	private static final int NUM_NETWORK_THREADS = 2;
	private static final int NUM_PROCESSING_THREADS = 1;
	private static final long THREAD_KEEP_ALIVE_TIME = 15;

	private final IdentifierMap<NetworkPrioritizable<?>> mNetworkMap = new IdentifierMap<NetworkPrioritizable<?>>();
	private final IdentifierMap<ProcessingPrioritizable<?>> mProcessingMap = new IdentifierMap<ProcessingPrioritizable<?>>();
	
	private AuxiliaryExecutor mNetworkExecutor;
	private AuxiliaryExecutor mProcessingExecutor;
	
	public Executor() {
		buildNetworkExecutor();
		buildProcessingExecutor();
	}

	private void buildProcessingExecutor() {
		final AuxiliaryExecutor.Builder builder = new AuxiliaryExecutor.Builder(Priority.newAccessorArray(), mProcessingObserver);
		builder.setCorePoolSize(NUM_PROCESSING_THREADS);
		builder.setKeepAliveTime(THREAD_KEEP_ALIVE_TIME, TimeUnit.SECONDS);
		mProcessingExecutor = builder.create();
	}

	private void buildNetworkExecutor() {
		final AuxiliaryExecutor.Builder builder = new AuxiliaryExecutor.Builder(Priority.newAccessorArray(), mNetworkObserver);
		builder.setCorePoolSize(NUM_NETWORK_THREADS);
		builder.setKeepAliveTime(THREAD_KEEP_ALIVE_TIME, TimeUnit.SECONDS);
		mNetworkExecutor = builder.create();
	}

	// ======================================================

	public synchronized int getOperationCount() {
		final int networkCount = mNetworkExecutor.getQueue().size() + mNetworkExecutor.getActiveCount();
		final int processingCount = mProcessingExecutor.getQueue().size() + mProcessingExecutor.getActiveCount();
		return networkCount + processingCount;
	}

	public boolean isEmpty() {
		return getOperationCount() == 0;
	}

	// ======================================================

	@Override
	public synchronized void executeNetworkComponent(final PrioritizableRequest request) {
		final RequestIdentifier<?> identifier = request.getRequestIdentifier();
		mNetworkMap.add(identifier, (NetworkPrioritizable<?>) request.getPrioritizable());
		mNetworkExecutor.execute(request);
	}

	@Override
	public synchronized void executeProcessingComponent(final PrioritizableRequest request) {
		final RequestIdentifier<?> identifier = request.getRequestIdentifier();
		mProcessingMap.add(identifier, (ProcessingPrioritizable<?>) request.getPrioritizable());
		mProcessingExecutor.execute(request);
	}

	public void destroy() {
		mNetworkExecutor.shutdownNow();
		mProcessingExecutor.shutdownNow();
	}

	// ======================================================

	private final AuxiliaryExecutorObserver mNetworkObserver = new AuxiliaryExecutorObserver() {

		@Override
		protected void onComplete(final PrioritizableRequest request) {
			
			synchronized (Executor.this) {
				final RequestIdentifier<?> identifier = request.getRequestIdentifier();
				final NetworkPrioritizable<?> result = (NetworkPrioritizable<?>) request.getPrioritizable();
				final Set<NetworkPrioritizable<?>> set = mNetworkMap.remove(identifier);
				
				final Object data = result.getData();
				final ServiceError error = result.getError();
				
				for (final NetworkPrioritizable<?> prioritizable : set) {
					prioritizable.onComplete(data, error);
				}
			
				mNetworkExecutor.notifyRequestComplete(identifier);
			}
		}

		@Override
		protected void onCancelled(final PrioritizableRequest request) {
			// do nothing
		}
	};

	private final AuxiliaryExecutorObserver mProcessingObserver = new AuxiliaryExecutorObserver() {

		@Override
		protected void onComplete(final PrioritizableRequest request) {
			
			synchronized (Executor.this) {
				final RequestIdentifier<?> identifier = request.getRequestIdentifier();
				final ProcessingPrioritizable<?> result = (ProcessingPrioritizable<?>) request.getPrioritizable();
				final Set<ProcessingPrioritizable<?>> set = mProcessingMap.remove(identifier);
				
				final ServiceError error = result.getError();
				
				for (final ProcessingPrioritizable<?> prioritizable : set) {
					prioritizable.onComplete(error);
				}
				
				mProcessingExecutor.notifyRequestComplete(identifier);
			}
		}

		@Override
		protected void onCancelled(final PrioritizableRequest request) {
			// do nothing
		}
	};
}
