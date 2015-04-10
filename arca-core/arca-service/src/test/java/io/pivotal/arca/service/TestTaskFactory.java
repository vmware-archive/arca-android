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
package io.pivotal.arca.service;

import java.util.ArrayList;
import java.util.List;

import io.pivotal.arca.threading.Identifier;

public class TestTaskFactory {

	public static TestTask newTask() {
		return new TestTask(new Identifier<String>("task"));
	}

	public static TestTask newTaskWithIdentifier(final Identifier<String> identifier) {
		return new TestTask(identifier);
	}

	public static TestTask newTaskWithNetworkingResult(final String networkResult) {
		return new TestTask(new Identifier<String>("task"), networkResult);
	}

	public static TestTask newTaskThatThrowsNetworkingException(final Exception exception) {
		return new TestTask(new Identifier<String>("task"), null, exception, null);
	}

	public static TestTask newTaskThatThrowsProcessingException(final Exception exception) {
		return new TestTask(new Identifier<String>("task"), null, null, exception);
	}

	private static TestTask newTaskWithDynamicDependency(final Identifier<String> identifier, final Task<?> task) {
		return new TestTask(identifier, task);
	}

	public static List<Task<?>> newTaskListWithPrerequisites() {
		final RequestExecutor.SerialRequestExecutor executor = new RequestExecutor.SerialRequestExecutor();

		final TestTask task1 = TestTaskFactory.newTaskWithIdentifier(new Identifier<String>("task1"));
		task1.setRequestExecutor(executor);

		final TestTask task2 = TestTaskFactory.newTaskWithIdentifier(new Identifier<String>("task2"));
		task2.setRequestExecutor(executor);
		task2.addPrerequisite(task1);

		final List<Task<?>> expectedOrder = new ArrayList<Task<?>>();
		expectedOrder.add(task1);
		expectedOrder.add(task2);

		return expectedOrder;
	}

	public static List<Task<?>> newTaskListWithPrerequisitesFirstTaskFailsWithNetworkingException(final Exception exception) {
		final RequestExecutor.SerialRequestExecutor executor = new RequestExecutor.SerialRequestExecutor();

		final TestTask task1 = TestTaskFactory.newTaskThatThrowsNetworkingException(exception);
		task1.setRequestExecutor(executor);

		final TestTask task2 = TestTaskFactory.newTask();
		task2.setRequestExecutor(executor);
		task2.addPrerequisite(task1);

		final List<Task<?>> expectedOrder = new ArrayList<Task<?>>();
		expectedOrder.add(task1);
		expectedOrder.add(task2);

		return expectedOrder;
	}

	public static List<Task<?>> newTaskListWithPrerequisitesSecondTaskFailsWithNetworkingException(final Exception exception) {
		final RequestExecutor.SerialRequestExecutor executor = new RequestExecutor.SerialRequestExecutor();

		final TestTask task1 = TestTaskFactory.newTaskWithIdentifier(new Identifier<String>("task1"));
		task1.setRequestExecutor(executor);

		final TestTask task2 = TestTaskFactory.newTaskThatThrowsNetworkingException(exception);
		task2.setRequestExecutor(executor);
		task2.addPrerequisite(task1);

		final List<Task<?>> expectedOrder = new ArrayList<Task<?>>();
		expectedOrder.add(task1);
		expectedOrder.add(task2);

		return expectedOrder;
	}

	public static List<Task<?>> newTaskListWithPrerequisitesFirstTaskFailsWithProcessingException(final Exception exception) {
		final RequestExecutor.SerialRequestExecutor executor = new RequestExecutor.SerialRequestExecutor();

		final TestTask task1 = TestTaskFactory.newTaskThatThrowsProcessingException(exception);
		task1.setRequestExecutor(executor);

		final TestTask task2 = TestTaskFactory.newTask();
		task2.setRequestExecutor(executor);
		task2.addPrerequisite(task1);

		final List<Task<?>> expectedOrder = new ArrayList<Task<?>>();
		expectedOrder.add(task1);
		expectedOrder.add(task2);

		return expectedOrder;
	}

	public static List<Task<?>> newTaskListWithPrerequisitesSecondTaskFailsWithProcessingException(final Exception exception) {
		final RequestExecutor.SerialRequestExecutor executor = new RequestExecutor.SerialRequestExecutor();

		final TestTask task1 = TestTaskFactory.newTaskWithIdentifier(new Identifier<String>("task1"));
		task1.setRequestExecutor(executor);

		final TestTask task2 = TestTaskFactory.newTaskThatThrowsProcessingException(exception);
		task2.setRequestExecutor(executor);
		task2.addPrerequisite(task1);

		final List<Task<?>> expectedOrder = new ArrayList<Task<?>>();
		expectedOrder.add(task1);
		expectedOrder.add(task2);

		return expectedOrder;
	}

	public static List<Task<?>> newTaskListWithDependencies() {
		final RequestExecutor.SerialRequestExecutor executor = new RequestExecutor.SerialRequestExecutor();

		final TestTask task2 = TestTaskFactory.newTaskWithIdentifier(new Identifier<String>("task2"));
		task2.setRequestExecutor(executor);

		final TestTask task1 = TestTaskFactory.newTaskWithIdentifier(new Identifier<String>("task1"));
		task1.setRequestExecutor(executor);
		task1.addDependency(task2);

		final List<Task<?>> expectedOrder = new ArrayList<Task<?>>();
		expectedOrder.add(task1);
		expectedOrder.add(task2);

		return expectedOrder;
	}

