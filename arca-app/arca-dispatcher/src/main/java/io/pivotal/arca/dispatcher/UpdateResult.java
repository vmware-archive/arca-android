/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.dispatcher;

public class UpdateResult extends Result<Integer> {

	public UpdateResult(final Integer data) {
		super(data);
	}

	public UpdateResult(final Error error) {
		super(error);
	}

}
