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