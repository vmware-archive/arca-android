package com.arca.dispatcher.test;

import android.test.AndroidTestCase;

import com.arca.dispatcher.DeleteResult;
import com.arca.dispatcher.Error;
import com.arca.dispatcher.SupportDeleteLoader;

public class SupportDeleteLoaderTest extends AndroidTestCase {

	public void testErrorResult() {
		final Error error = new Error(100, "message");
		final SupportDeleteLoader loader = new SupportDeleteLoader(getContext(), null, null);
		final DeleteResult result = loader.getErrorResult(error);

		assertTrue(result.hasError());
		assertEquals(error, result.getError());
	}
	
}
