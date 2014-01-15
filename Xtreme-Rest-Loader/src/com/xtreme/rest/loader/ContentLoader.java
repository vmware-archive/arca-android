package com.xtreme.rest.loader;

import android.app.LoaderManager.LoaderCallbacks;

/**
 * A wrapper around Android's {@link LoaderCallbacks} that allows for {@link ContentRequest}s to be executed. If any {@link ContentRequest}
 * is executed, then {@link #destroy()} has to be called when appropriate.
 */
public interface ContentLoader {
	/**
	 * Executes a {@link ContentRequest} to fetch the appropriate data. This method initializes the {@link ContentLoader} if necessary.
	 * @param request The request to be executed (must be non-null).
	 */
	public void execute(ContentRequest request);

	/**
	 * Cancel the currently loading {@link ContentRequest} if any.
	 */
	public void cancel();

	/**
	 * Destroys the {@link ContentLoader}. This method must be called when a response is no longer needed.
	 */
	public void destroy();
}