	public static List<Task<?>> newTaskListWithDependenciesFirstTaskFailsWithNetworkingException(final Exception exception) {
		final RequestExecutor.SerialRequestExecutor executor = new RequestExecutor.SerialRequestExecutor();

		final TestTask task2 = TestTaskFactory.newTaskWithIdentifier(new Identifier<String>("task2"));
		task2.setRequestExecutor(executor);

		final TestTask task1 = TestTaskFactory.newTaskThatThrowsNetworkingException(exception);
		task1.setRequestExecutor(executor);
		task1.addDependency(task2);

		final List<Task<?>> expectedOrder = new ArrayList<Task<?>>();
		expectedOrder.add(task1);
		expectedOrder.add(task2);

		return expectedOrder;
	}

	public static List<Task<?>> newTaskListWithDependenciesSecondTaskFailsWithNetworkingException(final Exception exception) {
		final RequestExecutor.SerialRequestExecutor executor = new RequestExecutor.SerialRequestExecutor();

		final TestTask task2 = TestTaskFactory.newTaskThatThrowsNetworkingException(exception);
		task2.setRequestExecutor(executor);

		final TestTask task1 = TestTaskFactory.newTaskWithIdentifier(new Identifier<String>("task1"));
		task1.setRequestExecutor(executor);
		task1.addDependency(task2);

		final List<Task<?>> expectedOrder = new ArrayList<Task<?>>();
		expectedOrder.add(task1);
		expectedOrder.add(task2);

		return expectedOrder;
	}

	public static List<Task<?>> newTaskListWithDependenciesFirstTaskFailsWithProcessingException(final Exception exception) {
		final RequestExecutor.SerialRequestExecutor executor = new RequestExecutor.SerialRequestExecutor();

		final TestTask task2 = TestTaskFactory.newTaskWithIdentifier(new Identifier<String>("task2"));
		task2.setRequestExecutor(executor);

		final TestTask task1 = TestTaskFactory.newTaskThatThrowsProcessingException(exception);
		task1.setRequestExecutor(executor);
		task1.addDependency(task2);

		final List<Task<?>> expectedOrder = new ArrayList<Task<?>>();
		expectedOrder.add(task1);
		expectedOrder.add(task2);

		return expectedOrder;
	}

	public static List<Task<?>> newTaskListWithDependenciesSecondTaskFailsWithProcessingException(final Exception exception) {
		final RequestExecutor.SerialRequestExecutor executor = new RequestExecutor.SerialRequestExecutor();

		final TestTask task2 = TestTaskFactory.newTaskThatThrowsProcessingException(exception);
		task2.setRequestExecutor(executor);

		final TestTask task1 = TestTaskFactory.newTaskWithIdentifier(new Identifier<String>("task1"));
		task1.setRequestExecutor(executor);
		task1.addDependency(task2);

		final List<Task<?>> expectedOrder = new ArrayList<Task<?>>();
		expectedOrder.add(task1);
		expectedOrder.add(task2);

		return expectedOrder;
	}

	public static List<Task<?>> newTaskListWithDynamicDependencies() {
		final RequestExecutor.SerialRequestExecutor executor = new RequestExecutor.SerialRequestExecutor();

		final TestTask task2 = TestTaskFactory.newTaskWithIdentifier(new Identifier<String>("task2"));
		task2.setRequestExecutor(executor);

        final TestTask task1 = TestTaskFactory.newTaskWithDynamicDependency(new Identifier<String>("task1"), task2);
		task1.setRequestExecutor(executor);

		final List<Task<?>> expectedOrder = new ArrayList<Task<?>>();
		expectedOrder.add(task1);
		expectedOrder.add(task2);

		return expectedOrder;
	}

	public static List<Task<?>> newTaskListWithDynamicDependenciesSecondTaskFailsWithNetworkingException(final Exception exception) {
		final RequestExecutor.SerialRequestExecutor executor = new RequestExecutor.SerialRequestExecutor();

		final TestTask task2 = TestTaskFactory.newTaskThatThrowsNetworkingException(exception);
        task2.setRequestExecutor(executor);

        final TestTask task1 = TestTaskFactory.newTaskWithDynamicDependency(new Identifier<String>("task1"), task2);
		task1.setRequestExecutor(executor);

		final List<Task<?>> expectedOrder = new ArrayList<Task<?>>();
		expectedOrder.add(task1);
		expectedOrder.add(task2);

		return expectedOrder;
	}

	public static List<Task<?>> newTaskListWithDynamicDependenciesSecondTaskFailsWithProcessingException(final Exception exception) {
		final RequestExecutor.SerialRequestExecutor executor = new RequestExecutor.SerialRequestExecutor();

		final TestTask task2 = TestTaskFactory.newTaskThatThrowsProcessingException(exception);
        task2.setRequestExecutor(executor);

        final TestTask task1 = TestTaskFactory.newTaskWithDynamicDependency(new Identifier<String>("task1"), task2);
		task1.setRequestExecutor(executor);

		final List<Task<?>> expectedOrder = new ArrayList<Task<?>>();
		expectedOrder.add(task1);
		expectedOrder.add(task2);

		return expectedOrder;
	}

    public static List<Task<?>> newTaskList() {
        final RequestExecutor.SerialRequestExecutor executor = new RequestExecutor.SerialRequestExecutor();

        final TestTask task1 = TestTaskFactory.newTaskWithIdentifier(new Identifier<String>("task1"));
        task1.setRequestExecutor(executor);

        final TestTask task2 = TestTaskFactory.newTaskWithIdentifier(new Identifier<String>("task2"));
        task2.setRequestExecutor(executor);

        final List<Task<?>> expectedOrder = new ArrayList<Task<?>>();
        expectedOrder.add(task1);
        expectedOrder.add(task2);

        return expectedOrder;
    }
}
