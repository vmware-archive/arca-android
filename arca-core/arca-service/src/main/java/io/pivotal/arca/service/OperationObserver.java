package io.pivotal.arca.service;

public interface OperationObserver {
    public void onOperationComplete(Operation operation);
}
