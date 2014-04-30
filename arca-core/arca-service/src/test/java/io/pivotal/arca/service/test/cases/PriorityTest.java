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
package io.pivotal.arca.service.test.cases;

import android.test.AndroidTestCase;

import io.pivotal.arca.service.Priority;
import io.pivotal.arca.threading.PriorityAccessor;

import junit.framework.Assert;

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
		Assert.assertEquals(Priority.LIVE, Priority.valueOf("LIVE"));
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
