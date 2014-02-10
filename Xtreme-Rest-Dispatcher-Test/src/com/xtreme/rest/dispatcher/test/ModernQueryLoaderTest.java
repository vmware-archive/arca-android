package com.xtreme.rest.dispatcher.test;

import com.xtreme.rest.dispatcher.ModernQueryLoader;
import com.xtreme.rest.dispatcher.QueryResult;
import com.xtreme.rest.dispatcher.Error;

import android.test.AndroidTestCase;

public class ModernQueryLoaderTest extends AndroidTestCase {

	public void testErrorResult() {
		final Error error = new Error(100, "message");
		final ModernQueryLoader loader = new ModernQueryLoader(getContext(), null, null);
		final QueryResult result = loader.getErrorResult(error);

		assertTrue(result.hasError());
		assertEquals(error, result.getError());
	}
	
}
