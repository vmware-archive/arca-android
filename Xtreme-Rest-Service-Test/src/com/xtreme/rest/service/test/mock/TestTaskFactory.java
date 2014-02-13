package com.xtreme.rest.service.test.mock;

import java.util.ArrayList;
import java.util.List;

import com.xtreme.rest.service.RequestExecutor.SerialRequestExecutor;
import com.xtreme.rest.service.Task;
import com.xtreme.threading.RequestIdentifier;

public class TestTaskFactory {

	public static TestTask newTask() {
		return new TestTask(new RequestIdentifier<String>("task"));
	}

	public static TestTask newTaskWithIdentifier(final RequestIdentifier<String> identifier) {
		return new TestTask(identifier);
	}

	public static TestTask newTaskWithNetworkResult(final String networkResult) {
		return new TestTask(new RequestIdentifier<String>("task"), networkResult);
	}

	public static TestTask newTaskThatThrowsNetworkException(final Exception exception) {
		return new TestTask(new RequestIdentifier<String>("task"), null, exception, null);
	}

	public static TestTask newTaskThatThrowsProcessingException(final Exception exception) {
		return new TestTask(new RequestIdentifier<String>("task"), null, null, exception);
	}
	
	public static List<Task<?>> newTaskListWithPrerequisites() {
		final SerialRequestExecutor executor = new SerialRequestExecutor();
		
		final TestTask task1 = TestTaskFactory.newTaskWithIdentifier(new RequestIdentifier<String>("task1"));
		task1.setRequestExecutor(executor);
		
		final TestTask task2 = TestTaskFactory.newTaskWithIdentifier(new RequestIdentifier<String>("task2"));
		task2.setRequestExecutor(executor);
		task2.addPrerequisite(task1);

		final List<Task<?>> expectedOrder = new ArrayList<Task<?>>();
		expectedOrder.add(task1);
		expectedOrder.add(task2);
		
		return expectedOrder;
	}

	public static List<Task<?>> newTaskListWithPrerequisitesFirstTaskFailsWithNetworkException(final Exception exception) {
		final SerialRequestExecutor executor = new SerialRequestExecutor();

		final TestTask task1 = TestTaskFactory.newTaskThatThrowsNetworkException(exception);
		task1.setRequestExecutor(executor);
		
		final TestTask task2 = TestTaskFactory.newTask();
		task2.setRequestExecutor(executor);
		task2.addPrerequisite(task1);

		final List<Task<?>> expectedOrder = new ArrayList<Task<?>>();
		expectedOrder.add(task1);
		expectedOrder.add(task2);
		
		return expectedOrder;
	}
	
	public static List<Task<?>> newTaskListWithPrerequisitesSecondTaskFailsWithNetworkException(final Exception exception) {
		final SerialRequestExecutor executor = new SerialRequestExecutor();

		final TestTask task1 = TestTaskFactory.newTaskWithIdentifier(new RequestIdentifier<String>("task1"));
		task1.setRequestExecutor(executor);
		
		final TestTask task2 = TestTaskFactory.newTaskThatThrowsNetworkException(exception);
		task2.setRequestExecutor(executor);
		task2.addPrerequisite(task1);

		final List<Task<?>> expectedOrder = new ArrayList<Task<?>>();
		expectedOrder.add(task1);
		expectedOrder.add(task2);
		
		return expectedOrder;
	}

	public static List<Task<?>> newTaskListWithPrerequisitesFirstTaskFailsWithProcessingException(final Exception exception) {
		final SerialRequestExecutor executor = new SerialRequestExecutor();
		
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
		final SerialRequestExecutor executor = new SerialRequestExecutor();
		
		final TestTask task1 = TestTaskFactory.newTaskWithIdentifier(new RequestIdentifier<String>("task1"));
		task1.setRequestExecutor(executor);
		
		final TestTask task2 = TestTaskFactory.newTaskThatThrowsProcessingException(exception);
		task2.setRequestExecutor(executor);
		task2.addPrerequisite(task1);

		final List<Task<?>> expectedOrder = new ArrayList<Task<?>>();
		expectedOrder.add(task1);
		expectedOrder.add(task2);
		
		return expectedOrder;
	}
	
