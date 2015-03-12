# Arca-Fragments

The Arca Fragments package manages an `ArcaDispatcher` object which lets you easily execute a `Query`, `Update`, `Insert` or `Delete` request. It also allows you to register an `ArcaMonitor` to observe callbacks before and after each request is executed.

The `ArcaAdapterFragment` has a convenience method that lets you execute a `Query` and automatically takes care of the the `QueryResult` by ripping out the cursor and swapping it in your adapter.

The `ArcaSimpleAdapterFragment` reduces the amount of boiler plate code required by allowing you to specify all your layouts and components in an `@ArcaFragment` annotation.

## Usage

The easiest way to get started with Arca Fragments is with the `@ArcaFragment` annotation. The example below demonstrates how to display a list of Posts.

```java
@ArcaFragment(
    fragmentLayout = R.layout.fragment_post_list,
    adapterItemLayout = R.layout.list_item_post,
    monitor = PostListMonitor.class,
    binder = PostListViewBinder.class
)
public class PostListFragment extends ArcaSimpleAdapterFragment {

    @ArcaFragmentBindings
	private static final Collection<Binding> BINDINGS = Arrays.asList(
		new Binding(R.id.list_item_post_text, PostTable.Columns.TEXT),
		new Binding(R.id.list_item_post_created_at, PostTable.Columns.CREATED_AT)
	);

	@Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadPosts();
    }

    private void loadPosts() {
        final Uri contentUri = MyAppContentProvider.Uris.POSTS;
        final Query request = new Query(contentUri);
        request.setSortOrder(PostTable.Columns.CREATED_AT);
        execute(request);
    }
}
```


Instead of using the annotations above, you can setup your fragment manually:


```java
public class PostListFragment extends ArcaAdapterFragment implements OnItemClickListener {

	private static final Collection<Binding> BINDINGS = Arrays.asList(
		new Binding(R.id.list_item_post_text, PostTable.Columns.TEXT),
		new Binding(R.id.list_item_post_created_at, PostTable.Columns.CREATED_AT)
	);

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
	public ArcaDispatcher onCreateDispatcher(final Bundle savedInstanceState) {
		final ArcaDispatcher dispatcher = super.onCreateDispatcher(savedInstanceState);
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

By default the `ArcaViewManager` uses the built in `android.R.id` values to find at manage 3 different view states in your fragment.

#### Progress

The view manager looks for a view with id `android.R.id.progress` to display when content is loading.

#### Content

The view manager looks for a view with id `android.R.id.content` or `android.R.id.list` to display when the cursor has content to display.

#### Empty

The view manager looks for a view with id `android.R.id.empty` to display when the cursor has no content to display.

### Customization

You can use `setContentId(final int id)`, `setEmptyId(final int id)`, and `setProgressId(final int id)` to override which id it looks for in your hierarchy.
