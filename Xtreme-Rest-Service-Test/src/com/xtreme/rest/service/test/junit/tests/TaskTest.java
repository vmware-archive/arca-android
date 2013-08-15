package com.xtreme.rest.service.test.junit.tests;

import android.test.AndroidTestCase;

import com.xtreme.rest.service.test.junit.mock.TestPrioritizableHandler;
import com.xtreme.rest.service.test.junit.mock.TestTask;
import com.xtreme.rest.service.test.junit.mock.TestTaskObserver;

public class TaskTest extends AndroidTestCase {

	private TestTask mTask;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		mTask = new TestTask();
		mTask.setPrioritizableHandler(new TestPrioritizableHandler());
		mTask.setTaskObserver(new TestTaskObserver());
		
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testInitialization() {
		// success
	}
}
