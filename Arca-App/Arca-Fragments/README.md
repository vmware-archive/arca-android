# Arca-Fragments

The Arca Fragments package manages an Arca Dispatcher object which lets you easily execute a Query, Update, Insert or Delete request. It also allows you to register an Arca Monitor to observe callbacks before and after each request is executed.

The ArcaAdapterFragment has a convenience method that lets you execute Queries and automatically takes care of the the QueryResult by ripping out the cursor and swapping it in your adapter. It is recommend that you use the Arca Adapters package to take full adavantage of data bindings.

## Usage

The example below shows a simple example of a fragment that displays a list of Posts.

```java
public class PostListFragment extends ArcaAdapterFragment implements OnItemClickListener {

	private static final Collection<Binding> BINDINGS = Arrays.asList(new Binding[] { 
		new Binding(R.id.list_item_post_text, PostTable.Columns.TEXT.name),
		new Binding(R.id.list_item_post_created_at, PostTable.Columns.CREATED_AT.name),
	});

	@Override
	public int getAdapterViewId() {
		return R.id.post_list;
	}
	
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_post_list, container, false);
		((AbsListView) view.findViewById(R.id.post_list)).setOnItemClickListener(this);
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

If you need full control over the state of your UI, you can also take advantage of the various callbacks for content state changes.

```java
@Override
public void onContentChanged(final QueryResult result) {
	// Our adapter already has the new cursor
	final CursorAdapter adapter = getCursorAdapter();
	if (adapter.getCount() > 0) {
		showResults();
	}
}

@Override
public void onContentError(final Error error) {
	hideEverything();
	showError(error);
}

private void showError(final Error error) {
	Toast.makeText(getActivity(), "ERROR: " + error.getMessage(), Toast.LENGTH_SHORT).show();
}

private void hideEverything() {
	getView().findViewById(R.id.post_list).setVisibility(View.INVISIBLE);
	getView().findViewById(R.id.loading).setVisibility(View.INVISIBLE);
}

private void showResults() {
	getView().findViewById(R.id.post_list).setVisibility(View.VISIBLE);
	getView().findViewById(R.id.loading).setVisibility(View.INVISIBLE);
}

```
