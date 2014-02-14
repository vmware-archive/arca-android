package com.xtreme.rest.service.test.mock;

import java.util.HashSet;
import java.util.Set;

import android.net.Uri;

import com.xtreme.rest.service.Task;

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

	public static TestOperation newOperationWithTaskDependants() {
		final Set<Task<?>> tasks = new HashSet<Task<?>>();
		tasks.addAll(TestTaskFactory.newTaskListWithDependants());
		return newOperationWithTasks(tasks);
	}

	public static TestOperation newOperationWithTaskDependantsThatThrowsNetworkingException(final Exception exception) {
		final Set<Task<?>> tasks = new HashSet<Task<?>>();
		tasks.addAll(TestTaskFactory.newTaskListWithDependantsFirstTaskFailsWithNetworkingException(exception));
		return newOperationWithTasks(tasks);
	}


	public static TestOperation newOperationWithTaskDependantsThatThrowsProcessingException(final Exception exception) {
		final Set<Task<?>> tasks = new HashSet<Task<?>>();
		tasks.addAll(TestTaskFactory.newTaskListWithDependantsFirstTaskFailsWithProcessingException(exception));
		return newOperationWithTasks(tasks);
	}
	
	private static TestOperation newOperationWithTasks(final Set<Task<?>> tasks) {
		final TestOperation operation = new TestOperation(tasks);
		operation.setContext(null);
		return operation;
	}

}
