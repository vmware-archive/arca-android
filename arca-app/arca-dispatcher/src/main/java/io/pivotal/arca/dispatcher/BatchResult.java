package io.pivotal.arca.dispatcher;

import android.content.ContentProviderResult;

public class BatchResult extends Result<ContentProviderResult[]> {

	public BatchResult(final ContentProviderResult[] results) {
		super(results);
	}

	public BatchResult(final Error error) {
		super(error);
	}

}
