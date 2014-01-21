package com.xtreme.rest.dispatcher;

import android.os.Bundle;

abstract class AbstractRequestLoaderDispatcher extends AbstractRequestDispatcher {

	protected static final class Extras {
		public static final String REQUEST = "request";
	}
	
	public AbstractRequestLoaderDispatcher(final RequestExecutor executor) {
		super(executor);
	}
	
	protected static Bundle createRequestBundle(final ContentRequest<?> request) {
		final Bundle bundle = new Bundle();
		bundle.putParcelable(Extras.REQUEST, request);
		return bundle;
	}
}
