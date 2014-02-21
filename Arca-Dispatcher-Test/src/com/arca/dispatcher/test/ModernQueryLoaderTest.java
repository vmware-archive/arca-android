package com.arca.dispatcher.test;

import android.test.AndroidTestCase;

import com.arca.dispatcher.Error;
import com.arca.dispatcher.ModernQueryLoader;
import com.arca.dispatcher.QueryResult;

public class ModernQueryLoaderTest extends AndroidTestCase {

	public void testErrorResult() {
		final Error error = new Error(100, "message");
		final ModernQueryLoader loader = new ModernQueryLoader(getContext(), null, null);
		final QueryResult result = loader.getErrorResult(error);

		assertTrue(result.hasError());
		assertEquals(error, result.getError());
	}
	
}
