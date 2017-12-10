package io.pivotal.arca.service;

public interface OperationHandlerObserver {
    void onOperationStarted(Operation operation);
    void onOperationFinished(Operation operation);
}
