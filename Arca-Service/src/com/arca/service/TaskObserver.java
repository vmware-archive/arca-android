package com.arca.service;


public interface TaskObserver {
	/**
	 * A callback for when a {@link Task} has just started executing.</br>
	 * </br>
	 * Note: This method is called on the {@link Thread} on which it is being executed.
	 * 
	 * @param task The started {@link Task}
	 */
	public void onTaskStarted(Task<?> task);

	/**
	 * A callback for when a {@link Task} has just completed successfully.</br>
	 * </br>
	 * Note: This method is called on the {@link Thread} on which it is being executed.
	 * 
	 * @param task The completed {@link Task}
	 */
	public void onTaskComplete(Task<?> task);

	/**
	 * A callback for when a {@link Task} has just completed with an error.</br>
	 * </br>
	 * Note: This method is called on the {@link Thread} on which it is being executed.
	 * 
	 * @param task The failed {@link Task}
	 * @param error The {@link ServiceError} that has occurred
	 */
	public void onTaskFailure(Task<?> task, ServiceError error);
}