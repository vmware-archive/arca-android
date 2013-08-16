package com.xtreme.rest.service;

public interface RequestHandler {
	public void executeNetworkRequest(NetworkRequest<?> request);
	public void executeProcessingRequest(ProcessingRequest<?> request);
}
