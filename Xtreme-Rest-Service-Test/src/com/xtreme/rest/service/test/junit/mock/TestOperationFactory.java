package com.xtreme.rest.service.test.junit.mock;

import java.util.HashSet;
import java.util.Set;

import com.xtreme.rest.service.Task;

public class TestOperationFactory {

	public static TestOperation newOperationWithoutTasks() {
		final Set<Task<?>> tasks = null;
		return new TestOperation(tasks);
	}
	
	public static TestOperation newOperationWithPassingTask() {
		final Set<Task<?>> tasks = new HashSet<Task<?>>();
		tasks.add(TestTaskFactory.newTask());
		return new TestOperation(tasks);
	}

	public static TestOperation newOperationWithTaskThatThrowsNetworkException(final Exception exception) {
		final Set<Task<?>> tasks = new HashSet<Task<?>>();
		tasks.add(TestTaskFactory.newTaskThatThrowsNetworkException(exception));
		return new TestOperation(tasks);
	}

	public static TestOperation newOperationWithTaskPrerequisites() {
		final Set<Task<?>> tasks = new HashSet<Task<?>>();
		tasks.addAll(TestTaskFactory.newTaskListWithPrerequisites());
		return new TestOperation(tasks);
	}

	public static TestOperation newOperationWithTaskDependants() {
		final Set<Task<?>> tasks = new HashSet<Task<?>>();
		tasks.addAll(TestTaskFactory.newTaskListWithDependants());
		return new TestOperation(tasks);
	}

}
