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
package io.pivotal.arca.dispatcher;

import android.test.AndroidTestCase;

import io.pivotal.arca.dispatcher.Error;
import io.pivotal.arca.dispatcher.Result;

public class ResultTest extends AndroidTestCase {

	public void testResultHasError() {
		final io.pivotal.arca.dispatcher.Error error = new Error(-1, "");
		final TestResult result = new TestResult(error);
		assertTrue(result.hasError());
	}

	public void testResultNoError() {
		final Object data = new Object();
		final TestResult result = new TestResult(data);
		assertFalse(result.hasError());
	}

	public void testResultIsSyncing() {
		final Object data = new Object();
		final TestResult result = new TestResult(data);
		result.setIsSyncing(true);
		assertTrue(result.isSyncing());
	}

	public void testResultIsValid() {
		final Object data = new Object();
		final TestResult result = new TestResult(data);
		result.setIsValid(true);
		assertTrue(result.isValid());
	}

	private static final class TestResult extends Result<Object> {

		public TestResult(final Object data) {
			super(data);
		}

		public TestResult(final Error error) {
			super(error);
		}

	}

}
