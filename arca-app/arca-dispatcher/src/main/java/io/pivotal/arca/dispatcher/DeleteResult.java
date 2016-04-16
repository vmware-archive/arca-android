package io.pivotal.arca.dispatcher;

public class DeleteResult extends Result<Integer> {

	public DeleteResult(final Integer data) {
		super(data);
	}

	public DeleteResult(final Error error) {
		super(error);
	}

}
