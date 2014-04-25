package com.xtreme.rest.service;

import com.xtreme.threading.PrioritizableRequest;

interface PrioritizableHandler {
	void executeNetworkComponent(PrioritizableRequest request);
	void executeProcessingComponent(PrioritizableRequest request);
}
