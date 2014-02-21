package com.arca.dispatcher.test;

import android.test.AndroidTestCase;

import com.arca.dispatcher.Error;
import com.arca.dispatcher.InsertResult;
import com.arca.dispatcher.SupportInsertLoader;

public class SupportInsertLoaderTest extends AndroidTestCase {

	public void testErrorResult() {
		final Error error = new Error(100, "message");
		final SupportInsertLoader loader = new SupportInsertLoader(getContext(), null, null);
		final InsertResult result = loader.getErrorResult(error);

		assertTrue(result.hasError());
		assertEquals(error, result.getError());
	}
	
}
