package com.xtreme.rest.service.test.junit.mock;

import com.xtreme.rest.service.ServiceError;
import com.xtreme.rest.service.Task;
import com.xtreme.rest.service.TaskObserver;

public class TestTaskObserver implements TaskObserver {

	@Override
	public void onTaskStarted(final Task<?> task) {

	}

	@Override
	public void onTaskComplete(final Task<?> task) {

	}

	@Override
	public void onTaskFailure(final Task<?> task, final ServiceError error) {

	}

}
