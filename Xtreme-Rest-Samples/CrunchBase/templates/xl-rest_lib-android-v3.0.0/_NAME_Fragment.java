package ${base_package}.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ${base_package}.R;
import ${base_package}.datasets.${name}Table;
import ${base_package}.providers.${project_name}ContentProvider;

import com.xtreme.rest.adapters.ItemCursorAdapter;
import com.xtreme.rest.fragments.ContentLoaderItemSupportFragment;
import com.xtreme.rest.loader.ContentRequest;
import com.xtreme.rest.loader.ContentResponse;
import com.xtreme.rest.broadcasts.RestError;

public class ${name}Fragment extends ContentLoaderItemSupportFragment {
	
	private static final String[] COLUMN_NAMES = new String[] { 
		%for prop in properties: 
			%if common.isSqliteType(prop, sqlite_types):
        ${name}Table.Columns.${common.underscoreUppercase(prop['key'])},
			%endif
		%endfor
	};

	private static final int[] VIEW_IDS = new int[] { 
		%for prop in properties: 
			%if common.isSqliteType(prop, sqlite_types):
        R.id.${common.underscoreCombine(name, prop['key'])},
			%endif
		%endfor
	};

	private String mId;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_${name.lower()}, container, false);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		load${name}(mId);
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

	private void load${name}(final String id) {
		final Uri baseUri = ${project_name}ContentProvider.Uris.${common.pluralUnderscoreUppercase(name)}_URI;
		final Uri contentUri = Uri.withAppendedPath(baseUri, id);
		execute(new ContentRequest(contentUri));
	}
	
	@Override
	public void onError(final RestError error) {
		showError(error.getMessage());
		hideLoading();
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
		getView().findViewById(R.id.${common.underscore(name)}_container).setVisibility(View.VISIBLE);
		getView().findViewById(R.id.loading).setVisibility(View.INVISIBLE);
	}
	
	private void hideLoading() {
		getView().findViewById(R.id.loading).setVisibility(View.INVISIBLE);
	}
	
	private void showError(final String message) {
		Toast.makeText(getActivity(), "ERROR: " + message, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public boolean setViewValue(final View view, final Cursor cursor, final int columnIndex) {
		return false;
	}
	
}
