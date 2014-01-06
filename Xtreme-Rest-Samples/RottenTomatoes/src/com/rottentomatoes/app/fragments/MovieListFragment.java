package com.rottentomatoes.app.fragments;

import java.util.Arrays;
import java.util.Collection;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.rottentomatoes.app.R;
import com.rottentomatoes.app.activities.MovieActivity;
import com.rottentomatoes.app.animators.SimpleAdapterAnimator;
import com.rottentomatoes.app.datasets.MovieTable;
import com.rottentomatoes.app.datasets.MovieTypeView;
import com.rottentomatoes.app.providers.RottenTomatoesContentProvider;
import com.xtreme.rest.adapters.SupportCursorAdapter;
import com.xtreme.rest.binders.Binding;
import com.xtreme.rest.binders.ViewBinder;
import com.xtreme.rest.broadcasts.RestError;
import com.xtreme.rest.fragments.ContentLoaderAdapterSupportFragment;
import com.xtreme.rest.loader.ContentRequest;
import com.xtreme.rest.loader.ContentResponse;
import com.xtremelabs.imageutils.ImageLoader;

public class MovieListFragment extends ContentLoaderAdapterSupportFragment implements OnItemClickListener, ViewBinder {
	
	private static final Collection<Binding> BINDINGS = Arrays.asList(new Binding[] { 
		new Binding(R.id.list_item_movie_title, MovieTypeView.Columns.TITLE),
		new Binding(R.id.list_item_movie_image, MovieTypeView.Columns.IMAGE_URL),
	});

	private String mType;
	private ImageLoader mImageLoader;
	
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
		mImageLoader = ImageLoader.buildImageLoaderForSupportFragment(this);
		return view;
	}
	
	@Override
	public CursorAdapter onCreateAdapter(final AdapterView<CursorAdapter> adapterView) {
		final SupportCursorAdapter adapter = new SupportCursorAdapter(getActivity(), R.layout.list_item_movie, BINDINGS);
		adapter.setViewAnimator(new SimpleAdapterAnimator());
		adapter.setViewBinder(this);
		return adapter;
	}
	
	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getAdapterView().setOnItemClickListener(this);
	}
	
	@Override
	public void onDestroyView() {
		mImageLoader.destroy();
		super.onDestroyView();
	}
	
	public void setType(final String type) {
		mType = type;
		scrollToTop();
		loadMovies();
	}
	
	private void scrollToTop() {
		//((AbsListView) getAdapterView()).smoothScrollToPosition(0);
	}
	
	@Override
	protected int getAdapterViewId() {
		return R.id.movie_list;
	}
	
	private void loadMovies() {
		final Uri baseUri = RottenTomatoesContentProvider.Uris.MOVIE_TYPES_URI;
		final Uri contentUri = Uri.withAppendedPath(baseUri, mType);
		
		final ContentRequest request = new ContentRequest(contentUri);
		request.setSortOrder("title asc");
		
		execute(request);
		showLoading();
	}
	
	@Override
	public void onError(final RestError error) {
		showError(error.getMessage());
		hideLoading();
	}
	
	@Override
	public void onContentChanged(final ContentResponse response) {
		final CursorAdapter adapter = getCursorAdapter();
		if (adapter.getCount() > 0) {
			showResults();
		} else if (!response.isExecutingRemote()) {
			hideLoading();
		}
	}
	
	private void showResults() {
		getView().findViewById(R.id.movie_list).setVisibility(View.VISIBLE);
		getView().findViewById(R.id.loading).setVisibility(View.INVISIBLE);
	}
	
	private void showLoading() {
		getView().findViewById(R.id.movie_list).setVisibility(View.INVISIBLE);
		getView().findViewById(R.id.loading).setVisibility(View.VISIBLE);
	}
	
	private void hideLoading() {
		getView().findViewById(R.id.loading).setVisibility(View.INVISIBLE);
	}
	
	private void showError(final String message) {
		Toast.makeText(getActivity(), "ERROR: " + message, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
		final Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
		final String itemId = cursor.getString(cursor.getColumnIndex(MovieTable.Columns.ID));
		MovieActivity.newInstance(getActivity(), itemId);
	}

	@Override
	public boolean setViewValue(final View view, final Cursor cursor, final Binding binding) {
		switch (view.getId()) {
		case R.id.list_item_movie_image:
		    final String url = cursor.getString(binding.getColumnIndex());
		    mImageLoader.loadImage((ImageView) view, url);
		    return true;

		default:
		    return false;
		}
	}
}
