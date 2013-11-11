package com.xtreme.rest.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ResourceCursorAdapter;

import com.xtreme.rest.animators.ViewAnimator;
import com.xtreme.rest.binders.TextViewBinder;
import com.xtreme.rest.binders.ViewBinder;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ModernCursorAdapter extends ResourceCursorAdapter {
	
	private final TextViewBinder mDefaultBinder = new TextViewBinder();
	private final CursorAdapterHelper mHelper;
	
	private ViewBinder mViewBinder;
	private ViewAnimator mViewAnimator;

	private int mCurrentPosition = 0;
	private int mLastPosition = -1;
	
	public ModernCursorAdapter(final Context context, final int layout, final String[] columnNames, final int[] viewIds) {
		super(context, layout, null, 0);
		mHelper = new CursorAdapterHelper(columnNames, viewIds);
	}
	
	public void setViewBinder(final ViewBinder binder) {
		mViewBinder = binder;
	}

	public void setViewAnimator(final ViewAnimator animator) {
		mViewAnimator = animator;
	}
	
	@Override
	public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
		final View view = super.newView(context, cursor, parent);
		mHelper.findViews(view);
		return view;
	}
	
	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		mLastPosition = mCurrentPosition;
		mCurrentPosition = position;
		return super.getView(position, convertView, parent);
	}

	@Override
	public void bindView(final View containerView, final Context context, final Cursor cursor) {
		final int count = mHelper.getViewCount();

		for (int i = 0; i < count; i++) {
			
			final int columnIndex = mHelper.getColumnIndex(i);
			final View view = mHelper.getView(containerView, i);
			
			boolean bound = false;
			
			if (mViewBinder != null) {
				bound = mViewBinder.setViewValue(view, cursor, columnIndex);
			}
			
			if (!bound) {
				bound = mDefaultBinder.setViewValue(view, cursor, columnIndex);
			}
			
			if (!bound) {
				throw new IllegalStateException("Connot bind to view: " + view.getClass());
			}
		}
		
		animateView(containerView);
	}

	protected void animateView(final View view) {
		if (mViewAnimator == null) {
			return;
		}
		
		if (mCurrentPosition >= mLastPosition) { 
			mViewAnimator.animateViewOnForwardScroll(view, mCurrentPosition);
		} else {
			mViewAnimator.animateViewOnBackwardScroll(view, mCurrentPosition);
		}
	}

	@Override
	public Cursor swapCursor(final Cursor cursor) {
		// super.swapCursor() will notify observers before we have
		// a valid mapping, make sure we have a mapping before this
		// happens
		mHelper.findColumns(cursor);
		return super.swapCursor(cursor);
	}
}
