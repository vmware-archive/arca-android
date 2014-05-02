# Arca-Adapters

Within the Android framework, adapters act as a bridge between your data and your UI. The Arca Adapters package builds off the standard CursorAdapter implementation adding a number of improvements.

- It adds default support for the view holder pattern, which optimizes view access.
- It makes the ViewBinder construct much more intuitive through the use of Bindings.
- It brings adapters and data bindings to static (non adapter-based) views.
- It makes it very easy to add support for multiple view types.

## Usage

### Simple Example

In the simple case, you do not need to create your own adapter. You can use one of the standard adapters available in this package. All you need to do is define a collection of bindings and pass it into the adapter's constructor.

```java
private static final Collection<Binding> BINDINGS = Arrays.asList(new Binding[] { 
    new Binding(R.id.list_item_custom_title, PostTable.Columns.TITLE),
	new Binding(R.id.list_item_custom_date, PostTable.Columns.DATE),
});

private CursorAdapter createAdapterForAdapterView() {
	return new ModernCursorAdapter(context, R.layout.list_item_default, BINDINGS);
}

private CursorAdapter createSupportAdapterForAdapterView() {
	return new SupportCursorAdapter(context, R.layout.list_item_default, BINDINGS);
}

private CursorAdapter createAdapterForStaticView() {
	return new ModernItemAdapter(context, BINDINGS);
}

private CursorAdapter createSupportAdapterForStaticView() {
	return new SupportItemAdapter(context, BINDINGS);
}
```

### View Binders

View binding is the process by which data from a column in your cursor is bound to a view in your layout. 

Each adapter in this package contains a default ViewBinder. The default ViewBinder is able to extract a String from a Cursor and set it as the text to a TextView. If you want more advanced control over how the data from your cursor gets bound to its view, you can create your own custom ViewBinder class.

```java
public class MyViewBinder implements ViewBinder {

	private final DateFormat mFormatter = DateFormat.getDateInstance();

	@Override
	public boolean setViewValue(final View view, final Cursor cursor, final Binding binding) {
		switch (view.getId()) {
		case R.id.list_item_default_title:
			return setDefaultTitle((TextView) view, cursor, binding);

		case R.id.list_item_default_date:
			return setDefaultDate((TextView) view, cursor, binding);

		default:
			return false;
		}
	}
	
	private boolean setDefaultTitle(final TextView textview, final Cursor cursor, final Binding binding) {
		final int columnIndex = binding.getColumnIndex();
		final String text = cursor.getString(columnIndex);
		textview.setText(String.format("MY TITLE : %s", text));
		return true;
	}

	private boolean setDefaultDate(final TextView textview, final Cursor cursor, final Binding binding) {
		final int columnIndex = binding.getColumnIndex();
		final int timestamp = cursor.getInt(columnIndex);
		final Date date = new Date(timestamp);
		textview.setText(mFormatter.format(date));
		return true;
	}
}
```

By returning **true** from the `setViewValue()` method your are telling the adapter that you have successfully bound the necessary data. Otherwise, returning **false** means you are delegating control of the binding to the default ViewBinder. 

**Note:** *An exception will be thrown if the default ViewBinder cannot bind the given view.*

### Multiple View Types

In more advanced cases you can very cleanly create a custom adapter which supports multiple view types. You should be mindful of the way you store your resource type in order to make it easier on your adapter.

```java
public class MyTypedCursorAdapter extends ModernCursorAdapter {

	public static enum ViewTypes {
		DEFAULT(R.layout.list_item_default),
		CUSTOM(R.layout.list_item_custom);

		private int mLayout;

		ViewTypes(final int layout) {
			mLayout = layout;
		}

		public int getLayout() {
			return mLayout;
		}

		public Binding newBinding(final int viewId, final String column) {
			return new Binding(ordinal(), viewId, column);
		}
	}

	public MyTypedCursorAdapter(final Context context, final Collection<Binding> bindings) {
		super(context, 0, bindings);
	}

	@Override
	public int getViewTypeCount() {
		return ViewTypes.values().length;
	}

	@Override
	public int getItemViewType(final int position) {
		final Cursor cursor = (Cursor) getItem(position);
		final int index = cursor.getColumnIndex(PostTable.Columns.TYPE);
		final String typeString = cursor.getString(index);
		return ViewTypes.valueOf(typeString).ordinal();
	}

	@Override
	public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
		final int index = cursor.getColumnIndex(PostTable.Columns.TYPE);
		final String typeString = cursor.getString(index);
		final ViewTypes viewType = ViewTypes.valueOf(typeString);
		final LayoutInflater inflater = LayoutInflater.from(context);
		return inflater.inflate(viewType.getLayout(), parent, false);
	}
}
```

When implementing your adapter you create a list of Bindings as follows:

```java
private static final Collection<Binding> BINDINGS = Arrays.asList(new Binding[] { 
	ViewTypes.DEFAULT.newBinding(R.id.list_item_default_title, PostTable.Columns.TITLE),
	ViewTypes.DEFAULT.newBinding(R.id.list_item_default_date, PostTable.Columns.DATE),
	ViewTypes.CUSTOM.newBinding(R.id.list_item_custom_title, PostTable.Columns.TITLE),
	ViewTypes.CUSTOM.newBinding(R.id.list_item_custom_date, PostTable.Columns.DATE),
});
```
