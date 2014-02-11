package com.xtreme.rest.dispatcher;

public class UpdateResult extends Result<Integer> {

	public UpdateResult(final Integer data) {
		super(data);
	}

	public UpdateResult(final Error error) {
		super(error);
	}

}
