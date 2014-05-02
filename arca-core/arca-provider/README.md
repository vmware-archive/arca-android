# Arca-Provider

The Arca Provider package builds off of the ContentProvider construct but delegates all the heavy lifting to a variety of different Dataset classes. Datasets are class representations for various storage types. This includes SQLiteTables, SQLiteViews or any custom implementation you create.

Each Dataset gets registered against a Uri and any requests into the provider for that Uri get forwarded along. SQLiteDatasets also handle lifecycle events from their backing SQLiteDatabase, such as **create**, **drop**, **upgrade** and **downgrade** events. The default implementation for upgrading and downgrading is to drop the table a re-create it, but this behaviour can be customized to fit your requirements.

## Usage

### DatabaseProvider

If you are planning on persisting data to a SQLiteDatabase you should extend the DatabaseProvider class. The DatabaseProvider will ensure that a SQLiteDatabase gets created and injected into any SQLiteDatasets before the **query**, **update**, **insert** or **delete** request gets called. Your implementation might look something like the following:

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

**Note:** *Don't forget to include your provider in your AndroidManifest.xml*

```xml
<provider
	android:name=".providers.MyAppContentProvider"
	android:authorities="com.mycompany.myapp.providers.MyAppContentProvider"
	android:exported="false" />
```

#### Configuration

You can override the ```onCreateDatabaseConfiguration()``` method in the DatabaseProvider class to define your own database name and version number. The default implementation of the DatabaseConfiguration grabs this information from your AndroidManifest.xml file.

### DatasetProvider

If you don't want to persist data in a database but still want to take full advantage of all the benefits of using Datasets, you can extend the DatasetProvider class directly. You can create Datasets that store data in a HashMap, to disk, or using any other mechanism you like. Registering these Datasets with the provider is the similar to the example shown above.

### Datasets

After the request has been filtered through the ContentProvider, an individual Dataset will handle the requested action. If you are persisting data to a SQLiteDatabase you will need to to create your own SQLiteTable subclass. Notice in the example below that we are extending the SQLiteTable.Columns interface and adding our own set of columns. This makes it easy for you to refer back to these column values when querying for data (see [Arca-Dispatcher](../../arca-app/arca-dispatcher)) or binding to views in your adapter (see [Arca-Adapters](../../arca-app/arca-adapters)).

```java
public class PostTable extends SQLiteTable {

	public static interface Columns extends SQLiteTable.Columns {
		@Unique(OnConflict.REPLACE)
		@Column(Column.Type.INTEGER)
		public static final String ID = "id";

		@Column(Column.Type.TEXT)
		public static final String TEXT = "text";

		@Column(Column.Type.INTEGER)
		public static final String USER_ID = "user_id";

		@Column(Column.Type.INTEGER)
		public static final String DATE = "date";
	}
}
```

After your Dataset has been setup its ready to handle requests. The default query method can handle applying a where clause, a projection and sort order. You can, however, override the query method to add whatever business logic you need. A typical customization is stripping the resource identifier off the end of the Uri and ammending the where arguments to return that single resource when required.

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

### ContentValues

There are convenience method that make it easy to convert a list of models into an array of ContentValues.

```java
final ContentValues values = DataUtils.getContentValues(Object object);
final ContentValues[] values = DataUtils.getContentValues(List<?> objects);
```

In order to take advantage of these methods you need to add the `@ColumnName` annotation to any field or method you want persisted. Annotating methods is a convenient way to transform and customize your data before persisting it to the datastore.

```java
public class Post {
	
	@ColumnName(ColumnNames.ID)
	private long mId;
	
	@ColumnName(ColumnNames.TEXT)
	private String mText;
	
	@ColumnName(ColumnNames.USER_ID)
	private long mUserId;

	@ColumnName(ColumnNames.DATE)
	public long getDate() {
		return System.currentTimeMillis();
	}
}
```

