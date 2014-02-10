package com.xtreme.rest.dispatcher.test;

import android.test.AndroidTestCase;

import com.xtreme.rest.dispatcher.Error;
import com.xtreme.rest.dispatcher.InsertResult;
import com.xtreme.rest.dispatcher.ModernInsertLoader;

public class ModernInsertLoaderTest extends AndroidTestCase {

	public void testErrorResult() {
		final Error error = new Error(100, "message");
		final ModernInsertLoader loader = new ModernInsertLoader(getContext(), null, null);
		final InsertResult result = loader.getErrorResult(error);

		assertTrue(result.hasError());
		assertEquals(error, result.getError());
	}
	
}
