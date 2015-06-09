/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.service;

public class TestProcessingRequest extends ProcessingRequest<String> {

	public TestProcessingRequest(final ProcessingPrioritizable<String> prioritizable) {
		super(prioritizable, 0, null);
	}

	@Override
	public void notifyComplete(final ServiceError error) {

	}

}
