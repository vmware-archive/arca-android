package com.xtreme.rest.loader;

import com.xtreme.rest.broadcasts.RestError;

/**
 * An interface that allows you to listen on errors occurring from within a {@link ContentLoader}.
 */
public interface ContentErrorListener {
	/**
	 *  Called if an error occurred during the loading process.
	 * @param error The {@link RestError} that occurred.
	 */
	public void onError(RestError error);
}
