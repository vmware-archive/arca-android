/*
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Modified BSD License.
 *
 * All rights reserved.
 */
package io.pivotal.arca.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemClick implements RecyclerView.OnItemTouchListener {

    public interface Listener {
        public void onItemClick(RecyclerView recyclerView, View view, int position, long id);

        public void onItemLongClick(RecyclerView recyclerView, View view, int position, long id);
    }

    private Listener mListener;
    private GestureDetector mGestureDetector;

    public RecyclerItemClick(final Context context, final RecyclerView recyclerView, final Listener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(final MotionEvent event) {
                return true;
            }

            @Override
            public void onLongPress(final MotionEvent event) {
                final View childView = recyclerView.findChildViewUnder(event.getX(), event.getY());

                if (childView != null && mListener != null) {
                    final long itemId = recyclerView.getChildItemId(childView);
                    final int position = recyclerView.getChildAdapterPosition(childView);
                    mListener.onItemLongClick(recyclerView, childView, position, itemId);
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(final RecyclerView recyclerView, final MotionEvent event) {
        final View childView = recyclerView.findChildViewUnder(event.getX(), event.getY());

        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(event)) {
            final long itemId = recyclerView.getChildItemId(childView);
            final int position = recyclerView.getChildAdapterPosition(childView);
            mListener.onItemClick(recyclerView, childView, position, itemId);
        }

        return false;
    }

    @Override
    public void onTouchEvent(final RecyclerView view, final MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(final boolean disallowIntercept) {
    }
}
