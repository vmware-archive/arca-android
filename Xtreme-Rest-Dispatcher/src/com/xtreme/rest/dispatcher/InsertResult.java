package com.xtreme.rest.dispatcher;


public class InsertResult extends Result<Integer> {

	public InsertResult(final Integer data) {
		super(data);
	}

	public InsertResult(final Error error) {
		super(error);
	}

	public boolean isValid() {
		// TODO this should return the result from the validator?
		return true;
	}

}
