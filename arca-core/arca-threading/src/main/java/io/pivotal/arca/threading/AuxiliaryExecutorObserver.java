/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.threading;

public interface AuxiliaryExecutorObserver {

	public void onComplete(PrioritizableRequest request);

	public void onCancelled(PrioritizableRequest request);
}