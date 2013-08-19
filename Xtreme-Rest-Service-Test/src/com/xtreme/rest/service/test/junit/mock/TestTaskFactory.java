package com.xtreme.rest.service.test.junit.mock;

import com.xtreme.threading.RequestIdentifier;

public class TestTaskFactory {

	public static TestTask newTask() {
		return new TestTask(null);
	}

	public static TestTask newTaskWithIdentifier(final RequestIdentifier<String> identifier) {
		return new TestTask(identifier);
	}

	public static TestTask newTaskWithNetworkResult(String networkResult) {
		return new TestTask(null, networkResult);
	}

	public static TestTask newTaskThatThrowsNetworkException(Exception exception) {
		return new TestTask(null, null, exception, null);
	}

	public static TestTask newTaskThatThrowsProcessingException(Exception exception) {
		return new TestTask(null, null, null, exception);
	}

}
