package com.xtreme.rest.dispatcher.test;

import android.test.AndroidTestCase;

import com.xtreme.rest.dispatcher.Error;
import com.xtreme.rest.dispatcher.UpdateResult;
import com.xtreme.rest.dispatcher.SupportUpdateLoader;

public class SupportUpdateLoaderTest extends AndroidTestCase {

	public void testErrorResult() {
		final Error error = new Error(100, "message");
		final SupportUpdateLoader loader = new SupportUpdateLoader(getContext(), null, null);
		final UpdateResult result = loader.getErrorResult(error);

		assertTrue(result.hasError());
		assertEquals(error, result.getError());
	}
	
}
