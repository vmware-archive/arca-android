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
package io.pivotal.arca.utils;

import android.test.AndroidTestCase;

import io.pivotal.arca.utils.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
