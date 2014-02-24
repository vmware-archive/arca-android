package com.arca.utils.test;

import java.util.Collection;

import android.test.AndroidTestCase;

import com.arca.utils.ClassMapping;

public class ClassMappingTest extends AndroidTestCase {

	public void testClassMappingCountWithInvalidClass() throws ClassNotFoundException {
		final ClassMapping<Object> mapping = new ClassMapping<Object>();
		mapping.append(0, Class.forName("com.arca.utils.test.ClassMappingTest$TestClass"));
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
