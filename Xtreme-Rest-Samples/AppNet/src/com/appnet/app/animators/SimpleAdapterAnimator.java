package com.appnet.app.animators;

import android.view.View;
import android.view.animation.AnimationUtils;

import com.appnet.app.R;
import com.xtreme.rest.animators.AdapterAnimator;

public class SimpleAdapterAnimator implements AdapterAnimator {

	@Override
	public void animateViewOnForwardScroll(final View view, final int position) {
		view.clearAnimation();
		view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_up));
	}

	@Override
	public void animateViewOnBackwardScroll(final View view, final int position) {
		view.clearAnimation();
	}

}
