package com.arca.threading;

public class RequestIdentifier<T> {
	private final T mData;
	private final int mHashCode;

	public RequestIdentifier(final T data) {
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
		if (!(o instanceof RequestIdentifier)) {
			return false;
		}
		return mData.equals(((RequestIdentifier<?>) o).mData);
	}
}
