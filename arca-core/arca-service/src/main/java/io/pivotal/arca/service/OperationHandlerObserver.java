/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.service;

public interface OperationHandlerObserver {
    public void onOperationStarted(Operation operation);
    public void onOperationFinished(Operation operation);
}