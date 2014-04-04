package com.arca.service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.arca.threading.AuxiliaryExecutor;
import com.arca.threading.AuxiliaryExecutorObserver;
import com.arca.threading.Identifier;
import com.arca.threading.PrioritizableRequest;

public interface RequestExecutor {
	public void executeNetworkingRequest(NetworkingRequest<?> request);
	public void executeProcessingRequest(ProcessingRequest<?> request);
	
	public static class SerialRequestExecutor implements RequestExecutor {

		@Override
		public void executeNetworkingRequest(final NetworkingRequest<?> request) {
			request.run();
			request.notifyComplete(request.getData(), request.getError());
		}

		@Override
		public void executeProcessingRequest(final ProcessingRequest<?> request) {
			request.run();
			request.notifyComplete(request.getError());
		}
	}
	
	public static class ThreadedRequestExecutor implements RequestExecutor, RequestObserver, AuxiliaryExecutorObserver {
		
		public static interface Config {
			public static final int NUM_NETWORK_THREADS = 2;
			public static final int NUM_PROCESSING_THREADS = 1;
			public static final long THREAD_KEEP_ALIVE_TIME = 15;
		}

		private final IdentifierMap<NetworkingRequest<?>> mNetworkMap = new IdentifierMap<NetworkingRequest<?>>();
		private final IdentifierMap<ProcessingRequest<?>> mProcessingMap = new IdentifierMap<ProcessingRequest<?>>();

		private final AuxiliaryExecutor mNetworkExecutor;
		private final AuxiliaryExecutor mProcessingExecutor;
		
		public ThreadedRequestExecutor() {
			mNetworkExecutor = onCreateNetworkingExecutor();
			mProcessingExecutor = onCreateProcessingExecutor();
		}
		
		protected AuxiliaryExecutor onCreateNetworkingExecutor() {
			final AuxiliaryExecutor.Builder builder = new AuxiliaryExecutor.Builder(Priority.newAccessorArray(), this);
			builder.setKeepAliveTime(Config.THREAD_KEEP_ALIVE_TIME, TimeUnit.SECONDS);
			builder.setCorePoolSize(Config.NUM_NETWORK_THREADS);
			builder.allowCoreThreadTimeOut();
			return builder.create();
		}

		protected AuxiliaryExecutor onCreateProcessingExecutor() {
			final AuxiliaryExecutor.Builder builder = new AuxiliaryExecutor.Builder(Priority.newAccessorArray(), this);
			builder.setKeepAliveTime(Config.THREAD_KEEP_ALIVE_TIME, TimeUnit.SECONDS);
			builder.setCorePoolSize(Config.NUM_PROCESSING_THREADS);
			builder.allowCoreThreadTimeOut();
			return builder.create();
		}

		// ======================================================

		public int getRequestCount() {
			synchronized (ThreadedRequestExecutor.this) {
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
		public void executeNetworkingRequest(final NetworkingRequest<?> request) {
			synchronized (ThreadedRequestExecutor.this) {
				final Identifier<?> identifier = request.getIdentifier();
				mNetworkMap.add(identifier, request);
				mNetworkExecutor.execute(request);
			}
		}
		
		@Override
		public void executeProcessingRequest(final ProcessingRequest<?> request) {
			synchronized (ThreadedRequestExecutor.this) {
				final Identifier<?> identifier = request.getIdentifier();
				mProcessingMap.add(identifier, request);
				mProcessingExecutor.execute(request);
			}
		}
		
		@Override
		public void onComplete(final PrioritizableRequest request) {
			
			if (request instanceof NetworkingRequest) { 
				onNetworkingRequestComplete((NetworkingRequest<?>) request);
			}
			
			if (request instanceof ProcessingRequest) { 
				onProcessingRequestComplete((ProcessingRequest<?>) request);
			}
		}
		
		@Override
		public void onCancelled(final PrioritizableRequest request) {
			
			if (request instanceof NetworkingRequest) { 
				onNetworkingRequestCancelled((NetworkingRequest<?>) request);
			}
			
			if (request instanceof ProcessingRequest) { 
				onProcessingRequestCancelled((ProcessingRequest<?>) request);
			}
		}
		
		@Override
		public void onNetworkingRequestComplete(final NetworkingRequest<?> request) {
			synchronized (ThreadedRequestExecutor.this) {
				final Object data = request.getData();
				final ServiceError error = request.getError();
				
				final Identifier<?> identifier = request.getIdentifier();
				final Set<NetworkingRequest<?>> set = mNetworkMap.remove(identifier);
				
				for (final NetworkingRequest<?> prioritizable : set) {
					prioritizable.notifyComplete(data, error);
				}
			
				mNetworkExecutor.notifyRequestComplete(identifier);
			}
		}
		
		@Override
		public void onProcessingRequestComplete(final ProcessingRequest<?> request) {
			synchronized (ThreadedRequestExecutor.this) {
				final ServiceError error = request.getError();

				final Identifier<?> identifier = request.getIdentifier();
				final Set<ProcessingRequest<?>> set = mProcessingMap.remove(identifier);
				
				for (final ProcessingRequest<?> prioritizable : set) {
					prioritizable.notifyComplete(error);
				}
				
				mProcessingExecutor.notifyRequestComplete(identifier);
			}
		}
		
		@Override
		public void onNetworkingRequestCancelled(final NetworkingRequest<?> request) {
			// do nothing
		}
		
		@Override
		public void onProcessingRequestCancelled(final ProcessingRequest<?> request) {
			// do nothing
		}
		
	}
}
