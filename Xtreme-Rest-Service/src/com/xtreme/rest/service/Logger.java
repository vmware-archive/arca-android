package com.xtreme.rest.service;

import java.util.Locale;

import android.os.Looper;
import android.util.Log;

public class Logger {

	private static final class LogLevel {
		private static final int ERROR = 0;
		private static final int WARNING = 1;
		private static final int INFO = 2;
		private static final int DEBUG = 3;
		private static final int VERBOSE = 4;
	}

	private static final int MAX_LOG_LEVEL = LogLevel.VERBOSE;

	private static final String UI_THREAD = "UI";
	private static final String BACKGROUND_THREAD = "BG";

	private static String sTagName = "Xtreme-Rest-Service";
	private static boolean sIsDebuggable = false;

	private static final class Holder { 
        public static final Logger INSTANCE = new Logger();
	}
	
	private static Logger getInstance() {
	    return Holder.INSTANCE;
	}

	private Logger() {}

	/**
	 * prints to info log stream
	 * 
	 * @param message
	 *            the message (including any possible format specifiers)
	 * @param objects
	 *            objects for objects (variable argument list - optional)
	 */
	public static void i(final String message, final Object... objects) {
		if (sIsDebuggable && MAX_LOG_LEVEL >= LogLevel.INFO) {
			final String formattedString = formatMessage(message, objects);
			Log.i(sTagName, formattedString);
		}
	}
	
	public static void info(final String message, final Object... objects) {
		i(message, objects);
	}

	/**
	 * prints to warning log stream
	 * 
	 * @param message
	 *            the message (including any possible format specifiers)
	 * @param objects
	 *            objects for objects (variable argument list - optional)
	 */
	public static void w(final String message, final Object... objects) {
		if (sIsDebuggable && MAX_LOG_LEVEL >= LogLevel.WARNING) {
			final String formattedString = formatMessage(message, objects);
			Log.w(sTagName, formattedString);
		}
	}
	
	public static void warning(final String message, final Object... objects) {
		w(message, objects);
	}

	/**
	 * prints to verbose log stream
	 * 
	 * @param message
	 *            the message (including any possible format specifiers)
	 * @param objects
	 *            objects for objects (variable argument list - optional)
	 */
	public static void v(final String message, final Object... objects) {
		if (sIsDebuggable && MAX_LOG_LEVEL >= LogLevel.VERBOSE) {
			final String formattedString = formatMessage(message, objects);
			Log.v(sTagName, formattedString);
		}
	}
	
	public static void verbose(final String message, final Object... objects) {
		v(message, objects);
	}

	/**
	 * prints to debug log stream
	 * 
	 * @param message
	 *            the message (including any possible format specifiers)
	 * @param objects
	 *            objects for objects (variable argument list - optional)
	 */
	public static void d(final String message, final Object... objects) {
		if (sIsDebuggable && MAX_LOG_LEVEL >= LogLevel.DEBUG) {
			final String formattedString = formatMessage(message, objects);
			Log.d(sTagName, formattedString);
		}
	}
	
	public static void debug(final String message, final Object... objects) {
		d(message, objects);
	}

	/**
	 * prints to error log stream
	 * 
	 * @param message
	 *            the message (including any possible format specifiers)
	 * @param objects
	 *            objects for objects (variable argument list - optional)
	 */
	public static void e(final String message, final Object... objects) {
		if (sIsDebuggable && MAX_LOG_LEVEL >= LogLevel.ERROR) {
			final String formattedString = formatMessage(message, objects);
			Log.e(sTagName, formattedString);
		}
	}
	
	public static void error(final String message, final Object... objects) {
		e(message, objects);
	}

	/**
	 * prints a message an exception and stack track to the error log stream
	 * 
	 * @param message
	 *            the error message to log
	 * @param tr
	 *            the exception to print
	 */
	public static void ex(final String message, final Throwable tr) {
		final String formattedString = formatMessage(message, new Object[] {}) + ": " + Log.getStackTraceString(tr);
		Log.w(sTagName, formattedString);
	}
	
	public static void exception(final String message, final Throwable tr) {
		ex(message, tr);
	}

	/**
	 * prints an exception and stack track to the error log stream
	 * 
	 * @param tr
	 *            the exception to print
	 */
	public static void ex(final Throwable tr) {
		ex("", tr);
	}
	
	public static void exception(final Throwable tr) {
		ex(tr);
	}

	/**
	 * Sets the debuggable flag in the logger.</br> 
	 * If set to 'true' then all messages will be logged.</br> 
	 * If set to 'false' then only error messages will be logged.
	 * 
	 * @param isDebuggable
	 * @param tagName
	 */
	public static void setup(final boolean isDebuggable, final String tagName) {
		sIsDebuggable = isDebuggable;
		sTagName = tagName;
	}

	private static String formatMessage(String message, final Object... objects) {
		
		if (objects.length > 0) {
			message = String.format(Locale.getDefault(), message, objects);
		}
		
		final StackTraceElement element = getCallingStackTraceElement();
		final String threadInfo = getThreadInfo();
		final String className = element.getClassName();
		final String methodName = element.getMethodName();
		final int lineNumber = element.getLineNumber();
		final long threadId = Thread.currentThread().getId();
		
		return String.format(Locale.getDefault(), "%s [%s:%s:%d:tid%d] %s", threadInfo, className, methodName, lineNumber, threadId, message);
	}
	

	private static StackTraceElement getCallingStackTraceElement() {
		
		final StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		final String loggerClassName = getInstance().getClass().getName();

		boolean foundLogger = false;
		for (int i = 0; i < elements.length; i++) {
			final StackTraceElement element = elements[i];

			// Scan down the list until we find the Logger itself
			if (!foundLogger) {
				if (element.getClassName().equalsIgnoreCase(loggerClassName)) {
					foundLogger = true;
				}
				continue;
			}

			// After finding the Logger, look for the first class that isn't the
			// logger -- that's the class that called the Logger!
			if (foundLogger) {
				if (!element.getClassName().equalsIgnoreCase(loggerClassName)) {
					return element;
				}
			}
		}
		return null;
	}

	private static String getThreadInfo() {
		if (isUiThread()) {
			return "*" + UI_THREAD + "*";
		} else {
			return "*" + BACKGROUND_THREAD + "*";
		}
	}

	private static boolean isUiThread() {
		return Looper.myLooper() == Looper.getMainLooper();
	}
}