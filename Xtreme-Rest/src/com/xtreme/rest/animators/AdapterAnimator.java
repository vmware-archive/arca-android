package com.xtreme.rest.animators;

import android.view.View;

public interface AdapterAnimator {
	public void animateViewOnForwardScroll(View view, int position);
	public void animateViewOnBackwardScroll(View view, int position);
}
