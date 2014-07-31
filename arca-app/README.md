# Arca-App

The Arca App package makes it really easy to fetch data from a ContentProvider and bind it to elements in your layout. This means your UI will always be up to date with the most recent data without having to write a single additional line of code.

## [Arca-Adapters](arca-adapters)
Within the Android framework, adapters act as a bridge between your data and your UI. The Arca Adapters package builds off the standard CursorAdapter implementation adding a number of improvements.

## [Arca-Dispatcher](arca-dispatcher)
The Arca Dispatcher package makes it really easy for you to request data synchronously or asynchronously from a ContentProvider. It accepts Query, Update, Insert and Delete requests and returns a corresponding QueryResult, UpdateResult, InsertResult or DeleteResult.

## [Arca-Fragments](arca-fragments)
The Arca Fragments package manages an Arca Dispatcher object which lets you easily execute a Query, Update, Insert or Delete request. It also allows you to register an Arca Monitor to observe callbacks before and after each request is executed.

## [Arca-Monitor](arca-monitor)
The Arca Monitor package adds a layer on top of the Arca Dispatcher that allows you to monitor every request that gets executed in the system. This allows you to verify every Query, Updated, Insert or Delete result and resolve differences with a remote store when appropriate.

# Quick Start

## Fetching data from your Content Provider

You can easily query data from a `ContentProvider` within your activity or fragment. In the asynchronous case all queries are made using a `CursorLoader` and the result is returned to you via a listener.

```java
public class UserListActivity extends Activity {

	private RequestDispatcher mDispatcher;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);

		mDispatcher = ArcaDispatcherFactory.generateDispatcher(this);
	}

	public void fetchUsers() {
		final Query request = new Query(MyAppContentProvider.Uris.USERS);
		final QueryResult result = mDispatcher.execute(request);

		final Cursor cursor = result.getResult();

		// do something with cursor

		cursor.close();
	}

	public void fetchUsersAsync() {
		final Query request = new Query(MyAppContentProvider.Uris.USERS);

		mDispatcher.execute(request, new QueryListener() {

			@Override
			public void onRequestComplete(final QueryResult result) {
				// closing cursor is not necessary
			}

			@Override
			public void onRequestReset() {

			}
		});
	}
}
```

If you have an adapter view in your layout you can use the `ArcaAdapterFragment` in conjunction with the `CursorAdapters` that are included in the framework and your layout will be automatically populated for you.

```java
public class UserListFragment extends ArcaAdapterFragment {

	private static final Collection<Binding> BINDINGS = Arrays.asList(new Binding[] {
		new Binding(R.id.user_id, UserTable.Columns.ID),
		new Binding(R.id.user_name, UserTable.Columns.NAME),
		new Binding(R.id.user_age, UserTable.Columns.AGE),
	});

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_user_list, container, false);
	}

	@Override
	public CursorAdapter onCreateAdapter(final AdapterView<CursorAdapter> adapterView, final Bundle savedInstanceState) {
		return new ModernCursorAdapter(getActivity(), R.layout.list_item_user, BINDINGS);
	}

	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		final Uri uri = MyAppContentProvider.Uris.USERS;
		final Query request = new Query(uri);
		execute(request);
	}
}
```