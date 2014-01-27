package com.xtreme.rest.provider;


public class Column {

	public static enum Type {
		NULL, INTEGER, REAL, TEXT, BLOB, _ID;
		
		@Override
		public String toString() {
			if (this == _ID) {
				return "INTEGER PRIMARY KEY AUTOINCREMENT";
			} else {
				return name();
			}
		};
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