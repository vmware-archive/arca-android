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
package io.pivotal.arca.provider;

import android.test.AndroidTestCase;

import io.pivotal.arca.provider.CollectionUtils;

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
