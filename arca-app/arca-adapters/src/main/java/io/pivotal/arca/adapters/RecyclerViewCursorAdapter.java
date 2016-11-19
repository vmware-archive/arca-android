package io.pivotal.arca.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collection;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RecyclerViewCursorAdapter extends RecyclerView.Adapter<RecyclerViewCursorAdapter.ViewHolder> {

    private final ModernCursorAdapter mCursorAdapter;

    public RecyclerViewCursorAdapter(final ModernCursorAdapter cursorAdapter) {
        cursorAdapter.registerDataSetObserver(new RecyclerViewDataSetObserver());
        mCursorAdapter = cursorAdapter;
        setHasStableIds(true);
    }

    public RecyclerViewCursorAdapter(final Context context, final int layout, final Collection<Binding> bindings) {
        this(new ModernCursorAdapter(context, layout, bindings));
    }

    @Override
    public RecyclerViewCursorAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View view = mCursorAdapter.newView(parent.getContext(), mCursorAdapter.getCursor(), parent);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewCursorAdapter.ViewHolder holder, final int position) {
        final Cursor cursor = (Cursor) mCursorAdapter.getItem(position);
        mCursorAdapter.bindView(holder.getView(), holder.getView().getContext(), cursor);
    }

    @Override
    public int getItemViewType(final int position) {
        return mCursorAdapter.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }

    @Override
    public long getItemId(final int position) {
        return mCursorAdapter.getItemId(position);
    }

    public Object getItem(final int position) {
        return mCursorAdapter.getItem(position);
    }

    public void setViewBinder(final ViewBinder viewBinder) {
        mCursorAdapter.setViewBinder(viewBinder);
    }

    public Cursor swapCursor(final Cursor newCursor) {
        return mCursorAdapter.swapCursor(newCursor);
    }

    public Cursor getCursor() {
        return mCursorAdapter.getCursor();
    }

    public boolean hasResults() {
        final Cursor cursor = getCursor();
        return cursor != null && cursor.getCount() > 0;
    }

    private class RecyclerViewDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final View mView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;
            mView.setClickable(true);
        }

        public View getView() {
            return mView;
        }
    }
}
