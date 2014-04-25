package com.crunchbase.app.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.crunchbase.app.R;
import com.crunchbase.app.datasets.CompanyTable;
import com.crunchbase.app.providers.CrunchBaseContentProvider;
import com.xtreme.rest.adapters.ItemCursorAdapter;
import com.xtreme.rest.broadcasts.RestError;
import com.xtreme.rest.fragments.ContentLoaderItemSupportFragment;
import com.xtreme.rest.loader.ContentRequest;
import com.xtreme.rest.loader.ContentResponse;
import com.xtremelabs.imageutils.ImageLoader;

public class CompanyFragment extends ContentLoaderItemSupportFragment {
	
	private static final String[] COLUMN_NAMES = new String[] { 
        CompanyTable.Columns.NAME,
        CompanyTable.Columns.CATEGORY_CODE,
        CompanyTable.Columns.DESCRIPTION,
        CompanyTable.Columns.OVERVIEW,
        CompanyTable.Columns.IMAGE_URL,
	};

	private static final int[] VIEW_IDS = new int[] { 
        R.id.company_name,
        R.id.company_category_code,
        R.id.company_description,
        R.id.company_overview,
        R.id.company_image,
	};

	private String mId;
	private ImageLoader mImageLoader;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_company, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mImageLoader = ImageLoader.buildImageLoaderForSupportFragment(this);
	}
	
	@Override
	public void onDestroyView() {
		mImageLoader.destroy();
		super.onDestroyView();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		loadCompany(mId);
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

	private void loadCompany(final String id) {
		final Uri baseUri = CrunchBaseContentProvider.Uris.COMPANIES_URI;
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
		getView().findViewById(R.id.company_container).setVisibility(View.VISIBLE);
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
		case R.id.company_image:
		    final String url = cursor.getString(columnIndex);
		    mImageLoader.loadImage((ImageView) view, url);
		    return true;

		default:
		    return false;
		}
	}
	
}
