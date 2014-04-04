package com.arca.service.test.cases;

import android.test.AndroidTestCase;

import com.arca.service.Priority;
import com.arca.threading.PriorityAccessor;

public class PriorityTest extends AndroidTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testPriorityValueOfLive() {
		assertEquals(Priority.LIVE, Priority.valueOf("LIVE"));
	}
	
	public void testPriorityValueOfHigh() {
		assertEquals(Priority.HIGH, Priority.valueOf("HIGH"));
	}
	
	public void testPriorityValueOfMedium() {
		assertEquals(Priority.MEDIUM, Priority.valueOf("MEDIUM"));
	}
	
	public void testPriorityValueOfLow() {
		assertEquals(Priority.LOW, Priority.valueOf("LOW"));
	}
	
	public void testPriorityValues() {
		final Priority[] priorities = Priority.values();
		assertEquals(Priority.LIVE, priorities[0]);
		assertEquals(Priority.HIGH, priorities[1]);
		assertEquals(Priority.MEDIUM, priorities[2]);
		assertEquals(Priority.LOW, priorities[3]);
	}
	
	public void testPriorityNewAccessorArray() {
		final PriorityAccessor[] array = Priority.newAccessorArray();
		assertNotNull(array);
	}
	
}
