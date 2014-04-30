# Arca-Fragments

The Arca Fragments package manages an Arca Dispatcher object which lets you easily execute a Query, Update, Insert or Delete request. It also allows you to register an Arca Monitor to observe callbacks before and after each request is executed.

The ArcaAdapterFragment has a convenience method that lets you execute Queries and automatically takes care of the the QueryResult by ripping out the cursor and swapping it in your adapter.

## Usage

The example below shows a simple example of a fragment that displays a list of Posts.

```java
public class PostListFragment extends ArcaAdapterFragment implements OnItemClickListener {

	private static final Collection<Binding> BINDINGS = Arrays.asList(new Binding[] { 
		new Binding(R.id.list_item_post_text, PostTable.Columns.TEXT.name),
		new Binding(R.id.list_item_post_created_at, PostTable.Columns.CREATED_AT.name),
	});
	
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_post_list, container, false);
		final AbsListView list = (AbsListView) view.findViewById(getAdapterViewId());
		list.setOnItemClickListener(this);
		return view;
	}
	
	@Override
	public CursorAdapter onCreateAdapter(final AdapterView<CursorAdapter> adapterView, final Bundle savedInstanceState) {
		final ModernCursorAdapter adapter = new ModernCursorAdapter(getActivity(), R.layout.list_item_post, BINDINGS);
		adapter.setViewBinder(new PostListViewBinder());
		return adapter;
	}
	
	@Override
	public ArcaDispatcher onCreateDispatcher(final Bundle savedInstaceState) {
		final ArcaDispatcher dispatcher = super.onCreateDispatcher(savedInstaceState);
		dispatcher.setRequestMonitor(new PostListMonitor());
		return dispatcher;
	}
	
	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		loadPosts();
	}
	
	private void loadPosts() {
		final Uri contentUri = MyAppContentProvider.Uris.POSTS;
		final Query request = new Query(contentUri);
		request.setSortOrder(PostTable.Columns.CREATED_AT.name);
		execute(request);
	}
	
	@Override
	public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
		final Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
		final int columnIndex = cursor.getColumnIndex(PostTable.Columns.ID.name);
		final String itemId = cursor.getString(columnIndex);
		PostActivity.newInstance(getActivity(), itemId);
	}
}
```

If you need full control over the state of your UI, you can also take advantage of the various callbacks for content state changes. This simple implementation allows you hide and show content when results are present:

```java
@Override
public void onContentChanged(final QueryResult result) {
	final CursorAdapter adapter = getCursorAdapter();
	if (adapter.getCount() > 0) {
		showResults();
	} else if (!result.isSyncing()) {
		showNoResults();
	}
}

@Override
public void onContentError(final Error error) {
	showNoResults();
	showError(error);
}

private View getLoadingView() {
	return getView().findViewById(R.id.loading);
}

private View getEmptyView() {
	return getView().findViewById(R.id.empty);
}

private void showLoading() {
	getAdapterView().setVisibility(View.INVISIBLE);
	getLoadingView().setVisibility(View.VISIBLE);
	getEmptyView().setVisibility(View.INVISIBLE);
}

private void showResults() {
	getAdapterView().setVisibility(View.VISIBLE);
	getLoadingView().setVisibility(View.INVISIBLE);
	getEmptyView().setVisibility(View.INVISIBLE);
}

private void showNoResults() {
	getAdapterView().setVisibility(View.INVISIBLE);
	getLoadingView().setVisibility(View.INVISIBLE);
	getEmptyView().setVisibility(View.VISIBLE);
}

private void showError(final Error error) {
	final String message = String.format("ERROR: %s", error.getMessage());
	Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
}
```
