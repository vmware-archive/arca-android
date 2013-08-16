package com.xtreme.rest.service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.xtreme.threading.AuxiliaryExecutor;
import com.xtreme.threading.AuxiliaryExecutorObserver;
import com.xtreme.threading.PrioritizableRequest;
import com.xtreme.threading.RequestIdentifier;

public class Executor implements RequestHandler, RequestObserver {

	private static final int NUM_NETWORK_THREADS = 2;
	private static final int NUM_PROCESSING_THREADS = 1;
	private static final long THREAD_KEEP_ALIVE_TIME = 15;

	private final IdentifierMap<NetworkRequest<?>> mNetworkMap = new IdentifierMap<NetworkRequest<?>>();
	private final IdentifierMap<ProcessingRequest<?>> mProcessingMap = new IdentifierMap<ProcessingRequest<?>>();
	
	private AuxiliaryExecutor mNetworkExecutor;
	private AuxiliaryExecutor mProcessingExecutor;
	
	public Executor() {
		buildNetworkExecutor();
		buildProcessingExecutor();
	}

	private void buildNetworkExecutor() {
		final AuxiliaryExecutor.Builder builder = new AuxiliaryExecutor.Builder(Priority.newAccessorArray(), mNetworkObserver);
		builder.setCorePoolSize(NUM_NETWORK_THREADS);
		builder.setKeepAliveTime(THREAD_KEEP_ALIVE_TIME, TimeUnit.SECONDS);
		mNetworkExecutor = builder.create();
	}

	private void buildProcessingExecutor() {
		final AuxiliaryExecutor.Builder builder = new AuxiliaryExecutor.Builder(Priority.newAccessorArray(), mProcessingObserver);
		builder.setCorePoolSize(NUM_PROCESSING_THREADS);
		builder.setKeepAliveTime(THREAD_KEEP_ALIVE_TIME, TimeUnit.SECONDS);
		mProcessingExecutor = builder.create();
	}

	// ======================================================

	public int getOperationCount() {
		synchronized (Executor.this) {
			final int networkCount = mNetworkExecutor.getQueue().size() + mNetworkExecutor.getActiveCount();
			final int processingCount = mProcessingExecutor.getQueue().size() + mProcessingExecutor.getActiveCount();
			return networkCount + processingCount;
		}
	}

	public boolean isEmpty() {
		return getOperationCount() == 0;
	}

	// ======================================================

	@Override
	public void executeNetworkRequest(final NetworkRequest<?> request) {
		synchronized (Executor.this) {
			final RequestIdentifier<?> identifier = request.getRequestIdentifier();
			mNetworkMap.add(identifier, request);
			mNetworkExecutor.execute(request);
		}
	}

	@Override
	public void executeProcessingRequest(final ProcessingRequest<?> request) {
		synchronized (Executor.this) {
			final RequestIdentifier<?> identifier = request.getRequestIdentifier();
			mProcessingMap.add(identifier, request);
			mProcessingExecutor.execute(request);
		}
	}
	
	@Override
	public void onNetworkRequestComplete(final NetworkRequest<?> request) {
		synchronized (Executor.this) {
			final Object data = request.getData();
			final ServiceError error = request.getError();
			
			final RequestIdentifier<?> identifier = request.getRequestIdentifier();
			final Set<NetworkRequest<?>> set = mNetworkMap.remove(identifier);
			
			for (final NetworkRequest<?> prioritizable : set) {
				prioritizable.notifyComplete(data, error);
			}
		
			mNetworkExecutor.notifyRequestComplete(identifier);
		}
	}
	
	@Override
	public void onProcessingRequestComplete(final ProcessingRequest<?> request) {
		synchronized (Executor.this) {
			final ServiceError error = request.getError();

			final RequestIdentifier<?> identifier = request.getRequestIdentifier();
			final Set<ProcessingRequest<?>> set = mProcessingMap.remove(identifier);
			
			for (final ProcessingRequest<?> prioritizable : set) {
				prioritizable.notifyComplete(error);
			}
			
			mProcessingExecutor.notifyRequestComplete(identifier);
		}
	}

	// ======================================================

	private final AuxiliaryExecutorObserver mNetworkObserver = new AuxiliaryExecutorObserver() {

		@Override
		protected void onComplete(final PrioritizableRequest request) {
			onNetworkRequestComplete((NetworkRequest<?>) request);
		}

		@Override
		protected void onCancelled(final PrioritizableRequest request) {
			// do nothing
		}
	};

	private final AuxiliaryExecutorObserver mProcessingObserver = new AuxiliaryExecutorObserver() {

		@Override
		protected void onComplete(final PrioritizableRequest request) {
			onProcessingRequestComplete((ProcessingRequest<?>) request);
		}

		@Override
		protected void onCancelled(final PrioritizableRequest request) {
			// do nothing
		}
	};
}
