package com.arca.provider.test;

import java.util.ArrayList;
import java.util.Collection;

import android.test.AndroidTestCase;

import com.arca.provider.CollectionUtils;

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
