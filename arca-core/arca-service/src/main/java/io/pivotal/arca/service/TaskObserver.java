package io.pivotal.arca.service;

public interface TaskObserver {

	public void onTaskStarted(Task<?> task);

	public void onTaskComplete(Task<?> task);

	public void onTaskFailure(Task<?> task, ServiceError error);

    public void onTaskCancelled(Task<?> task);
}
