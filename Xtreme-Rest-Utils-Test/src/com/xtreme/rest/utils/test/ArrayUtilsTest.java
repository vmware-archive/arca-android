package com.xtreme.rest.utils.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.test.AndroidTestCase;

import com.xtreme.rest.utils.ArrayUtils;

public class ArrayUtilsTest extends AndroidTestCase {

	public void testAppendNullArrays() {
		final Object[] result = ArrayUtils.append(null, null);
		assertEquals(null, result);
	}
	
	public void testAppendNullArrayToFirst() {
		final Object[] first = new Object[0];
		final Object[] result = ArrayUtils.append(first, null);
		assertTrue(Arrays.deepEquals(first, result));
	}
	
	public void testAppendNullArrayToSecond() {
		final Object[] second = new Object[0];
		final Object[] result = ArrayUtils.append(null, second);
		assertTrue(Arrays.deepEquals(second, result));
	}
	
	public void testAppendTwoEmptyArrays() {
		final Object[] first = new Object[0];
		final Object[] second = new Object[0];
		final Object[] result = ArrayUtils.append(first, second);
		assertEquals(0, result.length);
	}
	
	public void testAppendTwoNonEmptyArrays() {
		final Object[] first = new Object[] { "first" };
		final Object[] second = new Object[] { "second" };
		final Object[] result = ArrayUtils.append(first, second);
		assertEquals("first", result[0]);
		assertEquals("second", result[1]);
	}
	
	public void testArrayIsEmptySucceedsIfNull() {
		final Object[] array = null;
		assertTrue(ArrayUtils.isEmpty(array));
	}
	
	public void testArrayIsEmptySucceedsIfEmpty() {
		final Object[] array = new Object[0];
		assertTrue(ArrayUtils.isEmpty(array));
	}
	
	public void testArrayIsEmptyFailsIfNotEmpty() {
		final Object[] array = new Object[] { new Object() };
		assertFalse(ArrayUtils.isEmpty(array));
	}
	
	public void testArrayIsEmptyFailsIfContainsNullObject() {
		final Object[] array = new Object[] { null };
		assertFalse(ArrayUtils.isEmpty(array));
	}
	
	
	public void testArrayIsNotEmptyFailsIfNull() {
		final Object[] array = null;
		assertFalse(ArrayUtils.isNotEmpty(array));
	}
	
	public void testArrayIsNotEmptyFailsIfEmpty() {
		final Object[] array = new Object[0];
		assertFalse(ArrayUtils.isNotEmpty(array));
	}
	
	public void testArrayIsNotEmptySucceedsIfContainsSingleItem() {
		final Object[] array = new Object[] { new Object() };
		assertTrue(ArrayUtils.isNotEmpty(array));
	}
	
	public void testArrayIsNotEmptySucceedsIfContainsMultipleItems() {
		final Object[] array = new Object[] { new Object(), new String() };
		assertTrue(ArrayUtils.isNotEmpty(array));
	}
	
	public void testArrayIsNotEmptySucceedsIfContainsNullObject() {
		final Object[] array = new Object[] { null };
		assertTrue(ArrayUtils.isNotEmpty(array));
	}

	
	public void testListIsEmptySucceedsIfNull() {
		final List<Object> list = null;
		assertTrue(ArrayUtils.isEmpty(list));
	}
	
	public void testListIsEmptySucceedsIfEmpty() {
		final List<Object> list = new ArrayList<Object>();
		assertTrue(ArrayUtils.isEmpty(list));
	}
	
	public void testListIsEmptyFailsIfNotEmpty() {
		final Object[] array = new Object[] { new Object() };
		final List<Object> list = Arrays.asList(array);
		assertFalse(ArrayUtils.isEmpty(list));
	}
	
	public void testListIsEmptyFailsIfContainsNullObject() {
		final Object[] array = new Object[] { null };
		final List<Object> list = Arrays.asList(array);
		assertFalse(ArrayUtils.isEmpty(list));
	}
	
	
	public void testListIsNotEmptyFailsIfNull() {
		final List<Object> list = null;
		assertFalse(ArrayUtils.isNotEmpty(list));
	}
	
	public void testListIsNotEmptyFailsIfEmpty() {
		final List<Object> list = new ArrayList<Object>();
		assertFalse(ArrayUtils.isNotEmpty(list));
	}
	
	public void testListIsNotEmptySucceedsIfContainsSingleItem() {
		final Object[] array = new Object[] { new Object() };
		final List<Object> list = Arrays.asList(array);
		assertTrue(ArrayUtils.isNotEmpty(list));
	}
	
	public void testListIsNotEmptySucceedsIfContainsMultipleItems() {
		final Object[] array = new Object[] { new Object(), new String() };
		final List<Object> list = Arrays.asList(array);
		assertTrue(ArrayUtils.isNotEmpty(list));
	}
	
	public void testListIsNotEmptySucceedsIfContainsNullObject() {
		final Object[] array = new Object[] { null };
		final List<Object> list = Arrays.asList(array);
		assertTrue(ArrayUtils.isNotEmpty(list));
	}
}
