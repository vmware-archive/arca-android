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

	private static final String AUTHORITY = MyAppContentProvider.class.getName();
	private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

	public static final class Uris {
        public static final Uri POSTS = Uri.withAppendedPath(BASE_URI, Paths.POSTS);
        public static final Uri USERS = Uri.withAppendedPath(BASE_URI, Paths.USERS);
		public static final Uri USER_POSTS = Uri.withAppendedPath(BASE_URI, Paths.USER_POSTS);
	}

	private static final class Paths {
		public static final String POSTS = "posts";
		public static final String USERS = "users";
		public static final String USER_POSTS = "user_posts";
	}

	@Override
	public boolean onCreate() {
		registerDataset(AUTHORITY, Paths.POSTS, PostTable.class);
		registerDataset(AUTHORITY, Paths.USERS, UserTable.class);
		registerDataset(AUTHORITY, Paths.USER_POSTS, UserPostView.class);
		return true;
	}

	public static class PostTable extends SQLiteTable {

		public interface Columns extends SQLiteTable.Columns {
			@Unique(Unique.OnConflict.REPLACE)
			@Column(Column.Type.INTEGER) String ID = "id";
			@Column(Column.Type.TEXT) String TEXT = "text";
			@Column(Column.Type.INTEGER) String USER_ID = "user_id";
			@Column(Column.Type.INTEGER) String DATE = "date";
		}
	}

	public static class UserTable extends SQLiteTable {

		public interface Columns extends SQLiteTable.Columns {
			@Unique(Unique.OnConflict.REPLACE)
			@Column(Column.Type.INTEGER) String ID = "id";
			@Column(Column.Type.TEXT) String NAME = "name";
			@Column(Column.Type.INTEGER) String AGE = "age";
		}
	}

	public static final class UserPostView extends SQLiteView {

		@SelectFrom("PostTable as posts")

		@Joins({
			"LEFT JOIN UserTable as users ON posts.user_id = users.id"
		})

		@OrderBy("posts.date")

		public interface Columns {
			@Select("posts.id") String _ID = "_id";
			@Select("posts.text") String TEXT = "text";
			@Select("posts.date") String DATE = "date";
			@Select("users.id") String USER_ID = "user_id";
			@Select("users.name") String USER_NAME = "user_name";
		}
	}
}
```

## Populating your Content Provider

Now in order to populate our `ContentProvider` we can use the `OperationService` to make asynchronous requests to our server. To do this we invoke the `OperationService.start(context, operation)` method by passing it a new instance of an `Operation`. In this case we define an `Operation` for Users and Posts as shown below. The main advantage to delegating this responsibility to a `Service` is that the lifecycle of the network request is not tied to an `Fragment` or `Activity`.

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
		final List<Post> response = MyAppApi.getPosts();
		return DataUtils.getContentValues(response);
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
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
		final List<User> response = MyAppApi.getUsers();
		return DataUtils.getContentValues(response);
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
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

The Post and User models are defined below. In order for the `DataUtils.getContentValues()` method to be able to convert your model into a `ContentValues` object you will need to include a `@ColumnName` annotation on each field you want persisted in the database.

```java
public static class Post {

	@ColumnName(PostTable.Columns.ID)
	private long mId;

	@ColumnName(PostTable.Columns.TEXT)
	private String mText;

	@ColumnName(PostTable.Columns.USER_ID)
	private long mUserId;

	@ColumnName(PostTable.Columns.DATE)
	private long mDate;
}
```

```java
public static class User {

	@ColumnName(UserTable.Columns.ID)
	private long mId;

	private String mName;

	@ColumnName(UserTable.Columns.NAME)
	public String getName() {
		return mName != null ? mName : "Anonymous";
	}

	@ColumnName(UserTable.Columns.AGE)
	private int mAge;
}
```