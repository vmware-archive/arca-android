/* 
 * Copyright (C) 2014 Pivotal Software, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
