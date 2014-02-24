package com.rottentomatoes.app.adapters;

import java.util.Collection;

import android.content.Context;
import android.database.Cursor;
import android.view.View;

import com.arca.adapters.Binding;
import com.arca.adapters.SupportCursorAdapter;
import com.rottentomatoes.app.animators.ViewAnimator;

public class AnimationCursorAdapter extends SupportCursorAdapter {
	
	private ViewAnimator mViewAnimator;


	public AnimationCursorAdapter(final Context context, final int layout, final Collection<Binding> bindings) {
		super(context, layout, bindings);
	}
	
	public void setViewAnimator(final ViewAnimator animator) {
		mViewAnimator = animator;
	}
	
	@Override
	public void bindView(final View container, final Context context, final Cursor cursor) {
		super.bindView(container, context, cursor);
		
		animateView(container, context, cursor);
	}

	private void animateView(final View container, final Context context, final Cursor cursor) {
		if (mViewAnimator != null) {
			mViewAnimator.animateView(container, context, cursor);
		}
	}
	
}
