package com.appnet.app.animators;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.appnet.app.R;
import com.xtreme.rest.adapters.ViewAnimator;

public class SimpleAdapterAnimator implements ViewAnimator {

	@Override
	public void animateView(final View view, final Context context, final Cursor cursor) {
		view.clearAnimation();
		view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_up));		
	}

}
