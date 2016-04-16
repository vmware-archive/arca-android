package io.pivotal.arca.service;

import io.pivotal.arca.threading.Identifier;

public class TestProcessingPrioritizable extends ProcessingPrioritizable<String> {

	public TestProcessingPrioritizable() {
		super(null, null);
	}

	@Override
	public Identifier<?> getIdentifier() {
		return new Identifier<String>("empty");
	}

	@Override
	public void execute() {
		// do nothing
	}

}
