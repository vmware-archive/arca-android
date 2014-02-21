package com.arca.dispatcher;

public class InsertResult extends Result<Integer> {

	public InsertResult(final Integer data) {
		super(data);
	}

	public InsertResult(final Error error) {
		super(error);
	}

}
