package com.xtreme.rest.loader;

/**
 * An interface that allows you to listen on errors occurring from 
 * within a {@link ContentLoader}.
 */
public interface ContentErrorListener {
	/**
	 *  Called if an error occurred during the loading process.
	 * @param error The {@link ContentError} that occurred.
	 */
	public void onError(ContentError error);
}
