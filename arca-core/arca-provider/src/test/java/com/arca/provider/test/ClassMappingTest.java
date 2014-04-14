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
package com.arca.provider.test;

import android.test.AndroidTestCase;

import com.arca.provider.ClassMapping;

import java.util.Collection;

public class ClassMappingTest extends AndroidTestCase {

	public void testClassMappingCountWithInvalidClass() throws ClassNotFoundException {
		final ClassMapping<Object> mapping = new ClassMapping<Object>();
		mapping.append(0, Class.forName("com.arca.provider.test.ClassMappingTest$TestClass"));
		final Collection<Object> objects = mapping.values();
		assertEquals(0, objects.size());
	}

	public void testClassMappingCountIfEmpty() {
		final ClassMapping<Object> mapping = new ClassMapping<Object>();
		final Collection<Object> objects = mapping.values();
		assertEquals(0, objects.size());
	}

	public void testClassMappingCountWithSingleInstance() {
		final ClassMapping<Object> mapping = new ClassMapping<Object>();
		mapping.append(0, Object.class);
		assertEquals(1, mapping.values().size());
	}

	public void testClassMappingCountWithMultipleInstancesOfTheSameClass() {
		final ClassMapping<Object> mapping = new ClassMapping<Object>();
		mapping.append(0, Object.class);
		mapping.append(1, Object.class);
		assertEquals(1, mapping.values().size());
	}

	public void testClassMappingCountWithInstancesOfDifferentClasses() {
		final ClassMapping<Object> mapping = new ClassMapping<Object>();
		mapping.append(0, Object.class);
		mapping.append(1, String.class);
		assertEquals(2, mapping.values().size());
	}

	public void testClassMappingCountAfterReplacingInstanceAtIndex() {
		final ClassMapping<Object> mapping = new ClassMapping<Object>();
		mapping.append(1, Object.class);
		mapping.append(1, String.class);
		assertEquals(1, mapping.values().size());
	}

	public void testClassMappingTypeAfterReplacingInstanceAtIndex() {
		final ClassMapping<Object> mapping = new ClassMapping<Object>();
		mapping.append(1, Object.class);
		mapping.append(1, String.class);
		assertEquals(String.class, mapping.get(1).getClass());
	}

	public void testClassMappingSameObjectAtDifferentIndexes() {
		final ClassMapping<Object> mapping = new ClassMapping<Object>();
		mapping.append(0, Object.class);
		mapping.append(1, Object.class);
		assertEquals(mapping.get(0), mapping.get(1));
	}

	// =================================

	@SuppressWarnings("unused")
	private static final class TestClass {

		TestClass() {

		}
	}
}
