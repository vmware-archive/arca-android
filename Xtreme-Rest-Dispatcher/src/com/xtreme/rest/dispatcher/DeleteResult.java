package com.xtreme.rest.dispatcher;


public class DeleteResult extends Result<Integer> {

	public DeleteResult(final Integer data) {
		super(data);
	}

	public DeleteResult(final Error error) {
		super(error);
	}

	public boolean isValid() {
		// TODO this should return the result from the validator?
		return true;
	}

}
