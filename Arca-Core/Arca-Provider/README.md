# Arca-Provider

The Arca Provider package builds off of the ContentProvider construct but delegates all the heavy lifting to a variety of different Dataset classes. Datasets are class representations for various storage types. This includes SQLiteTables, SQLiteViews or any custom implementation you create.

Each Dataset gets registered against a Uri and any requests into the provider for that Uri get forwarded along. SQLiteDatasets also handle lifecycle events from their backing SQLiteDatabase, such as **create**, **drop**, **upgrade** and **downgrade** events. The default implementation for upgrading and downgrading is to drop the table a re-create it, but this behaviour can be customized to fit your requirements.

## Usage

### DatabaseProvider

If you are planning on persisting data to a SQLiteDatabase you should extend the DatabaseProvider class. The DatabaseProvider will ensure that a SQLiteDatabase gets injected into any SQLiteDatasets before the **query**, **update**, **insert** or **delete** request gets forwarded along.

Your implementation might look something like the following:

```java
public class MyAppContentProvider extends DatabaseProvider {

    private static final String AUTHORITY = "com.mycompany.myapp.providers.MyAppContentProvider";
    
    public static final class Uris {
		public static final Uri POSTS = Uri.parse("content://" + AUTHORITY + "/" + Paths.POSTS);
		public static final Uri USERS = Uri.parse("content://" + AUTHORITY + "/" + Paths.USERS);
	}
	
	private static final class Paths {
		public static final String POSTS = "posts";
		public static final String USERS = "users";
	}

	@Override
	public boolean onCreate() {
		registerDataset(AUTHORITY, Paths.POSTS, PostTable.class);
		registerDataset(AUTHORITY, Paths.POSTS + "/*", PostTable.class);
		registerDataset(AUTHORITY, Paths.USERS, UserTable.class);
		registerDataset(AUTHORITY, Paths.USERS + "/*", UserTable.class);
		return true;
	}
}
```
### DatasetProvider

If you don't want to persist data in a database but still want to take full advantage of all the benefits of using Datasets, you can extend the DatasetProvider class directly. You can create Datasets that store data in a HashMap, to disk, or using any other mechanism you like. Registering these Datasets with the provider is the similar to the example shown above.

### Datasets

After the request has been filtered through the ContentProvider, an individual Dataset will handle the requested action. Lets see what a typical Dataset implementation might look like.

If you are persisting data to a SQLiteDatabase you will need to to create your own SQLiteTable subclass. Notice in the example below that we are extending the SQLiteTable.Columns interface and adding our own set of columns. This makes it easy for you to refer back to these column values when querying for data (see [Arca-Dispatcher](../../Arca-App/Arca-Dispatcher)) or binding to views in your adapter (see [Arca-Adapters](../../Arca-App/Arca-Adapters)).

```java
public class PostTable extends SQLiteTable {
    
	public static interface Columns extends SQLiteTable.Columns {
        public static final Column ID = Column.Type.TEXT.newColumn("id");
        public static final Column TEXT = Column.Type.TEXT.newColumn("text");
        public static final Column CREATED_AT = Column.Type.TEXT.newColumn("created_at");
	}
	
	@Override
	public void onCreate(final SQLiteDatabase db) {
		final String columns = ColumnUtils.toString(Columns.class);
		final String constraint = "UNIQUE (" + Columns.ID + ") ON CONFLICT REPLACE";
		db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (%s, %s);", getName(), columns, constraint));
	}
	
	@Override
	public void onDrop(final SQLiteDatabase db) {
		db.execSQL(String.format("DROP TABLE IF EXISTS %s;", getName()));
	}
}
```

After your Dataset has been setup its ready to handle requests. The default implementation in the SQLiteDataset for handling queries looks something like the following:

```java
@Override
public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
    final SQLiteDatabase database = getDatabase();
	if (database != null) {
		return database.query(getName(), projection, selection, selectionArgs, null, null, sortOrder);
	} else {
		throw new IllegalStateException("Database is null.");
	}
}
```

You can override the query method to add whatever business logic you need. A typical customization is stripping the resource identifier off the end of the Uri and returning that single resource when required.

```java
@Override
public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs, final String sortOrder) {
	if (uri.getPathSegments().size() > 1) { 
		final String selectionWithId = StringUtils.append(selection, Columns.ID + "=?", " AND ");
		final String[] selectionArgsWithId = ArrayUtils.append(selectionArgs, new String[] { uri.getLastPathSegment() });
		return super.query(uri, projection, selectionWithId, selectionArgsWithId, sortOrder);
	} else {
		return super.query(uri, projection, selection, selectionArgs, sortOrder);
	}
}
```

Lastly its convenient to add methods that can convert between a model and a ContentValues object which can easily be inserted into the database.

```java
public static ContentValues[] getContentValues(final List<Post> list) {
	final ContentValues[] values = new ContentValues[list.size()];
	for (int i = 0; i < values.length; i++) {
		values[i] = getContentValues(list.get(i));
	}
	return values;
}

public static ContentValues getContentValues(final Post item) {
	final ContentValues value = new ContentValues();
    value.put(Columns.ID.name, item.getId());
    value.put(Columns.TEXT.name, item.getText());
    value.put(Columns.CREATED_AT.name, item.getCreatedAt());
    return value;
}
```