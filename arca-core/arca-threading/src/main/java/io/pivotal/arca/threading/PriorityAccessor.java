/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.threading;

public interface PriorityAccessor {

	public void attach(PrioritizableRequest request);

	public PrioritizableRequest detachHighestPriorityItem();

	public PrioritizableRequest peek();

	public int size();

	public void clear();

	// TODO: Need to create a public void remove(PrioritizableRequest request)
	// method or else AuxiliaryExecutor's remove method will not work.
}
