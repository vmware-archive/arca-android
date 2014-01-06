package com.xtreme.rest.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.xtreme.rest.loader.ContentLoader;
import com.xtreme.rest.loader.ContentLoaderFactory;
import com.xtreme.rest.loader.ContentLoaderListener;
import com.xtreme.rest.loader.ContentRequest;

/**
 * This class provides a basic implementation of a single {@link ContentLoader} for a fragment. Using this class, fragments can request data
 * simply by calling {@link ContentLoaderSupportFragment#execute(ContentRequest)}.<br>
 * <br>
 * <b>IMPORTANT:</b> You MUST call the super when onViewCreated and onDestroyView is called in your fragment!<br>
 * <br>
 * <b>ALSO IMPORTANT:</b> {@link ContentLoaderSupportFragment#execute(ContentRequest)} may only be called after onViewCreated has been called and
 * before onDestroyView has been called.
 */
public abstract class ContentLoaderSupportFragment extends Fragment implements ContentLoaderListener {

	private ContentLoader mContentLoader;
	
	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		mContentLoader = ContentLoaderFactory.generateContentLoader(this, this);
	}

	@Override
	public void onDestroyView() {
		getContentLoader().destroy();
		super.onDestroyView();
	}
	
	/**
	 * @return The {@link ContentLoader} instance used by this fragment.
	 */
	protected ContentLoader getContentLoader() {
		return mContentLoader;
	}
	
	/**
	 * @see ContentLoader#execute(ContentRequest)
	 */
	protected void execute(final ContentRequest request) {
		getContentLoader().execute(request);
	}
}
