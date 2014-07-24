# Arca-Fragments

The Arca Fragments package manages an Arca Dispatcher object which lets you easily execute a Query, Update, Insert or Delete request. It also allows you to register an Arca Monitor to observe callbacks before and after each request is executed.

The ArcaAdapterFragment has a convenience method that lets you execute Queries and automatically takes care of the the QueryResult by ripping out the cursor and swapping it in your adapter.

## Usage

The example below shows a simple example of a fragment that displays a list of Posts.

```java
public class PostListFragment extends ArcaAdapterFragment implements OnItemClickListener {

	private static final Collection<Binding> BINDINGS = Arrays.asList(new Binding[] { 
		new Binding(R.id.list_item_post_text, PostTable.Columns.TEXT),
		new Binding(R.id.list_item_post_created_at, PostTable.Columns.CREATED_AT),
	});

	private ArcaViewManager mViewManager;

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

		mViewManager = new ArcaViewManager(view);
		mViewManager.showProgressView();

		loadPosts();
	}

	@Override
	public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
		final Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
		final int columnIndex = cursor.getColumnIndex(PostTable.Columns.ID);
		final String itemId = cursor.getString(columnIndex);
		PostActivity.newInstance(getActivity(), itemId);
	}

	private void loadPosts() {
		final Uri contentUri = MyAppContentProvider.Uris.POSTS;
		final Query request = new Query(contentUri);
		request.setSortOrder(PostTable.Columns.CREATED_AT);
		execute(request);
	}

	@Override
	public void onContentChanged(final QueryResult result) {
		mViewManager.checkResult(result);
	}

	@Override
	public void onContentError(final Error error) {
		mViewManager.checkError(error);
	}
}
```

## ViewManager

By default the `ArcaViewManager` uses the built in `android.R` values to find at manage your view state for you. That means if you have three views with ids `android.R.id.list`, `android.R.id.empty`, and `android.R.id.progress` everything should work out of the box. If you need to customize the behaviour you can use `setContentId()`, `setEmptyId()`, and `setProgressId()` to override which id it looks for in your hierarchy.
