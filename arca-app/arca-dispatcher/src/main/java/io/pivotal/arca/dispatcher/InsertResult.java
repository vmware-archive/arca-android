/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.dispatcher;

public class InsertResult extends Result<Integer> {

	public InsertResult(final Integer data) {
		super(data);
	}

	public InsertResult(final Error error) {
		super(error);
	}

}