	public static List<Task<?>> newTaskListWithDependants() {
		final SerialRequestExecutor executor = new SerialRequestExecutor();

		final TestTask task2 = TestTaskFactory.newTaskWithIdentifier(new RequestIdentifier<String>("task2"));
		task2.setRequestExecutor(executor);
		
		final TestTask task1 = TestTaskFactory.newTaskWithIdentifier(new RequestIdentifier<String>("task1"));
		task1.setRequestExecutor(executor);
		task1.addDependant(task2);
		
		final List<Task<?>> expectedOrder = new ArrayList<Task<?>>();
		expectedOrder.add(task1);
		expectedOrder.add(task2);
		
		return expectedOrder;
	}
	
	public static List<Task<?>> newTaskListWithDependantsFirstTaskFailsWithNetworkException(final Exception exception) {
		final SerialRequestExecutor executor = new SerialRequestExecutor();

		final TestTask task2 = TestTaskFactory.newTaskWithIdentifier(new RequestIdentifier<String>("task2"));
		task2.setRequestExecutor(executor);
		
		final TestTask task1 = TestTaskFactory.newTaskThatThrowsNetworkException(exception);
		task1.setRequestExecutor(executor);
		task1.addDependant(task2);
		
		final List<Task<?>> expectedOrder = new ArrayList<Task<?>>();
		expectedOrder.add(task1);
		expectedOrder.add(task2);
		
		return expectedOrder;
	}
	
	public static List<Task<?>> newTaskListWithDependantsSecondTaskFailsWithNetworkException(final Exception exception) {
		final SerialRequestExecutor executor = new SerialRequestExecutor();

		final TestTask task2 = TestTaskFactory.newTaskThatThrowsNetworkException(exception);
		task2.setRequestExecutor(executor);
		
		final TestTask task1 = TestTaskFactory.newTaskWithIdentifier(new RequestIdentifier<String>("task1"));
		task1.setRequestExecutor(executor);
		task1.addDependant(task2);
		
		final List<Task<?>> expectedOrder = new ArrayList<Task<?>>();
		expectedOrder.add(task1);
		expectedOrder.add(task2);
		
		return expectedOrder;
	}
	
	public static List<Task<?>> newTaskListWithDependantsFirstTaskFailsWithProcessingException(final Exception exception) {
		final SerialRequestExecutor executor = new SerialRequestExecutor();

		final TestTask task2 = TestTaskFactory.newTaskWithIdentifier(new RequestIdentifier<String>("task2"));
		task2.setRequestExecutor(executor);
		
		final TestTask task1 = TestTaskFactory.newTaskThatThrowsProcessingException(exception);
		task1.setRequestExecutor(executor);
		task1.addDependant(task2);
		
		final List<Task<?>> expectedOrder = new ArrayList<Task<?>>();
		expectedOrder.add(task1);
		expectedOrder.add(task2);
		
		return expectedOrder;
	}
	
	public static List<Task<?>> newTaskListWithDependantsSecondTaskFailsWithProcessingException(final Exception exception) {
		final SerialRequestExecutor executor = new SerialRequestExecutor();

		final TestTask task2 = TestTaskFactory.newTaskThatThrowsProcessingException(exception);
		task2.setRequestExecutor(executor);
		
		final TestTask task1 = TestTaskFactory.newTaskWithIdentifier(new RequestIdentifier<String>("task1"));
		task1.setRequestExecutor(executor);
		task1.addDependant(task2);
		
		final List<Task<?>> expectedOrder = new ArrayList<Task<?>>();
		expectedOrder.add(task1);
		expectedOrder.add(task2);
		
		return expectedOrder;
	}

}
