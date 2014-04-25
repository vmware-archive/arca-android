package com.xtreme.rest.utils;

import android.annotation.SuppressLint;
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

	private static String sTagName = "Xtreme-Rest";
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
	 * sets the debuggable flag in the logger. if set to 'true' then all messages will be logged. if set to 'false' then only error messages will be logged.
	 * 
	 * @param isDebuggable
	 */
	public static void setup(final boolean isDebuggable, final String tagName) {
		Logger.sIsDebuggable = isDebuggable;
		Logger.sTagName = tagName;
	}

	/**
	 * formats and returns a message for the logger
	 * 
	 * @param message
	 *            the message formatting string, including (optional) format specifiers
	 * @param objects
	 *            objects to be formatted in the formatted string (optional)
	 * @return the formatted message
	 */
	@SuppressLint("DefaultLocale")
	private static String formatMessage(final String message, final Object... objects) {

		final StackTraceElement s = getCallingStackTraceElement();
		String formattedMessage = String.format("[%s:%s:%d:tid%d] ", s.getClassName(), s.getMethodName(), s.getLineNumber(), Thread.currentThread().getId());

		if (objects.length > 0)
			formattedMessage += String.format(message, objects);
		else
			formattedMessage += message;

		return addThreadInfo(formattedMessage);
	}

	/**
	 * @return the StackTraceElement for the method that called into the logger
	 */
	private static StackTraceElement getCallingStackTraceElement() {
		final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		final String loggerClassName = getInstance().getClass().getName();

		boolean foundLogger = false;
		for (int i = 0; i < stackTraceElements.length; i += 1) {
			final StackTraceElement s = stackTraceElements[i];

			// Scan down the list until we find the Logger itself
			if (!foundLogger) {
				if (s.getClassName().equalsIgnoreCase(loggerClassName)) {
					foundLogger = true;
				}
				continue;
			}

			// After finding the Logger, look for the first class that isn't the
			// logger -- that's the class that called the Logger!
			if (foundLogger) {
				if (!s.getClassName().equalsIgnoreCase(loggerClassName)) {
					return s;
				}
			}
		}

		// Should never happen?
		return null;
	}

	private static String addThreadInfo(final String string) {
		if (isUiThread())
			return "*" + UI_THREAD + "* " + string;
		return "*" + BACKGROUND_THREAD + "* " + string;
	}

	private static boolean isUiThread() {
		return Looper.myLooper() == Looper.getMainLooper();
	}
}