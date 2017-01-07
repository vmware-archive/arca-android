package io.pivotal.arca.fragments;

import android.database.Cursor;
import android.view.View;
import android.widget.Toast;

import io.pivotal.arca.dispatcher.Error;
import io.pivotal.arca.dispatcher.QueryResult;

public class ArcaViewManager {

    private final View mView;

    private int mContentId = android.R.id.content;
    private int mProgressId = android.R.id.progress;
    private int mEmptyId = android.R.id.empty;
    private int mListId = android.R.id.list;

    public ArcaViewManager(final View view) {
        mView = view;
    }

    public void setContentId(final int id) {
        mContentId = id;
    }

    public void setProgressId(final int id) {
        mProgressId = id;
    }

    public void setEmptyId(final int id) {
        mEmptyId = id;
    }

    private View getView() {
        return mView;
    }

    private View getContentView() {
        final View content = getView().findViewById(mContentId);
        return content != null ? content : getView().findViewById(mListId);
    }

    private View getProgressView() {
        return getView().findViewById(mProgressId);
    }

    private View getEmptyView() {
        return getView().findViewById(mEmptyId);
    }

    public void showContentView() {
        if (getContentView() != null) getContentView().setVisibility(View.VISIBLE);
        if (getProgressView() != null) getProgressView().setVisibility(View.INVISIBLE);
        if (getEmptyView() != null) getEmptyView().setVisibility(View.INVISIBLE);
    }

    public void showProgressView() {
        if (getContentView() != null) getContentView().setVisibility(View.INVISIBLE);
        if (getProgressView() != null) getProgressView().setVisibility(View.VISIBLE);
        if (getEmptyView() != null) getEmptyView().setVisibility(View.INVISIBLE);
    }

    public void showEmptyView() {
        if (getContentView() != null) getContentView().setVisibility(View.INVISIBLE);
        if (getProgressView() != null) getProgressView().setVisibility(View.INVISIBLE);
        if (getEmptyView() != null) getEmptyView().setVisibility(View.VISIBLE);
    }

    public void showError(final Error error) {
        final String message = String.format("%s", error.getMessage());
        Toast.makeText(mView.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void checkResult(final QueryResult result) {
        final Cursor data = result.getData();

        if (data != null && data.getCount() > 0) {
            showContentView();
        } else if (!result.isSyncing()) {
            showEmptyView();
        }
    }

    public void checkError(final Error error) {
        if (error != null) {
            showEmptyView();
            showError(error);
        }
    }
}
