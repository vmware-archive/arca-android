package com.xtreme.rest.service;

import com.xtreme.threading.PrioritizableRequest;

public interface PrioritizableHandler {
	public void executeNetworkComponent(PrioritizableRequest request);
	public void executeProcessingComponent(PrioritizableRequest request);
}
