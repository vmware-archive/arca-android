package com.xtreme.rest.provider.test;

import android.test.AndroidTestCase;

import com.xtreme.rest.provider.PackageUtils;

public class PackageUtilsTest extends AndroidTestCase {

	public void testVersionCode() {
		assertEquals(1, PackageUtils.getVersionCode(getContext()));
	}
	
	public void testVersionCodeNotFound() {
		assertEquals(-1, PackageUtils.getVersionCode(getContext(), "com.test"));
	}
	
	public void testPackageName() {
		final String expected = "com.xtreme.rest.provider.test";
		final String actual = PackageUtils.getPackageName(getContext());
		assertEquals(expected, actual);
	}
	
}
