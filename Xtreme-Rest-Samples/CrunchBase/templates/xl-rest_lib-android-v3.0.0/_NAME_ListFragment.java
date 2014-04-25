package ${base_package}.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import ${base_package}.R;
import ${base_package}.datasets.${name}Table;
import ${base_package}.providers.${project_name}ContentProvider;
import ${base_package}.activities.${name}Activity;

import com.xtreme.rest.fragments.ContentLoaderAdapterSupportFragment;
import com.xtreme.rest.loader.ContentRequest;
import com.xtreme.rest.loader.ContentResponse;
import com.xtreme.rest.broadcasts.RestError;

public class ${name}ListFragment extends ContentLoaderAdapterSupportFragment implements OnItemClickListener {

	
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
        R.id.list_item_${common.underscoreCombine(name, prop['key'])},
			%endif 
		%endfor
	};
	
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_${name.lower()}_list, container, false);
		((AbsListView) view.findViewById(R.id.${common.underscore(name)}_list)).setOnItemClickListener(this);
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		load${common.pluralCapitalized(name)}();
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
		return R.id.${common.underscore(name)}_list;
	}
	
	@Override
	protected int getAdapterItemResourceId() {
		return R.layout.list_item_${name.lower()};
	}
	
	private void load${common.pluralCapitalized(name)}() {
		final Uri contentUri = ${project_name}ContentProvider.Uris.${common.pluralUnderscoreUppercase(name)}_URI;
		execute(new ContentRequest(contentUri));
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
		} else {
			hideLoading();
		}
	}
	
	private void showResults() {
		getView().findViewById(R.id.${common.underscore(name)}_list).setVisibility(View.VISIBLE);
		getView().findViewById(R.id.loading).setVisibility(View.INVISIBLE);
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
		final String itemId = cursor.getString(cursor.getColumnIndex(${name}Table.Columns.${common.idProp(properties)}));
		${name}Activity.newInstance(getActivity(), itemId);
	}

	@Override
	public boolean setViewValue(final View view, final Cursor cursor, final int columnIndex) {
		return false;
	}
}
