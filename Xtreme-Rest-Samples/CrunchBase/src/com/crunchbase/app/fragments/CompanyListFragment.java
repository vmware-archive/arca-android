package com.crunchbase.app.fragments;

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
import android.widget.ListView;
import android.widget.Toast;

import com.crunchbase.app.R;
import com.crunchbase.app.activities.CompanyActivity;
import com.crunchbase.app.datasets.CompanyTable;
import com.crunchbase.app.providers.CrunchBaseContentProvider;
import com.xtreme.rest.broadcasts.RestError;
import com.xtreme.rest.fragments.ContentLoaderAdapterSupportFragment;
import com.xtreme.rest.loader.ContentRequest;
import com.xtreme.rest.loader.ContentResponse;
import com.xtremelabs.imageutils.ImageLoader;

public class CompanyListFragment extends ContentLoaderAdapterSupportFragment implements OnItemClickListener {

	
	private static final String[] COLUMN_NAMES = new String[] { 
		CompanyTable.Columns.NAME,
		CompanyTable.Columns.OVERVIEW,
		CompanyTable.Columns.IMAGE_URL,
	};

	private static final int[] VIEW_IDS = new int[] { 
        R.id.list_item_company_name,
        R.id.list_item_company_overview,
        R.id.list_item_company_image,
	};
	
	private ImageLoader mImageLoader;
	
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_company_list, container, false);
		((ListView) view.findViewById(R.id.company_list)).setOnItemClickListener(this);
		return view;
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
		
		loadCompanies();
	}
	
	@Override
	public String[] getColumnNames() {
		return COLUMN_NAMES;
	}
	
	@Override
	public int[] getViewIds() {
		return VIEW_IDS;
	}
	
	@Override
	protected int getAdapterViewId() {
		return R.id.company_list;
	}
	
	@Override
	protected int getAdapterItemResourceId() {
		return R.layout.list_item_company;
	}
	
	private void loadCompanies() {
		final Uri contentUri = CrunchBaseContentProvider.Uris.COMPANIES_URI;
		final ContentRequest request = new ContentRequest(contentUri);
		request.setSortOrder(CompanyTable.Columns.NAME);
		execute(request);
	}
	
	@Override
	public void onContentChanged(final ContentResponse response) {
		final CursorAdapter adapter = getCursorAdapter();
		if (adapter.getCount() > 0) {
			showResults();
		} else {
			hideLoading();
		}
	}
	
	private void showResults() {
		getView().findViewById(R.id.company_list).setVisibility(View.VISIBLE);
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
	public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
		final Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
		final String itemId = cursor.getString(cursor.getColumnIndex(CompanyTable.Columns.NAME));
		CompanyActivity.newInstance(getActivity(), itemId);
	}

	@Override
	public boolean setViewValue(final View view, final Cursor cursor, final int columnIndex) {
		switch (view.getId()) {
		case R.id.list_item_company_image:
		    final String url = cursor.getString(columnIndex);
		    mImageLoader.loadImage((ImageView) view, url);
		    return true;

		default:
		    return super.setViewValue(view, cursor, columnIndex);
		}
	}
}
