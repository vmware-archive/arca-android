package com.rottentomatoes.app.animators;

import java.util.HashSet;
import java.util.Set;

import android.view.View;
import android.view.animation.AnimationUtils;

import com.rottentomatoes.app.R;
import com.xtreme.rest.animators.AdapterAnimator;

public class SimpleAdapterAnimator implements AdapterAnimator {

	private final Set<Integer> mVisited = new HashSet<Integer>();
	
	@Override
	public void animateViewOnForwardScroll(final View view, final int position) {
		if (!mVisited.contains(position)) {
			animateViewForPosition(view, position);
			mVisited.add(position);
		}
	}

	@Override
	public void animateViewOnBackwardScroll(final View view, final int position) {
		if (!mVisited.contains(position)) {
			animateViewForPosition(view, position);
			mVisited.add(position);
		}
	}

	private static void animateViewForPosition(final View view, final int position) {
		if (position % 2 == 0) { 
			view.clearAnimation();
			view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_in_left));
		} else {
			view.clearAnimation();
			view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_in_right));
		}
	}

}
