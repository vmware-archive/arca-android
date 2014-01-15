package com.rottentomatoes.app.animators;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.rottentomatoes.app.R;
import com.xtreme.rest.animators.ViewAnimator;

public class SimpleAdapterAnimator implements ViewAnimator {

	private final Set<Integer> mVisited = new HashSet<Integer>();
	
	@Override
	public void animateView(final View view, final Context context, final Cursor cursor) {
		final int position = cursor.getPosition();
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
