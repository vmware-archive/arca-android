# Arca-Service

The Arca Service package lets you offload long running operations away from the lifecycle of your Activities and Fragments. It provides a Service implementation which accepts Operations, where each Operation is designed to encapsulate all the networking and storage required by an individual Dataset (see [Arca-Provider](../arca-provider)). 

Within each Operation you can have multiple Tasks, where each Task is responsible for requesting data from a particular endpoint. Often you may have an Operation which contains only a single Task, however, you may also need to aggregate data from multiple sources in order to fullfill the requirements of your Dataset.

Each Task can have pre-requisites and dependencies. A Task will not execute until all its pre-requisites have completed.

## Usage

### Service

The OpertionService accepts Operations through its static interface. If the service is not running it will start itself when given an Operation to execute. Similarly it will stop itself once all its Operations have finished.

```java
OperationService.start(context, new GetDataOperation(uri));
```

**Note:** *This class must be declared in the AndroidManifest.xml file as follows:*
```xml
<service android:name="com.arca.service.OperationService" android:exported="false" />
```

### Operations

An Operation is responsible for a collection of Tasks that, when executed, update the data associated with a certain Dataset. When started via the OperationService, an Operation's Tasks begin executing. Dependencies can be set up between tasks in the `onCreateTasks()` method.  When the final Task finishes executing, the Operation itself is considered finished and either `onSuccess()` or `onFailure()` is called.

#### Simple Example

In the simple example below you may notice that the Operation implements the Parcelable interface. This is required since the operation is bundled within an intent when passed to the OperationService. You will need to define your own static CREATOR class as well as write any properties to and from the given Parcel.

```java
public class PostListOperation extends Operation {

	public PostListOperation(final Uri uri) {
		super(uri);
	}

	public PostListOperation(final Parcel in) {
		super(in);
		// read properties from in
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
		// write properties to dest
	}

	@Override
	public Set<Task<?>> onCreateTasks() {
		final Set<Task<?>> set = new HashSet<Task<?>>();
		set.add(new PostListTask());
		return set;
	}

	@Override
	public void onSuccess(final Context context, final List<Task<?>> completed) {
		final ContentResolver resolver = context.getContentResolver();
		resolver.notifyChange(getUri(), null);
	}

	@Override
	public void onFailure(final Context context, final ServiceError error) {
		ErrorBroadcaster.broadcast(context, getUri(), error.getCode(), error.getMessage());
	}
	
	public static final Creator<PostListOperation> CREATOR = new Creator<PostListOperation>() {
		@Override
		public PostListOperation createFromParcel(final Parcel in) {
			return new PostListOperation(in);
		}

		@Override
		public PostListOperation[] newArray(final int size) {
			return new PostListOperation[size];
		}
	};

}
```

### Tasks

A Task consists of two components: networking and processing. A Task is considered complete when both of those components have finished  executing. 

#### Networking

The `onExecuteNetworking()` method is meant to execute network-dependent code (or anything that yields its Thread frequently and is light on the CPU). For example, this is where you would download data from an API. 

**Note:** *All networking should happen synchronously within this method.*

#### Processing

The `onExecuteProcessing()` method is meant to execute CPU-intensive processing requests. For example, this is where one would insert data into a ContentProvider (backed by a SQLiteDatabase). 

**Note:** *All processing should happen synchronously within this method.*

#### Uniqueness

The `onCreateIdentifier()` method must return a globally unique Identifier. This is used within the backing executor and ensures that if more than one Task is requesting the same data, only one actually executes. The results are then shared among all similar Tasks.

#### Simple Example

A custom Task implementation (as shown below) is responsible for implementing all the methods described above.

```java
public class PostListTask extends Task<List<Post>> {

	public PostListTask() {}

	@Override
	public Identifier<?> onCreateIdentifier() {
		return new Identifier<String>("post_list");
	}
	
	@Override
	public List<Post> onExecuteNetworking(final Context context) throws Exception {
		final PostListResponse response = MyAppEndpoint.getPostList();
		return response.getPostList();
	}

	@Override
	public void onExecuteProcessing(final Context context, final List<Post> data) throws Exception {
		final ContentValues[] values = DataUtils.getContentValues(data);
		final ContentResolver resolver = context.getContentResolver();
		resolver.bulkInsert(MyAppContentProvider.Uris.POSTS, values);
	}
}
```
