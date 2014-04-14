# Arca-Monitor

The Arca Monitor package adds a layer on top of the Arca Dispatcher that allows you to monitor every request that gets executed in the system. This allows you to verify every Query, Updated, Insert or Delete result and resolve differences with a remote store when appropriate.

**Note:** *Monitoring callbacks occur on the same thread in which the request is executed.*

## Usage

When implementing a custom Monitor you should return one or more flags to indicate the state of the data you are monitoring. 

**Flags.DATA_SYNCING** will set the isSyncing flag in the Result object which gets returned from the Arca Dispatcher. This flag can then be used to decided whether or not you need to show a loading indicator in your UI after receiving the Result object.

**Flags.DATA_INVALID** will set the isValid flag in the Result object which gets returned from the Arca Dispatcher. If the isValid flag is false, the Result will be ignored and the listener will not receive a callback for that Request.

### Simple example

A simple implementation using the Arca Service package might look something like the following:

```java
public class MyDataMonitor extends AbstractRequestMonitor {

    @Override
	public int onPostExecute(final Context context, final Query request, final QueryResult result) {
		final int count = result.getResult().getCount();
		if (count == 0 && getDataFromNetwork(context, request.getUri())) {
			return Flags.DATA_SYNCING; 
		} else {
			return 0;
		}
	}

	private boolean getDataFromNetwork(final Context context, final Uri uri) {
		final boolean isSyncing = OperationService.start(context, new GetDataOperation(uri));
		return isSyncing;
	}

	@Override
	public int onPostExecute(final Context context, final Insert request, final InsertResult result) {
		final Integer numInserted = result.getResult();
		if (numInserted > 0 && postDataToNetwork(context, request.getUri())) {
			return Flags.DATA_SYNCING; 
		} else {
			return 0;
		}
	}

	private boolean postDataToNetwork(final Context context, final Uri uri) {
		final boolean isSyncing = OperationService.start(context, new PostDataOperation(uri));
		return isSyncing;
	}
}
```

This custom Arca Monitor can be registered with the Arca Dispatcher whenever it gets constructed.

```java
final ArcaDispatcher dispatcher = createDispatcher(this);
dispatcher.setRequestMonitor(new MyDataMonitor());
```