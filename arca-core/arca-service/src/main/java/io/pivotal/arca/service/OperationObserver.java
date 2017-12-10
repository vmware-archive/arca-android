package io.pivotal.arca.service;

public interface OperationObserver {
    void onOperationComplete(Operation operation);
}
