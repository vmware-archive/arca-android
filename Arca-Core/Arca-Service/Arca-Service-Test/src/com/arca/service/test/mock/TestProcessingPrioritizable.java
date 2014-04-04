package com.arca.service.test.mock;

import com.arca.service.ProcessingPrioritizable;
import com.arca.threading.Identifier;

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
