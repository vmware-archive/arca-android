/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.threading;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class QueuingMaps {

	private final Set<Identifier<?>> mRunningRequests = Collections.newSetFromMap(new ConcurrentHashMap<Identifier<?>, Boolean>());
	private final Map<Identifier<?>, List<PrioritizableRequest>> mRequestListeners = new ConcurrentHashMap<Identifier<?>, List<PrioritizableRequest>>();

	public synchronized void put(final PrioritizableRequest request) {
		final Identifier<?> identifier = request.getIdentifier();

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

	public synchronized void onComplete(final Identifier<?> request) {
		mRunningRequests.remove(request);
	}

	public synchronized void notifyExecuting(final PrioritizableRequest request) {
		final Identifier<?> identifier = request.getIdentifier();

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
		final Identifier<?> identifier = request.getIdentifier();
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
		for (final Entry<Identifier<?>, List<PrioritizableRequest>> entry : mRequestListeners.entrySet()) {
			for (final PrioritizableRequest request : entry.getValue()) {
				requestList.add(request);
			}
		}

		for (final PrioritizableRequest request : requestList) {
			cancel(request);
		}
	}
}
