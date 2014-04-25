package com.xtreme.rest.service;

import com.xtreme.threading.PriorityAccessor;
import com.xtreme.threading.QueuePriorityAccessor;
import com.xtreme.threading.StackPriorityAccessor;

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
