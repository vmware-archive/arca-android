# Arca-Dispatcher

The Arca Dispatcher package makes it really easy for you to request data synchronously or asynchronously from a ContentProvider. It accepts Query, Update, Insert and Delete requests and returns a corresponding QueryResult, UpdateResult, InsertResult or DeleteResult.

The default implementation of the RequestExecutor within this package executes the corresponding request against a ContentProvider but you can specify your own RequestExecutor with custom behaviour. 

In the case of asynchronous execution, background processing is achieved using an AsyncTaskLoader.

## Usage

Executing a Request is simple. First you need to create a Dispatcher. You can have as many or as few as you like. We recommend one per Fragment.

```java
public RequestDispatcher createDispatcher() {
    final ContentResolver resolver = activity.getContentResolver();
    final RequestExecutor executor = new DefaultRequestExecutor(resolver, activity);
    return new ModernRequestDispatcher(executor, activity, activity.getLoaderManager());
}
```

And then you can use that Dispatcher to execute a request synchronsouly or asynchronously. In both cases you will get back a Result object which contains the requested data.

### Synchronous Execution

```java
public void executeSynchronously() {
    final RequestDispatcher dispatcher = createDispatcher();
    final QueryResult result = dispatcher.execute(new Query(uri));
}
```

### Asynchronous Execution

```java
public void executeAsynchronously() {
    final RequestDispatcher dispatcher = createDispatcher();
    dispatcher.execute(new Query(uri), new QueryListener() {
        
        @Override
        public void onRequestComplete(final QueryResult result) {
            // do something
        }
    });
}
```

The RequestDispatcher interface handles all the typical CRUD operations.

```java
public interface RequestDispatcher {

    // Synchronous
    public QueryResult execute(Query request);
    public UpdateResult execute(Update request);
	public InsertResult execute(Insert request);
	public DeleteResult execute(Delete request);

	// Asynchronous
	public void execute(Query request, QueryListener listener);
	public void execute(Update request, UpdateListener listener);
	public void execute(Insert request, InsertListener listener);
	public void execute(Delete request, DeleteListener listener);
	
}
```