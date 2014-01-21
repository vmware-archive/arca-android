package com.xtreme.rest.dispatcher;


public class UpdateResult extends ContentResult<Integer> {

	public UpdateResult(final Integer data) {
		super(data);
	}

	public UpdateResult(final ContentError error) {
		super(error);
	}

	public boolean isValid() {
		// TODO this should return the result from the validator?
		return true;
	}

}
