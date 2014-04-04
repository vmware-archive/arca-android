package com.arca.threading;

public class PrioritizableRequest implements Runnable {

	private final int mAccessorIndex;
	private final Prioritizable mPrioritizable;

	public PrioritizableRequest(final Prioritizable prioritizable, final int accessorIndex) {
		if (prioritizable == null || accessorIndex < 0)
			throw new IllegalArgumentException("Cannot pass null prioritizable or negative accessorIndex to constructor.");

		mPrioritizable = prioritizable;
		mAccessorIndex = accessorIndex;
	}

	public int getAccessorIndex() {
		return mAccessorIndex;
	}

	public Prioritizable getPrioritizable() {
		return mPrioritizable;
	}

	@Override
	public void run() {
		mPrioritizable.disableCancel();
		if (!mPrioritizable.isCancelled()) {
			mPrioritizable.execute();
		}
	}

	public boolean isCancelled() {
		return mPrioritizable.isCancelled();
	}

	public Identifier<?> getIdentifier() {
		return mPrioritizable.getIdentifier();
	}

	public boolean cancel() {
		return mPrioritizable.cancel();
	}
}
