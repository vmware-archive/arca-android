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
package com.arca.threading.test;

import android.test.AndroidTestCase;

import com.arca.threading.Prioritizable;
import com.arca.threading.PrioritizableRequest;
import com.arca.threading.QueuingMaps;
import com.arca.threading.Identifier;

public class QueuingMapsTests extends AndroidTestCase {
	private QueuingMaps mMaps;

	private PrioritizableRequest mTestPrioritizable1;
	private PrioritizableRequest mTestPrioritizable2;
	private PrioritizableRequest mTestPrioritizable3;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		mMaps = new QueuingMaps();
		mTestPrioritizable1 = generatePrioritizable("request1");
		mTestPrioritizable2 = generatePrioritizable("request1");
		mTestPrioritizable3 = generatePrioritizable("request2");
	}

	public void testPrioritizableEquality() {
		final Identifier<?> r1 = new Identifier<String>("hello");
		final Identifier<?> r2 = new Identifier<String>("hello");

		assertFalse(r1 == r2);
		assertTrue(r1.equals(r2));
		assertTrue(r1.hashCode() == r2.hashCode());
	}

	public void testingNotifySingleRequest() {
		mMaps.put(mTestPrioritizable1);
		mMaps.put(mTestPrioritizable2);
		mMaps.put(mTestPrioritizable3);
		mMaps.notifyExecuting(mTestPrioritizable3);

		assertFalse(mTestPrioritizable1.isCancelled());
		assertFalse(mTestPrioritizable2.isCancelled());
		assertFalse(mTestPrioritizable3.isCancelled());
	}

	public void testingNotifyMultipleRequests() {
		mMaps.put(mTestPrioritizable1);
		mMaps.put(mTestPrioritizable2);
		mMaps.put(mTestPrioritizable3);
		mMaps.notifyExecuting(mTestPrioritizable1);

		assertFalse(mTestPrioritizable1.isCancelled());
		assertTrue(mTestPrioritizable2.isCancelled());
		assertFalse(mTestPrioritizable3.isCancelled());
	}

	public void testingExecutionOfPendingRequest() {
		mMaps.put(mTestPrioritizable1);
		mMaps.notifyExecuting(mTestPrioritizable1);
		mMaps.put(mTestPrioritizable2);

		assertFalse(mTestPrioritizable1.isCancelled());
		assertTrue(mTestPrioritizable2.isCancelled());
	}

	public void testingExecutionOfPostCompletionRequest() {
		mMaps.put(mTestPrioritizable1);
		mMaps.notifyExecuting(mTestPrioritizable1);
		mMaps.onComplete(new Identifier<String>("request1"));
		mMaps.put(mTestPrioritizable2);

		assertFalse(mTestPrioritizable2.isCancelled());
	}

	public void testingRemovalOfPrioritizables() {
		mMaps.put(mTestPrioritizable1);
		mMaps.cancel(mTestPrioritizable1);

		assertTrue(mTestPrioritizable1.isCancelled());
	}

	private static PrioritizableRequest generatePrioritizable(final String request) {
		return new PrioritizableRequest(new Prioritizable() {
			@Override
			public void execute() {
			}

			@Override
			public Identifier<?> getIdentifier() {
				return new Identifier<String>(request);
			}
		}, 0);
	}
}
