package com.xtreme.rest.sync.test.cases;

import android.test.AndroidTestCase;

import com.xtreme.rest.sync.Logger;


public class LoggerTest extends AndroidTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testLoggerValueOfInstance() {
		assertEquals(Logger.INSTANCE, Logger.valueOf("INSTANCE"));
	}
	
	public void testLoggerValues() {
		final Logger[] loggers = Logger.values();
		assertEquals(Logger.INSTANCE, loggers[0]);
	}
	
	public void testLoggerLogLevel() {
		assertNotNull(new Logger.Level());
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
