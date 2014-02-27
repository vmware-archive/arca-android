package com.arca.service;


public interface TaskObserver {

	public void onTaskStarted(Task<?> task);

	public void onTaskComplete(Task<?> task);

	public void onTaskFailure(Task<?> task, ServiceError error);
}