package com.arca.dispatcher.test;

import android.test.AndroidTestCase;

import com.arca.dispatcher.Error;
import com.arca.dispatcher.SupportUpdateLoader;
import com.arca.dispatcher.UpdateResult;

public class SupportUpdateLoaderTest extends AndroidTestCase {

	public void testErrorResult() {
		final Error error = new Error(100, "message");
		final SupportUpdateLoader loader = new SupportUpdateLoader(getContext(), null, null);
		final UpdateResult result = loader.getErrorResult(error);

		assertTrue(result.hasError());
		assertEquals(error, result.getError());
	}
	
}
