package com.appnet.app.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appnet.app.R;
import com.appnet.app.datasets.PostTable;
import com.appnet.app.providers.AppNetContentProvider;
import com.xtreme.rest.adapters.ItemCursorAdapter;
import com.xtreme.rest.broadcasts.RestError;
import com.xtreme.rest.fragments.ContentLoaderItemSupportFragment;
import com.xtreme.rest.loader.ContentRequest;
import com.xtreme.rest.loader.ContentResponse;

public class PostFragment extends ContentLoaderItemSupportFragment {
	
	private static final String[] COLUMN_NAMES = new String[] { 
        PostTable.Columns.TEXT,
	};

	private static final int[] VIEW_IDS = new int[] { 
        R.id.post_text,
	};

	private String mId;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_post, container, false);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		loadPost(mId);
	}
	
	public void setId(final String id) {
		mId = id;
	}

	@Override
	public String[] getColumnNames() {
		return COLUMN_NAMES;
	}

	@Override
	public int[] getViewIds() {
		return VIEW_IDS;
	}

	private void loadPost(final String id) {
		final Uri baseUri = AppNetContentProvider.Uris.POSTS_URI;
		final Uri contentUri = Uri.withAppendedPath(baseUri, id);
		execute(new ContentRequest(contentUri));
	}
	
	@Override
	public void onContentChanged(final ContentResponse response) {
		final ItemCursorAdapter adapter = getItemAdapter();
		if (adapter.hasResults()) {
			showResults();
		} else {
			hideLoading();
		}
	}
	
	private void showResults() {
		getView().findViewById(R.id.post_container).setVisibility(View.VISIBLE);
		getView().findViewById(R.id.loading).setVisibility(View.INVISIBLE);
	}
	
	private void hideLoading() {
		getView().findViewById(R.id.loading).setVisibility(View.INVISIBLE);
	}
	
	@Override
	public void onError(final RestError error) {
		Toast.makeText(getActivity(), "ERROR: " + error.getMessage(), Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public boolean setViewValue(final View view, final Cursor cursor, final int columnIndex) {
		return false;
	}
	
}
