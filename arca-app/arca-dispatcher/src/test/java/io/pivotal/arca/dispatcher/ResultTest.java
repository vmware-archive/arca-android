/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
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
