package com.arca.service;

import com.arca.threading.PriorityAccessor;
import com.arca.threading.QueuePriorityAccessor;
import com.arca.threading.StackPriorityAccessor;

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
