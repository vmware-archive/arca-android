package io.pivotal.arca.service;

public interface TaskObserver {

	void onTaskStarted(Task<?> task);

	void onTaskComplete(Task<?> task);

	void onTaskFailure(Task<?> task, ServiceError error);

    void onTaskCancelled(Task<?> task);
}
