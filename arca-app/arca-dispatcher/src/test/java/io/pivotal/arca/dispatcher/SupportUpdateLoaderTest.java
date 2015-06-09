/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.dispatcher;

import android.test.AndroidTestCase;

public class SupportUpdateLoaderTest extends AndroidTestCase {

	public void testErrorResult() {
		final Error error = new Error(100, "message");
		final SupportUpdateLoader loader = new SupportUpdateLoader(getContext(), null, null);
		final UpdateResult result = loader.getErrorResult(error);

		assertTrue(result.hasError());
		assertEquals(error, result.getError());
	}

}
