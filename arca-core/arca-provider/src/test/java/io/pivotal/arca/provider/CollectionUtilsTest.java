/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.provider;

import android.test.AndroidTestCase;

import java.util.ArrayList;
import java.util.Collection;

public class CollectionUtilsTest extends AndroidTestCase {

	public void testFilterEmptyCollection() {
		final Collection<Object> objects = new ArrayList<Object>();
		final Collection<String> filtered = CollectionUtils.filter(objects, String.class);
		assertEquals(0, filtered.size());
	}

	public void testFilterIncorrectTypeCollection() {
		final Collection<Object> objects = new ArrayList<Object>();
		objects.add(new Object());
		final Collection<String> filtered = CollectionUtils.filter(objects, String.class);
		assertEquals(0, filtered.size());
	}

	public void testFilterCorrectTypeCollection() {
		final Collection<Object> objects = new ArrayList<Object>();
		objects.add(new String());
		final Collection<String> filtered = CollectionUtils.filter(objects, String.class);
		assertEquals(1, filtered.size());
	}

	public void testFilterMixedTypesCollection() {
		final Collection<Object> objects = new ArrayList<Object>();
		objects.add(new String());
		objects.add(new Object());
		final Collection<String> filtered = CollectionUtils.filter(objects, String.class);
		assertEquals(1, filtered.size());
	}

}
