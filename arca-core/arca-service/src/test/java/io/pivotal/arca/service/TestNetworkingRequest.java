/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.service;

import io.pivotal.arca.service.NetworkingPrioritizable;
import io.pivotal.arca.service.NetworkingRequest;
import io.pivotal.arca.service.ServiceError;

public class TestNetworkingRequest extends NetworkingRequest<String> {

	public TestNetworkingRequest(final NetworkingPrioritizable<String> prioritizable) {
		super(prioritizable, 0, null);
	}

	@Override
	public void notifyComplete(final Object data, final ServiceError error) {

	}

}
