/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.threading;

public class Identifier<T> {
	private final T mData;
	private final int mHashCode;

	public Identifier(final T data) {
		mData = data;
		mHashCode = data.hashCode();
	}

	public T getData() {
		return mData;
	}

	@Override
	public int hashCode() {
		return mHashCode;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof Identifier)) {
			return false;
		}
		return mData.equals(((Identifier<?>) o).mData);
	}
}
