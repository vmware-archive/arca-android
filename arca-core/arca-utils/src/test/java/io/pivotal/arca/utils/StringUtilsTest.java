/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.utils;

import android.test.AndroidTestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class StringUtilsTest extends AndroidTestCase {

	public void testStringIsEmpty() {
		assertTrue(StringUtils.isEmpty(""));
	}

	public void testStringIsEmptyNull() {
		assertTrue(StringUtils.isEmpty(null));
	}

	public void testStringIsEmptyFailure() {
		assertFalse(StringUtils.isEmpty("."));
	}

	public void testStringIsNotEmpty() {
		assertTrue(StringUtils.isNotEmpty("."));
	}

	public void testStringIsNotEmptyNull() {
		assertFalse(StringUtils.isNotEmpty(null));
	}

	public void testStringIsNotEmptyFailure() {
		assertFalse(StringUtils.isNotEmpty(""));
	}

	public void testStringLeftEmpty() {
		assertEquals("", StringUtils.left("", 1));
	}

	public void testStringLeftNegativeIndex() {
		assertEquals("", StringUtils.left("", -1));
	}

	public void testStringLeftNull() {
		assertEquals("", StringUtils.left(null, 1));
	}

	public void testStringLeftIndexLessThanLength() {
		assertEquals(".", StringUtils.left("..", 1));
	}

	public void testStringLeftIndexEqualsLength() {
		assertEquals("..", StringUtils.left("..", 2));
	}

	public void testStringLeftIndexGreaterThanLength() {
		assertEquals("..", StringUtils.left("..", 3));
	}

	public void testStringRightEmpty() {
		assertEquals("", StringUtils.right("", 1));
	}

	public void testStringRightNegativeIndex() {
		assertEquals("", StringUtils.right("", -1));
	}

	public void testStringRightNull() {
		assertEquals("", StringUtils.right(null, 1));
	}

	public void testStringRightIndexLessThanLength() {
		assertEquals(".", StringUtils.right("..", 1));
	}

	public void testStringRightIndexEqualsLength() {
		assertEquals("..", StringUtils.right("..", 2));
	}

	public void testStringRightIndexGreaterThanLength() {
		assertEquals("..", StringUtils.right("..", 3));
	}

	public void testStringAppendAllNull() {
		assertEquals(null, StringUtils.append(null, null, null));
	}

	public void testStringAppendOriginalStringNull() {
		assertEquals(">", StringUtils.append(null, ">", "!"));
	}

	public void testStringAppendAppendedStringNull() {
		assertEquals("<!", StringUtils.append("<", null, "!"));
	}

	public void testStringAppendDelimeterStringNull() {
		assertEquals("<>", StringUtils.append("<", ">", null));
	}

	public void testStringAppend() {
		assertEquals("<!>", StringUtils.append("<", ">", "!"));
	}

	public void testStringJoinNullCollection() {
		final Collection<String> strings = null;
		assertEquals("", StringUtils.join(strings, "!"));
	}

	public void testStringJoinEmptyCollection() {
		final Collection<String> strings = new ArrayList<String>();
		assertEquals("", StringUtils.join(strings, "!"));
	}

	public void testStringJoinSingleItemCollection() {
		final String[] array = new String[] { "<" };
		final Collection<String> strings = Arrays.asList(array);
		assertEquals("<", StringUtils.join(strings, "!"));
	}

	public void testStringJoinMultipleItemCollection() {
		final String[] array = new String[] { "<", ">" };
		final Collection<String> strings = Arrays.asList(array);
		assertEquals("<!>", StringUtils.join(strings, "!"));
	}

	public void testStringJoinCollectionWithNullDelimeter() {
		final String[] array = new String[] { "<", ">" };
		final Collection<String> strings = Arrays.asList(array);
		assertEquals("<>", StringUtils.join(strings, null));
	}

	public void testStringJoinNullArray() {
		final String[] strings = null;
		assertEquals("", StringUtils.join(strings, "!"));
	}

	public void testStringJoinEmptyArray() {
		final String[] strings = new String[0];
		assertEquals("", StringUtils.join(strings, "!"));
	}

	public void testStringJoinSingleItemArray() {
		final String[] strings = new String[] { "<" };
		assertEquals("<", StringUtils.join(strings, "!"));
	}

	public void testStringJoinMultipleItemArray() {
		final String[] strings = new String[] { "<", ">" };
		assertEquals("<!>", StringUtils.join(strings, "!"));
	}

	public void testStringJoinArrayWithNullDelimeter() {
		final String[] strings = new String[] { "<", ">" };
		assertEquals("<>", StringUtils.join(strings, null));
	}
}
