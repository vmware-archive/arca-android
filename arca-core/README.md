# Arca-Core

The Arca Core package provides all the groundwork for caching data within a ContentProvider and interacting with a remote store using a Service (i.e. outside the context of your Fragments/Activities). This means your application can use less battery, less bandwidth and provide a much more responsive user experience.

## [Arca-Broadcaster](arca-broadcaster)
The Arca Broadcaster package adds the ability to send local broadcasts. This package is used by various other Arca packages in order to communicate errors and/or results.

## [Arca-Provider](arca-provider)
The Arca Provider package builds off of the ContentProvider construct but delegates all the heavy lifting to a variety of different Dataset classes. Datasets are class representations for various storage types. This includes SQLiteTables, SQLiteViews or any custom implementation you create.

## [Arca-Service](arca-service)
The Arca Service package lets you offload long running operations away from the lifecycle of your Activities and Fragments. It provides a Service implementation which accepts Operations, where each Operation is designed to encapsulate all the networking and storage required by an individual Dataset (see [Arca-Provider](arca-provider)). 

## [Arca-Threading](arca-threading)
The Arca Threading package provides a scheduler for background threads that allows for highly customizable, high-performance task scheduling. Developers can use this library to optimize tasks such as precaching, assigning/reassigning priorities, cancelling requests, and more.

## [Arca-Utils](arca-utils)
The Arca Utils package contains a set of useful helper classes used by a variety of other Arca packages. It also contains convenience classes that may be useful when implementing your own ContentProvider.

# Quick Start

## Setting up your Content Provider

Below we define our `ContentProvider`, which includes a definition of each `Dataset` that we are exposing to our application.

```java
public class MyAppContentProvider extends DatabaseProvider {

	private static final String AUTHORITY = "com.mycompany.myapp.providers.MyAppContentProvider";

	public static final class Uris {
		public static final Uri POSTS = Uri.parse("content://" + AUTHORITY + "/" + Paths.POSTS);
		public static final Uri USERS = Uri.parse("content://" + AUTHORITY + "/" + Paths.USERS);
		public static final Uri USER_POSTS = Uri.parse("content://" + AUTHORITY + "/" + Paths.USER_POSTS);
	}

	private static final class Paths {
		public static final String POSTS = "posts";
		public static final String USERS = "users";
		public static final String USER_POSTS = "user_posts";
	}

	@Override
	public boolean onCreate() {
		registerDataset(AUTHORITY, Paths.POSTS, PostTable.class);
		registerDataset(AUTHORITY, Paths.POSTS + "/*", PostTable.class);
		registerDataset(AUTHORITY, Paths.USERS, UserTable.class);
		registerDataset(AUTHORITY, Paths.USERS + "/*", UserTable.class);
		registerDataset(AUTHORITY, Paths.USER_POSTS, UserPostView.class);
		registerDataset(AUTHORITY, Paths.USER_POSTS + "/*", UserPostView.class);
		return true;
	}

	public static class PostTable extends SQLiteTable {

		public static interface Columns extends SQLiteTable.Columns {
			@Unique(Unique.OnConflict.REPLACE)
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

	public static class UserTable extends SQLiteTable {

		public static interface Columns extends SQLiteTable.Columns {
			@Unique(Unique.OnConflict.REPLACE)
			@Column(Column.Type.INTEGER)
			public static final String ID = "id";

			@Column(Column.Type.TEXT)
			public static final String NAME = "name";

			@Column(Column.Type.INTEGER)
			public static final String AGE = "age";
		}
	}

	public static final class UserPostView extends SQLiteView {

		@SelectFrom("PostTable as posts")

		@Joins({
			"LEFT JOIN UserTable as users ON posts.user_id = users.id"
		})

		@OrderBy("posts.date")

		public static interface Columns {
			@Select("posts.id")
			public static final String _ID = "_id";

			@Select("posts.text")
			public static final String TEXT = "text";

			@Select("posts.date")
			public static final String DATE = "date";

			@Select("users.id")
			public static final String USER_ID = "user_id";

			@Select("users.name")
			public static final String USER_NAME = "user_name";
		}
	}
}
```

## Populating your Content Provider

In order to populate our `ContentProvider` we first need to define a set of API calls that will correspond to each `Dataset`.

