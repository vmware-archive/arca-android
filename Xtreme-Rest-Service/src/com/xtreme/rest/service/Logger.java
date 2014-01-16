package com.xtreme.rest.service;

import java.util.Locale;

import android.os.Looper;
import android.util.Log;

public enum Logger {
	INSTANCE;

	public static final class Level {
		public static final int ERROR = 0;
		public static final int WARNING = 1;
		public static final int INFO = 2;
		public static final int DEBUG = 3;
		public static final int VERBOSE = 4;
	}

	private static final int MAX_LOG_LEVEL = Level.VERBOSE;

	private static final String UI_THREAD = "UI";
	private static final String BG_THREAD = "BG";

	private static boolean sIsDebuggable = false;
	private static String sTagName = "Logger";

	private Logger() {}
	
	/**
	 * Sets the debuggable flag in the logger and assigns a tag to the log message.</br>
	 * </br> 
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

	/**
	 * prints to info log stream
	 * 
	 * @param message
	 *            the message (including any possible format specifiers)
	 * @param objects
	 *            objects for objects (variable argument list - optional)
	 */
	public static void i(final String message, final Object... objects) {
		if (sIsDebuggable && MAX_LOG_LEVEL >= Level.INFO) {
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
		if (sIsDebuggable && MAX_LOG_LEVEL >= Level.WARNING) {
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
		if (sIsDebuggable && MAX_LOG_LEVEL >= Level.VERBOSE) {
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
		if (sIsDebuggable && MAX_LOG_LEVEL >= Level.DEBUG) {
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
		if (sIsDebuggable && MAX_LOG_LEVEL >= Level.ERROR) {
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
		final String stackTrace = getMessageFromThrowable(tr);
		final String fromattedMessage = formatMessage(message, new Object[] {});
		final String formattedString = String.format("%s : %s", fromattedMessage, stackTrace);
		Log.w(sTagName, formattedString);
	}

	private static String getMessageFromThrowable(final Throwable tr) {
		final String stackTrace = Log.getStackTraceString(tr);
		if (stackTrace == null || stackTrace.length() == 0) {
			return tr.getLocalizedMessage();
		} else {
			return stackTrace;
		}
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

	
	// ========================================
	
	
	private static boolean isUiThread() {
		return Looper.myLooper() == Looper.getMainLooper();
	}

	private static String formatMessage(final String message, final Object... objects) {
		if (objects.length > 0) {
			return formatMessage(String.format(Locale.getDefault(), message, objects));
		} else {
			return formatMessage(message);
		}
	}

	private static String formatMessage(final String message) {
		final String threadInfo = formatThreadInfo();
		final String stackTraceInfo = formatStackTraceInfo();
		
		return String.format(Locale.getDefault(), "*%s* [%s] %s", threadInfo, stackTraceInfo, message);
	}
	
	private static String formatThreadInfo() {
		final String threadName = isUiThread() ? UI_THREAD : BG_THREAD;
		final long threadId = Thread.currentThread().getId();
		
		return String.format(Locale.getDefault(), "%s:%d", threadName, threadId);
	}

	private static String formatStackTraceInfo() {
		final StackTraceElement element = getCallingStackTraceElement();
		final String className = element.getClassName();
		final String methodName = element.getMethodName();
		final int lineNumber = element.getLineNumber();
		
		return String.format(Locale.getDefault(), "%s:%s:%d", className, methodName, lineNumber);
	}

	private static StackTraceElement getCallingStackTraceElement() {
		
		final StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		final String loggerClassName = INSTANCE.getClass().getName();

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
}