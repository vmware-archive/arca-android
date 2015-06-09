/* 
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.service;

import io.pivotal.arca.threading.PriorityAccessor;
import io.pivotal.arca.threading.QueuePriorityAccessor;
import io.pivotal.arca.threading.StackPriorityAccessor;

public enum Priority {
	LIVE, HIGH, MEDIUM, LOW;

	public static PriorityAccessor[] newAccessorArray() {
		return new PriorityAccessor[] { 
				new StackPriorityAccessor(), // live
				new QueuePriorityAccessor(), // high
				new QueuePriorityAccessor(), // med
				new QueuePriorityAccessor() // low
		};
	}
}
