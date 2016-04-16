package io.pivotal.arca.service;

public interface OperationHandlerObserver {
    public void onOperationStarted(Operation operation);
    public void onOperationFinished(Operation operation);
}
