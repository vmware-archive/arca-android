package com.xtreme.rest.adapters;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Build;
import android.util.SparseArray;
import android.view.View;

class CursorAdapterHelper {
	
	private final int[] mViewIds;
	private final String[] mColumnNames;
	private int[] mColumnIndices;

	public CursorAdapterHelper(final String[] columnNames, final int[] viewIds) {
		mColumnNames = columnNames;
		mViewIds = viewIds;
	}
	
	public int getViewCount() {
		return mViewIds.length;
	}

	public int getColumnIndex(final int i) {
		return mColumnIndices[i];
	}
	
	public void findViews(final View view) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			setViewHolder(view);
		} else {
			setViewTags(view);
		}
	}
	
	private void setViewHolder(final View view) {
		final ViewHolder holder = new ViewHolder(view, mViewIds);
		view.setTag(holder);
	}
	
	@SuppressLint("ViewTag")
	private void setViewTags(final View view) {
		final int count = mViewIds.length;
		for (int i = 0; i < count; i++) {
			final int id = mViewIds[i];
			view.setTag(id, view.findViewById(id));
		}
	}

	public void findColumns(final Cursor cursor) {
		if (cursor == null) {
			mColumnIndices = null;
			return;
		}

		final int count = mColumnNames.length;
		if (mColumnIndices == null || mColumnIndices.length != count) {
			mColumnIndices = new int[count];
		}
		
		for (int i = 0; i < count; i++) {
			mColumnIndices[i] = cursor.getColumnIndexOrThrow(mColumnNames[i]);
		}
	}
	
	public View getView(final View container, final int index) {
		final int viewId = mViewIds[index];
		final View view = getViewForId(container, viewId);

		if (view == null) {
			throw new IllegalStateException("Unable to find view for id: " + viewId);
		}

		return view;
	}

	private static View getViewForId(final View container, final int viewId) {
		final View view = (View) container.getTag(viewId);
		if (view != null) {
			return view;
		}
		final ViewHolder holder = (ViewHolder) container.getTag();
		if (holder != null) {
			return holder.get(viewId);
		} else {
			return null;
		}
	}
	
	private static final class ViewHolder {
		private final SparseArray<View> mViews = new SparseArray<View>();

		public ViewHolder(final View view, final int[] viewIds) {
			for (int i = 0; i < viewIds.length; i++) {
				final int id = viewIds[i];
				mViews.put(id, view.findViewById(id));
			}
		}

		public final View get(final int viewId) {
			return mViews.get(viewId);
		}
	}

	
}
