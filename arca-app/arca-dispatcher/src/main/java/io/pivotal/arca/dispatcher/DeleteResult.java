/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.dispatcher;

public class DeleteResult extends Result<Integer> {

	public DeleteResult(final Integer data) {
		super(data);
	}

	public DeleteResult(final Error error) {
		super(error);
	}

}
