/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.utils;

import android.test.AndroidTestCase;

import io.pivotal.arca.utils.Logger;

import junit.framework.Assert;

public class LoggerTest extends AndroidTestCase {

	public void testLoggerValueOfInstance() {
		Assert.assertEquals(Logger.INSTANCE, Logger.valueOf("INSTANCE"));
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
