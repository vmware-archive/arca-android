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
package com.arca.provider;

public class Column {

	public static enum Type {
		INTEGER, REAL, TEXT, BLOB, NONE;

		public Column newColumn(final String name) {
			return newColumn(name, null);
		}

		public Column newColumn(final String name, final String options) {
			return new Column(name, this, options);
		}
	}

	public final String name;
	public final Type type;
	public final String options;

	public Column(final String name, final Type type) {
		this(name, type, null);
	}

	public Column(final String name, final Type type, final String options) {
		this.name = name;
		this.type = type;
		this.options = options;
	}

	@Override
	public String toString() {
		return name;
	}
}