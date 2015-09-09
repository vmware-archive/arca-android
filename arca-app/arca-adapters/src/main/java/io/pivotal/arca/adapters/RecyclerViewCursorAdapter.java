/*
 * Copyright (C) 2015 Pivotal Software, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pivotal.arca.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import java.util.Collection;

public abstract class RecyclerViewCursorAdapter<VH extends RecyclerViewCursorAdapter.ArcaBindingRecyclerViewHolder> extends RecyclerView.Adapter<VH> {

    protected boolean mDataValid;
    protected Cursor mCursor;
    protected int mRowIDColumn;

    protected CursorAdapterHelper mCursorAdapterHelper;
    protected Context mContext;
    protected Collection<Binding> mBindings;

    public RecyclerViewCursorAdapter(Context context, final Collection<Binding> bindings, Cursor cursor) {
        this(context, bindings, cursor, null);
    }

    public RecyclerViewCursorAdapter(Context context, final Collection<Binding> bindings, Cursor cursor, ViewBinder viewBinder) {
        super();
        mContext = context;
        mCursor = cursor;
        mBindings = bindings;
        mCursorAdapterHelper = new CursorAdapterHelper(bindings);

        init(cursor);

        if (viewBinder != null) {
            mCursorAdapterHelper.setViewBinder(viewBinder);
        }
    }

    void init(Cursor c) {
        boolean cursorPresent = c != null;
        mCursor = c;
        mDataValid = cursorPresent;
        mRowIDColumn = cursorPresent ? c.getColumnIndexOrThrow("_id") : -1;
        setHasStableIds(true);
    }

    @Override
    public void onBindViewHolder (VH holder, int position) {
        if (!mDataValid) {
            throw new IllegalStateException("This should only be called when the cursor is valid");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("Couldn't move cursor to position " + position);
        }

        onBindViewHolder(holder, mCursor, position);
    }

    public void onBindViewHolder (VH holder, Cursor cursor, int position) {
        for (Binding binding : mBindings) {
            View view = (View) holder.mViews.get(binding.getViewId());
            mCursorAdapterHelper.bindView(view, mContext, cursor, getItemViewType(position));
        }
    }

    public Cursor getCursor() {
        return mCursor;
    }

    @Override
    public int getItemCount () {
        if (mDataValid && mCursor != null) {
            return mCursor.getCount();
        } else {
            return 0;
        }
    }

    @Override
    public long getItemId (int position) {
        if(hasStableIds() && mDataValid && mCursor != null){
            if (mCursor.moveToPosition(position)) {
                return mCursor.getLong(mRowIDColumn);
            } else {
                return RecyclerView.NO_ID;
            }
        } else {
            return RecyclerView.NO_ID;
        }
    }

    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        mCursor = newCursor;
        if (newCursor != null) {
            mRowIDColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            notifyDataSetChanged();
        } else {
            mRowIDColumn = -1;
            mDataValid = false;
            notifyItemRangeRemoved(0, getItemCount());
        }
        return oldCursor;
    }

    public boolean hasResults() {
        final Cursor cursor = getCursor();
        return cursor != null && cursor.getCount() > 0;
    }

    public class ArcaBindingRecyclerViewHolder extends RecyclerView.ViewHolder {
        SparseArray<View> mViews;

        public ArcaBindingRecyclerViewHolder(View itemView, Collection<Binding> bindings) {
            super(itemView);
            mViews = new SparseArray<>();

            for (Binding binding : bindings) {
                mViews.put(binding.getViewId(), itemView.findViewById(binding.getViewId()));
            }
        }
    }
}
