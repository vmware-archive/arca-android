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
package io.pivotal.arca.provider.test;

import android.test.AndroidTestCase;

import junit.framework.Assert;

import io.pivotal.arca.provider.PackageUtils;

public class PackageUtilsTest extends AndroidTestCase {

	public void testVersionCode() {
		Assert.assertEquals(0, PackageUtils.getVersionCode(getContext()));
	}

	public void testVersionCodeNotFound() {
		assertEquals(-1, PackageUtils.getVersionCode(getContext(), "com.test.123456789"));
	}

	public void testPackageName() {
		final String expected = "io.pivotal.arca.provider.test";
		final String actual = PackageUtils.getPackageName(getContext());
		assertEquals(expected, actual);
	}

}
