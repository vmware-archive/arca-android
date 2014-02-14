package com.crunchbase.app.fragments;

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
import android.widget.ListView;
import android.widget.Toast;

import com.crunchbase.app.R;
import com.crunchbase.app.activities.CompanyActivity;
import com.crunchbase.app.datasets.CompanyTable;
import com.crunchbase.app.providers.CrunchBaseContentProvider;
import com.crunchbase.app.validators.CompanyListValidator;
import com.xtreme.rest.RestDispatcher;
import com.xtreme.rest.adapters.SupportCursorAdapter;
import com.xtreme.rest.adapters.Binding;
import com.xtreme.rest.adapters.ViewBinder;
import com.xtreme.rest.dispatcher.Error;
import com.xtreme.rest.dispatcher.Query;
import com.xtreme.rest.dispatcher.QueryResult;
import com.xtreme.rest.fragments.RestAdapterSupportFragment;
import com.xtremelabs.imageutils.ImageLoader;

public class CompanyListFragment extends RestAdapterSupportFragment implements OnItemClickListener, ViewBinder {

	private static final Collection<Binding> BINDINGS = Arrays.asList(new Binding[] { 
		new Binding(R.id.list_item_company_name, CompanyTable.Columns.NAME.name),
		new Binding(R.id.list_item_company_overview, CompanyTable.Columns.OVERVIEW.name),
		new Binding(R.id.list_item_company_image, CompanyTable.Columns.IMAGE_URL.name),
	});

	private ImageLoader mImageLoader;
	
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_company_list, container, false);
		((ListView) view.findViewById(R.id.company_list)).setOnItemClickListener(this);
		return view;
	}
	
	@Override
	public CursorAdapter onCreateAdapter(final AdapterView<CursorAdapter> adapterView, final Bundle savedInstaceState) {
		final SupportCursorAdapter adapter = new SupportCursorAdapter(getActivity(), R.layout.list_item_company, BINDINGS);
		adapter.setViewBinder(this);
		return adapter;
	}

	@Override
	protected RestDispatcher onCreateRequestDispatcher() {
		final RestDispatcher dispatcher = super.onCreateRequestDispatcher();
		dispatcher.setValidator(new CompanyListValidator());
		return dispatcher;
	}
	
	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
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
	protected int getAdapterViewId() {
		return R.id.company_list;
	}
	
	private void loadCompanies() {
		final Uri contentUri = CrunchBaseContentProvider.Uris.COMPANIES_URI;
		final Query request = new Query(contentUri);
		request.setSortOrder(CompanyTable.Columns.NAME.name);
		
		execute(request);
		showLoading();
	}
	
	@Override
	public void onContentError(final Error error) {
		Toast.makeText(getActivity(), "ERROR: " + error.getMessage(), Toast.LENGTH_SHORT).show();
		hideLoading();
	}
	
	@Override
	public void onContentChanged(final QueryResult result) {
		final CursorAdapter adapter = getCursorAdapter();
		if (adapter.getCount() > 0) {
			showResults();
		} else if (!result.isRefreshing()) {
			hideLoading();
		}
	}
	
	private void showResults() {
		getView().findViewById(R.id.company_list).setVisibility(View.VISIBLE);
		getView().findViewById(R.id.loading).setVisibility(View.INVISIBLE);
	}
	
	private void showLoading() {
		getView().findViewById(R.id.company_list).setVisibility(View.INVISIBLE);
		getView().findViewById(R.id.loading).setVisibility(View.VISIBLE);
	}
	
	private void hideLoading() {
		getView().findViewById(R.id.loading).setVisibility(View.INVISIBLE);
	}
	
	@Override
	public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
		final Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
		final String itemId = cursor.getString(cursor.getColumnIndex(CompanyTable.Columns.NAME.name));
		CompanyActivity.newInstance(getActivity(), itemId);
	}

	@Override
	public boolean setViewValue(final View view, final Cursor cursor, final Binding binding) {
		switch (view.getId()) {
		case R.id.list_item_company_image:
		    final String url = cursor.getString(binding.getColumnIndex());
		    mImageLoader.loadImage((ImageView) view, url);
		    return true;

		default:
		    return false;
		}
	}
}
