package com.xtreme.rest.loader;

/**
 * An interface that allows you to listen on content being managed by a {@link ContentLoader}.
 */
public interface ContentLoaderListener extends ContentErrorListener {

	/**
	 * @param response The {@link ContentResponse} that has finished loading.
	 */
	public void onLoaderFinished(ContentResponse response);

	/**
	 * Callback for when the loader is reset.
	 */
	public void onLoaderReset();
}
