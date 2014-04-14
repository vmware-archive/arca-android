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
package com.arca.utils.test;

import android.test.AndroidTestCase;

import com.arca.utils.Logger;

public class LoggerTest extends AndroidTestCase {

	public void testLoggerValueOfInstance() {
		assertEquals(Logger.INSTANCE, Logger.valueOf("INSTANCE"));
	}

	public void testLoggerValues() {
		final Logger[] loggers = Logger.values();
		assertEquals(Logger.INSTANCE, loggers[0]);
	}

	public void testLoggerLogLevel() {
		assertEquals(0, Logger.Level.ERROR);
		assertEquals(1, Logger.Level.WARNING);
		assertEquals(2, Logger.Level.INFO);
		assertEquals(3, Logger.Level.DEBUG);
		assertEquals(4, Logger.Level.VERBOSE);
	}

	public void testLoggerError() {
		Logger.setup(true, "ERROR_TEST");
		Logger.error("ERROR");
		Logger.error("ERROR: %s", new Object());
	}

	public void testLoggerWarning() {
		Logger.setup(true, "WARNING_TEST");
		Logger.warning("WARNING");
		Logger.warning("WARNING: %s", new Object());
	}

	public void testLoggerInfo() {
		Logger.setup(true, "INFO_TEST");
		Logger.info("INFO");
		Logger.info("INFO: %s", new Object());
	}

	public void testLoggerDebug() {
		Logger.setup(true, "DEBUG_TEST");
		Logger.debug("DEBUG");
		Logger.debug("DEBUG: %s", new Object());
	}

	public void testLoggerVerbose() {
		Logger.setup(true, "VERBOSE_TEST");
		Logger.verbose("VERBOSE");
		Logger.verbose("VERBOSE: %s", new Object());
	}

	public void testLoggerException() {
		Logger.setup(true, "EXCEPTION_TEST");
		Logger.exception(new Exception("EXCEPTION"));
		Logger.exception("EXCEPTION", new Exception());
	}
}
