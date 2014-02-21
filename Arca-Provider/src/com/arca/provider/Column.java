package com.arca.provider;

import java.util.HashMap;
import java.util.Map;

public class Column {

	public static enum Type {
		INTEGER, REAL, TEXT, BLOB, _ID, _STATE;
		
		private static final Map<Type, String> TYPE_MAPPING;
		
		static {
			TYPE_MAPPING = new HashMap<Type, String>();
			TYPE_MAPPING.put(_ID, "INTEGER PRIMARY KEY AUTOINCREMENT");
			TYPE_MAPPING.put(_STATE, "INTEGER DEFAULT 0");
		}
		
		@Override
		public String toString() {
			final String value = TYPE_MAPPING.get(this);
			return value != null ? value : name();
		};
		
		public Column newColumn(final String name) {
			return new Column(name, this);
		}
	}

	public final String name;
	public final Type type;

	public Column(final String name, final Type type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public String toString() {
		return name;
	}
}