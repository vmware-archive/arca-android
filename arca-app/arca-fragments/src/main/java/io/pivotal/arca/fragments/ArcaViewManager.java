package io.pivotal.arca.fragments;

import android.view.View;
import android.widget.Toast;

import io.pivotal.arca.dispatcher.Error;
import io.pivotal.arca.dispatcher.QueryResult;

public class ArcaViewManager {

    private final View mView;

    private int mProgressId = android.R.id.progress;
    private int mEmptyId = android.R.id.empty;
    private int mContentId = android.R.id.list;

    public ArcaViewManager(final View view) {
        mView = view;
    }

    public void setProgressId(final int id) {
        mProgressId = id;
    }

    public void setEmptyId(final int id) {
        mEmptyId = id;
    }

    public void setContentId(final int id) {
        mContentId = id;
    }

    private View getView() {
        return mView;
    }

    private View getProgressView() {
        return getView().findViewById(mProgressId);
    }

    private View getEmptyView() {
        return getView().findViewById(mEmptyId);
    }

    private View getContentView() {
        return getView().findViewById(mContentId);
    }

    public void showProgressView() {
        getContentView().setVisibility(View.INVISIBLE);
        getProgressView().setVisibility(View.VISIBLE);
        getEmptyView().setVisibility(View.INVISIBLE);
    }

    public void showContentView() {
        getContentView().setVisibility(View.VISIBLE);
        getProgressView().setVisibility(View.INVISIBLE);
        getEmptyView().setVisibility(View.INVISIBLE);
    }

    public void showEmptyView() {
        getContentView().setVisibility(View.INVISIBLE);
        getProgressView().setVisibility(View.INVISIBLE);
        getEmptyView().setVisibility(View.VISIBLE);
    }

    public void toastError(final Error error) {
        final String message = String.format("%s", error.getMessage());
        Toast.makeText(mView.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void checkResult(final QueryResult result) {
        if (result.getResult().getCount() > 0) {
            showContentView();
        } else if (!result.isSyncing()) {
            showEmptyView();
        }
    }

    public void checkError(final Error error) {
        if (error != null) {
            showEmptyView();
            toastError(error);
        }
    }
}
