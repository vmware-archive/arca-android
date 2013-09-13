package com.xtreme.rest.service.test.utils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

public class AssertionLatch extends CountDownLatch {

	public AssertionLatch(final int count) {
		super(count);
	}
	
	@Override
	public void countDown() {
		final long count = getCount();
		if (count == 0) {
			Assert.fail("This latch has already finished.");
		} else {
			super.countDown();
		}
	}
	
	public void assertComplete() {
		try {
			Assert.assertTrue(await(0, TimeUnit.SECONDS));
		} catch (final InterruptedException e) {
			Assert.fail();
		}
	}
}