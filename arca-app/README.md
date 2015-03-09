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

The easiest way to take advantage of all the features in the Arca App package is by creating your own subclass of `ArcaSimpleAdapterFragment` or `ArcaSimpleItemFragment` with an `@ArcaFragment` annotation. These fragment subclasses will create an `ArcaDispatcher` and use it to fetch data asynchronously from your `ContentProvider` every time a `Query` is executed. These fragments will also handle the resulting cursor and properly manage the view state of your fragment.

```java
@ArcaFragment(
    fragmentLayout = R.layout.fragment_user_list,
    adapterItemLayout = R.layout.list_item_user,
    monitor = UserListRequestMonitor.class,
    binder = UserListViewBinder.class
)
public class UserListFragment extends ArcaSimpleAdapterFragment {

    @ArcaFragmentBindings
    private static final Collection<Binding> BINDINGS = Arrays.asList(new Binding[] {
        new Binding(R.id.user_id, UserTable.Columns.ID),
        new Binding(R.id.user_name, UserTable.Columns.NAME),
        new Binding(R.id.user_age, UserTable.Columns.AGE)
    });

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Query request = new Query(MyAppContentProvider.Uris.USERS);
        request.setSortOrder(UserTable.Columns.NAME);

        execute(request);
    }
}
```


```java
@ArcaFragment(
    fragmentLayout = R.layout.fragment_user_item,
    monitor = UserItemRequestMonitor.class,
    binder = UserItemViewBinder.class
)
public class UserItemFragment extends ArcaSimpleItemFragment {

    @ArcaFragmentBindings
    private static final Collection<Binding> BINDINGS = Arrays.asList(
        new Binding(R.id.user_id, UserTable.Columns.ID),
        new Binding(R.id.user_name, UserTable.Columns.NAME),
        new Binding(R.id.user_age, UserTable.Columns.AGE)
    );

    private String mIdentifier;

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final String whereClause = UserTable.Columns.ID + "=?";
        final String[] whereArgs = new String[] { mIdentifier };

        final Query request = new Query(MyAppContentProvider.Uris.USERS);
        request.setWhere(whereClause, whereArgs);

        execute(request);
    }

    public void setIdentifier(final String identifier) {
        mIdentifier = identifier;
    }
}
```