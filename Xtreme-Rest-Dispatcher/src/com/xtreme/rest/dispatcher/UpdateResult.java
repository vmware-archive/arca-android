package com.xtreme.rest.dispatcher;


public class UpdateResult extends Result<Integer> {

	public UpdateResult(final Integer data) {
		super(data);
	}

	public UpdateResult(final Error error) {
		super(error);
	}

	public boolean isValid() {
		// TODO this should return the result from the validator?
		return true;
	}

}
