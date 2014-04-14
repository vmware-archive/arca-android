# Arca-Broadcaster

The Arca Broadcaster package adds the ability to send local broadcasts. This package is used by various other Arca packages in order to communicate errors and/or results.

## Usage

### Registering for Broadcasts

In order to receive local broadcasts, you need to create a receiver which subclasses the ArcaBroadcastReceiver class. 

```java
public class MyBroadcastReceiver extends ArcaBroadcastReceiver {

	@Override
	public void onReceive(final Context context, final Intent intent) {
		// do something
	}
}
```

From that point you all your receiver needs to know is what action you would like to listen for. The ArcaBroadcastReceiver will automatically communicate with the ArcaBroadcastManager whenever you tell it to register or unregister. 

**Note:** *Similar to a typical BroadcastReceiver, its important to unregister when you're done.*

```java
public void register() {
	mReceiver.register("some_action");
}

public void unregister() {
	mReceiver.unregister();
}
```

### Sending Broadcasts

Sending broadcasts with the ArcaBroadcastManager is very similar to sending a broadcast from a context. You simlpy create an Intent and fire away.

```java 
final Intent intent = new Intent("some_action");
ArcaBroadcastManager.sendBroadcast(context, intent);
```