```java
public class MyAppApi {
	
	public static Post.ListResponse getPostListResponse() throws Exception {
		final HttpGet request = new HttpGet("http://10.0.2.2:3000/posts.json");
		return execute(request, new Gson(), Post.ListResponse.class);
	}

	public static User.ListResponse getUserListResponse() throws Exception {
		final HttpGet request = new HttpGet("http://10.0.2.2:3000/users.json");
		return execute(request, new Gson(), User.ListResponse.class);
	}

	private static <T> T execute(final HttpUriRequest request, final Gson gson, final Type type) throws Exception {
		final HttpResponse response = new DefaultHttpClient().execute(request);
		final InputStream inputStream = response.getEntity().getContent();
		final InputStreamReader inputReader = new InputStreamReader(inputStream);
		final JsonReader jsonReader = new JsonReader(inputReader);
		final T list = gson.fromJson(jsonReader, type);
		jsonReader.close();
		return list;
	}
}
```

Since we will be using `Gson` to fetch our data we also need to define our annotated models.

```java
public static class Post {

	public static class ListResponse extends ArrayList<Post> {
		private static final long serialVersionUID = 1L;
	}

	@ColumnName(PostTable.Columns.ID)
	@SerializedName("id")
	private long mId;

	@ColumnName(PostTable.Columns.TEXT)
	@SerializedName("text")
	private String mText;

	@ColumnName(PostTable.Columns.USER_ID)
	@SerializedName("user_id")
	private long mUserId;

	@ColumnName(PostTable.Columns.DATE)
	@SerializedName("date")
	private long mDate;
}
```

```java
public static class User {

	public static class ListResponse extends ArrayList<User> {
		private static final long serialVersionUID = 1L;
	}

	@ColumnName(UserTable.Columns.ID)
	@SerializedName("id")
	private long mId;

	@SerializedName("name")
	private String mName;

	@ColumnName(UserTable.Columns.NAME)
	public String getName() {
		return mName != null ? mName : "Anonymous";
	}

	@ColumnName(UserTable.Columns.AGE)
	@SerializedName("age")
	private int mAge;
}
```

Now we can fetch data and insert it into our `ContentProvider`.

```java
public class MyAppProviderUtility {

	// Synchronously
	public static void populatePostTable(final Context context) throws Exception {
		final Post.ListResponse response = MyAppApi.getPostListResponse();
		final ContentValues[] values = DataUtils.getContentValues(response);

		final ContentResolver resolver = context.getContentResolver();
		resolver.bulkInsert(MyAppContentProvider.Uris.POSTS, values);
	}

	public static void populateUserTable(final Context context) throws Exception {
		final User.ListResponse response = MyAppApi.getUserListResponse();
		final ContentValues[] values = DataUtils.getContentValues(response);

		final ContentResolver resolver = context.getContentResolver();
		resolver.bulkInsert(MyAppContentProvider.Uris.USERS, values);
	}

	// Asynchronously
	public static void populatePostTableAsync(final Context context) {
		final Operation operation = new GetPostListOperation();

		OperationService.start(context, operation);
	}

	public static void populateUserTableAsync(final Context context) {
		final Operation operation = new GetUserListOperation();

		OperationService.start(context, operation);
	}
}
```

If we are doing things asynchronously each `Operation` is defined as follows.

```java
public class GetPostListOperation extends SimpleOperation {

	public GetPostListOperation() {
		super(MyAppContentProvider.Uris.POSTS);
	}

	public GetPostListOperation(final Parcel in) {
		super(in);
	}

	@Override
	public ContentValues[] onExecute(final Context context) throws Exception {
		final Post.ListResponse response = MyAppApi.getPostListResponse();
		return DataUtils.getContentValues(response);
	}

	public static final Parcelable.Creator<GetPostListOperation> CREATOR = new Parcelable.Creator<GetPostListOperation>() {
		@Override
		public GetPostListOperation createFromParcel(final Parcel in) {
			return new GetPostListOperation(in);
		}

		@Override
		public GetPostListOperation[] newArray(final int size) {
			return new GetPostListOperation[size];
		}
	};
}
```

```java
public class GetUserListOperation extends SimpleOperation {

	public GetUserListOperation() {
		super(MyAppContentProvider.Uris.USERS);
	}

	public GetUserListOperation(final Parcel in) {
		super(in);
	}

	@Override
	public ContentValues[] onExecute(final Context context) throws Exception {
		final User.ListResponse response = MyAppApi.getUserListResponse();
		return DataUtils.getContentValues(response);
	}

	public static final Parcelable.Creator<GetUserListOperation> CREATOR = new Parcelable.Creator<GetUserListOperation>() {
		@Override
		public GetUserListOperation createFromParcel(final Parcel in) {
			return new GetUserListOperation(in);
		}

		@Override
		public GetUserListOperation[] newArray(final int size) {
			return new GetUserListOperation[size];
		}
	};
}
```
