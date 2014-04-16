/* 
 * Copyright (C) 2014 Pivotal Software, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pivotal.arca.service.test.mock;

import android.net.Uri;

import io.pivotal.arca.service.Task;

import java.util.HashSet;
import java.util.Set;

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
