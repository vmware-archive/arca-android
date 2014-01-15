package com.xtreme.rest.service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.xtreme.threading.AuxiliaryExecutor;
import com.xtreme.threading.AuxiliaryExecutorObserver;
import com.xtreme.threading.PrioritizableRequest;
import com.xtreme.threading.RequestIdentifier;

public interface RequestExecutor {
	public void executeNetworkRequest(NetworkRequest<?> request);
	public void executeProcessingRequest(ProcessingRequest<?> request);
	
	public static class DefaultRequestExecutor implements AuxiliaryExecutorObserver, RequestExecutor, RequestObserver {
		
		public static final class Config {
			public static final int NUM_NETWORK_THREADS = 2;
			public static final int NUM_PROCESSING_THREADS = 1;
			public static final long THREAD_KEEP_ALIVE_TIME = 15;
		}

		private final IdentifierMap<NetworkRequest<?>> mNetworkMap = new IdentifierMap<NetworkRequest<?>>();
		private final IdentifierMap<ProcessingRequest<?>> mProcessingMap = new IdentifierMap<ProcessingRequest<?>>();

		private final AuxiliaryExecutor mNetworkExecutor;
		private final AuxiliaryExecutor mProcessingExecutor;
		
		public DefaultRequestExecutor() {
			mNetworkExecutor = onCreateNetworkExecutor();
			mProcessingExecutor = onCreateProcessingExecutor();
		}
		
		protected AuxiliaryExecutor onCreateNetworkExecutor() {
			final AuxiliaryExecutor.Builder builder = new AuxiliaryExecutor.Builder(Priority.newAccessorArray(), this);
			builder.setCorePoolSize(Config.NUM_NETWORK_THREADS);
			builder.setKeepAliveTime(Config.THREAD_KEEP_ALIVE_TIME, TimeUnit.SECONDS);
			return builder.create();
		}

		protected AuxiliaryExecutor onCreateProcessingExecutor() {
			final AuxiliaryExecutor.Builder builder = new AuxiliaryExecutor.Builder(Priority.newAccessorArray(), this);
			builder.setCorePoolSize(Config.NUM_PROCESSING_THREADS);
			builder.setKeepAliveTime(Config.THREAD_KEEP_ALIVE_TIME, TimeUnit.SECONDS);
			return builder.create();
		}

		// ======================================================

		public int getRequestCount() {
			synchronized (DefaultRequestExecutor.this) {
				final int networkCount = mNetworkExecutor.getQueue().size() + mNetworkExecutor.getActiveCount();
				final int processingCount = mProcessingExecutor.getQueue().size() + mProcessingExecutor.getActiveCount();
				return networkCount + processingCount;
			}
		}

		public boolean isEmpty() {
			return getRequestCount() == 0;
		}

		// ======================================================

		@Override
		public void executeNetworkRequest(final NetworkRequest<?> request) {
			synchronized (DefaultRequestExecutor.this) {
				final RequestIdentifier<?> identifier = request.getRequestIdentifier();
				mNetworkMap.add(identifier, request);
				mNetworkExecutor.execute(request);
			}
		}
		
		@Override
		public void executeProcessingRequest(final ProcessingRequest<?> request) {
			synchronized (DefaultRequestExecutor.this) {
				final RequestIdentifier<?> identifier = request.getRequestIdentifier();
				mProcessingMap.add(identifier, request);
				mProcessingExecutor.execute(request);
			}
		}
		
		@Override
		public void onComplete(final PrioritizableRequest request) {
			
			if (request instanceof NetworkRequest) { 
				onNetworkRequestComplete((NetworkRequest<?>) request);
			}
			
			if (request instanceof ProcessingRequest) { 
				onProcessingRequestComplete((ProcessingRequest<?>) request);
			}
		}
		
		@Override
		public void onCancelled(final PrioritizableRequest request) {
			
			if (request instanceof NetworkRequest) { 
				onNetworkRequestCancelled((NetworkRequest<?>) request);
			}
			
			if (request instanceof ProcessingRequest) { 
				onProcessingRequestCancelled((ProcessingRequest<?>) request);
			}
		}
		
		@Override
		public void onNetworkRequestComplete(final NetworkRequest<?> request) {
			synchronized (DefaultRequestExecutor.this) {
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
			synchronized (DefaultRequestExecutor.this) {
				final ServiceError error = request.getError();

				final RequestIdentifier<?> identifier = request.getRequestIdentifier();
				final Set<ProcessingRequest<?>> set = mProcessingMap.remove(identifier);
				
				for (final ProcessingRequest<?> prioritizable : set) {
					prioritizable.notifyComplete(error);
				}
				
				mProcessingExecutor.notifyRequestComplete(identifier);
			}
		}
		
		@Override
		public void onNetworkRequestCancelled(final NetworkRequest<?> request) {
			// do nothing
		}
		
		@Override
		public void onProcessingRequestCancelled(final ProcessingRequest<?> request) {
			// do nothing
		}
		
	}
}
