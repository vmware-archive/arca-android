package com.arca.service.test.mock;

import java.util.HashSet;
import java.util.Set;

import android.net.Uri;

import com.arca.service.Task;

public class TestOperationFactory {

	public static TestOperation newOperationWithUri(final Uri uri) {
		final TestOperation operation = new TestOperation(uri, null);
		operation.setContext(null);
		return operation;
	}
	
	public static TestOperation newOperationWithoutTasks() {
		final Set<Task<?>> tasks = null;
		return newOperationWithTasks(tasks);
	}
	
	public static TestOperation newOperationWithTask() {
		final Set<Task<?>> tasks = new HashSet<Task<?>>();
		tasks.add(TestTaskFactory.newTask());
		return newOperationWithTasks(tasks);
	}

	public static TestOperation newOperationWithTaskThatThrowsNetworkingException(final Exception exception) {
		final Set<Task<?>> tasks = new HashSet<Task<?>>();
		tasks.add(TestTaskFactory.newTaskThatThrowsNetworkingException(exception));
		return newOperationWithTasks(tasks);
	}

	public static TestOperation newOperationWithTaskThatThrowsProcessingException(final Exception exception) {
		final Set<Task<?>> tasks = new HashSet<Task<?>>();
		tasks.add(TestTaskFactory.newTaskThatThrowsProcessingException(exception));
		return newOperationWithTasks(tasks);
	}

	public static TestOperation newOperationWithTaskPrerequisites() {
		final Set<Task<?>> tasks = new HashSet<Task<?>>();
		tasks.addAll(TestTaskFactory.newTaskListWithPrerequisites());
		return newOperationWithTasks(tasks);
	}

	public static TestOperation newOperationWithTaskPrerequisitesThatThrowsNetworkingException(final Exception exception) {
		final Set<Task<?>> tasks = new HashSet<Task<?>>();
		tasks.addAll(TestTaskFactory.newTaskListWithPrerequisitesFirstTaskFailsWithNetworkingException(exception));
		return newOperationWithTasks(tasks);
	}

	public static TestOperation newOperationWithTaskPrerequisitesThatThrowsProcessingException(final Exception exception) {
		final Set<Task<?>> tasks = new HashSet<Task<?>>();
		tasks.addAll(TestTaskFactory.newTaskListWithPrerequisitesFirstTaskFailsWithProcessingException(exception));
		return newOperationWithTasks(tasks);
	}

	public static TestOperation newOperationWithTaskDependencies() {
		final Set<Task<?>> tasks = new HashSet<Task<?>>();
		tasks.addAll(TestTaskFactory.newTaskListWithDependencies());
		return newOperationWithTasks(tasks);
	}

	public static TestOperation newOperationWithTaskDependenciesThatThrowsNetworkingException(final Exception exception) {
		final Set<Task<?>> tasks = new HashSet<Task<?>>();
		tasks.addAll(TestTaskFactory.newTaskListWithDependenciesFirstTaskFailsWithNetworkingException(exception));
		return newOperationWithTasks(tasks);
	}


	public static TestOperation newOperationWithTaskDependenciesThatThrowsProcessingException(final Exception exception) {
		final Set<Task<?>> tasks = new HashSet<Task<?>>();
		tasks.addAll(TestTaskFactory.newTaskListWithDependenciesFirstTaskFailsWithProcessingException(exception));
		return newOperationWithTasks(tasks);
	}
	
	public static TestOperation newOperationWithDynamicTaskDependency() {
		final Set<Task<?>> tasks = new HashSet<Task<?>>();
		tasks.addAll(TestTaskFactory.newTaskListWithDynamicDependencies());
		return newOperationWithTasks(tasks);
	}
	
	public static TestOperation newOperationWithDynamicTaskDependenciesThatThrowsNetworkingException(final Exception exception) {
		final Set<Task<?>> tasks = new HashSet<Task<?>>();
		tasks.addAll(TestTaskFactory.newTaskListWithDynamicDependenciesSecondTaskFailsWithNetworkingException(exception));
		return newOperationWithTasks(tasks);
	}
	
	public static TestOperation newOperationWithDynamicTaskDependenciesThatThrowsProcessingException(final Exception exception) {
		final Set<Task<?>> tasks = new HashSet<Task<?>>();
		tasks.addAll(TestTaskFactory.newTaskListWithDynamicDependenciesSecondTaskFailsWithProcessingException(exception));
		return newOperationWithTasks(tasks);
	}
	
	private static TestOperation newOperationWithTasks(final Set<Task<?>> tasks) {
		final TestOperation operation = new TestOperation(tasks);
		operation.setContext(null);
		return operation;
	}

}
