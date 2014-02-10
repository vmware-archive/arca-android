package com.xtreme.rest.dispatcher.test;

import android.test.AndroidTestCase;

import com.xtreme.rest.dispatcher.DeleteResult;
import com.xtreme.rest.dispatcher.Error;
import com.xtreme.rest.dispatcher.SupportDeleteLoader;

public class SupportDeleteLoaderTest extends AndroidTestCase {

	public void testErrorResult() {
		final Error error = new Error(100, "message");
		final SupportDeleteLoader loader = new SupportDeleteLoader(getContext(), null, null);
		final DeleteResult result = loader.getErrorResult(error);

		assertTrue(result.hasError());
		assertEquals(error, result.getError());
	}
	
}
