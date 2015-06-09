/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.provider;

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
