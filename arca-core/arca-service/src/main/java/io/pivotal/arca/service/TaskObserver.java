/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.service;

public interface TaskObserver {

	public void onTaskStarted(Task<?> task);

	public void onTaskComplete(Task<?> task);

	public void onTaskFailure(Task<?> task, ServiceError error);

    public void onTaskCancelled(Task<?> task);
}