package com.rottentomatoes.app.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.rottentomatoes.app.R;
import com.rottentomatoes.app.datasets.MovieTable;
import com.rottentomatoes.app.providers.RottenTomatoesContentProvider;
import com.xtreme.rest.adapters.ItemCursorAdapter;
import com.xtreme.rest.broadcasts.RestError;
import com.xtreme.rest.fragments.ContentLoaderItemSupportFragment;
import com.xtreme.rest.loader.ContentRequest;
import com.xtreme.rest.loader.ContentResponse;
import com.xtremelabs.imageutils.ImageLoader;

public class MovieFragment extends ContentLoaderItemSupportFragment {
	
	private static final String[] COLUMN_NAMES = new String[] { 
        MovieTable.Columns.TITLE,
        MovieTable.Columns.YEAR,
        MovieTable.Columns.MPAA_RATING,
        MovieTable.Columns.RUNTIME,
        MovieTable.Columns.CRITICS_CONSENSUS,
        MovieTable.Columns.SYNOPSIS,
        MovieTable.Columns.IMAGE_URL,
	};

	private static final int[] VIEW_IDS = new int[] { 
        R.id.movie_title,
        R.id.movie_year,
        R.id.movie_mpaa_rating,
        R.id.movie_runtime,
        R.id.movie_critics_consensus,
        R.id.movie_synopsis,
        R.id.movie_image,
	};

	private String mId;
	private ImageLoader mImageLoader;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_movie, container, false);
		mImageLoader = ImageLoader.buildImageLoaderForSupportFragment(this);
		return view;
	}
	
	@Override
	public void onDestroyView() {
		mImageLoader.destroy();
		super.onDestroyView();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		loadMovie(mId);
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

	private void loadMovie(final String id) {
		final Uri baseUri = RottenTomatoesContentProvider.Uris.MOVIES_URI;
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
		getView().findViewById(R.id.movie_container).setVisibility(View.VISIBLE);
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
		switch (view.getId()) {
		case R.id.movie_image:
		    final String url = cursor.getString(columnIndex);
		    mImageLoader.loadImage((ImageView) view, url);
		    return true;

		default:
		    return false;
		}
	}
	
}
