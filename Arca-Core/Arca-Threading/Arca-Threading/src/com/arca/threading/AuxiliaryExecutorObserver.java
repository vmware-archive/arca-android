package com.arca.threading;

public interface AuxiliaryExecutorObserver {

	public void onComplete(PrioritizableRequest request);

	public void onCancelled(PrioritizableRequest request);
}