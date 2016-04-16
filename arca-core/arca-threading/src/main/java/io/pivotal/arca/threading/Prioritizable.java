package io.pivotal.arca.threading;

public abstract class Prioritizable {
	private volatile boolean mIsCancelled = false;
	private volatile boolean mCancelable = true;

	public abstract Identifier<?> getIdentifier();

	public abstract void execute();

	final synchronized boolean isCancelled() {
		return mIsCancelled;
	}

	final synchronized boolean cancel() {
		if (mCancelable) {
			mIsCancelled = true;
		}
		return mIsCancelled;
	}

	final void disableCancel() {
		mCancelable = false;
	}
}
