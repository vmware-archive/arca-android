/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.dispatcher;

public interface QueryListener extends RequestListener<QueryResult> {
	@Override
	public void onRequestComplete(QueryResult result);
}
