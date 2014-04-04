package com.arca.threading;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class QueuingMaps {

	private final Set<RequestIdentifier<?>> mRunningRequests = Collections.newSetFromMap(new ConcurrentHashMap<RequestIdentifier<?>, Boolean>());
	private final Map<RequestIdentifier<?>, List<PrioritizableRequest>> mRequestListeners = new ConcurrentHashMap<RequestIdentifier<?>, List<PrioritizableRequest>>();

	public synchronized void put(final PrioritizableRequest request) {
		final RequestIdentifier<?> identifier = request.getIdentifier();

		if (mRunningRequests.contains(identifier)) {
			request.cancel();
			return;
		}

		List<PrioritizableRequest> list = mRequestListeners.get(identifier);
		if (list == null) {
			list = new ArrayList<PrioritizableRequest>();
			mRequestListeners.put(identifier, list);
		}
		list.add(request);
	}

	public synchronized void onComplete(final RequestIdentifier<?> request) {
		mRunningRequests.remove(request);
	}

	public synchronized void notifyExecuting(final PrioritizableRequest request) {
		final RequestIdentifier<?> identifier = request.getIdentifier();

		if (mRunningRequests.contains(identifier)) {
			request.cancel();
		} else {
			mRunningRequests.add(identifier);
			final List<PrioritizableRequest> prioritizables = mRequestListeners.remove(identifier);
			if (prioritizables != null) {
				for (final PrioritizableRequest r : prioritizables) {
					if (r != request)
						r.cancel();
				}
			}
		}
	}

	public synchronized boolean cancel(final PrioritizableRequest request) {
		final RequestIdentifier<?> identifier = request.getIdentifier();
		final List<PrioritizableRequest> list = mRequestListeners.get(identifier);
		if (list != null) {
			list.remove(request);
			if (list.size() == 0)
				mRequestListeners.remove(identifier);
		}
		request.cancel();
		return mRunningRequests.contains(identifier);
	}

	public synchronized void cancelAll() {
		final List<PrioritizableRequest> requestList = new ArrayList<PrioritizableRequest>();
		for (final Entry<RequestIdentifier<?>, List<PrioritizableRequest>> entry : mRequestListeners.entrySet()) {
			for (final PrioritizableRequest request : entry.getValue()) {
				requestList.add(request);
			}
		}

		for (final PrioritizableRequest request : requestList) {
			cancel(request);
		}
	}
}
