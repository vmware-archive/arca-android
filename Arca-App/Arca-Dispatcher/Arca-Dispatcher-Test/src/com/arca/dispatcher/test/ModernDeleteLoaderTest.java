package com.arca.dispatcher.test;

import android.test.AndroidTestCase;

import com.arca.dispatcher.DeleteResult;
import com.arca.dispatcher.Error;
import com.arca.dispatcher.ModernDeleteLoader;

public class ModernDeleteLoaderTest extends AndroidTestCase {

	public void testErrorResult() {
		final Error error = new Error(100, "message");
		final ModernDeleteLoader loader = new ModernDeleteLoader(getContext(), null, null);
		final DeleteResult result = loader.getErrorResult(error);

		assertTrue(result.hasError());
		assertEquals(error, result.getError());
	}
	
}
