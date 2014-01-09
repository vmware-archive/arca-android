package com.xtreme.rest.providers;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.xtreme.rest.loader.ContentRequest;
import com.xtreme.rest.loader.ContentState;

/**
 * A validator to be used with a {@link Dataset} to identify the {@link ContentState} of the {@link Cursor}'s content and
 * fetch new data if necessary.
 */
public interface DatasetValidator {
	/**
	 * Validates the data in the provided cursor and identifies the state of the content. The state is then used to determine
	 * whether we should call {@link #fetchData(Context, Uri, Cursor)}
	 * 
	 * @param uri The {@link Uri} used with the {@link ContentRequest} and associated with this {@link Cursor}
	 * @param cursor The {@link Cursor} providing the data to be validated
	 * @return The state of the content, see {@link ContentState} for more info
	 */
	public ContentState validate(Uri uri, Cursor cursor);

	/**
	 * The data in the provided {@link Cursor} needs to be updated. In this method, begin an update process that downloads the new
	 * data, inserts it into the {@link DatasetProvider}, and then calls {@link ContentResolver#notifyChange(Uri, android.database.ContentObserver)}
	 * or {@link ContentResolver#notifyChange(Uri, android.database.ContentObserver, boolean)}.
	 * </br>
	 * </br>
	 * Please note that the update must be asynchronous.
	 * 
	 * @param context The {@link Context} in which the {@link ContentProvider} is running.
	 * @param uri The {@link Uri} for which the data must be updated
	 * @param cursor The {@link Cursor} providing the current data
	 * @return <code>true</code> if an update has been started, <code>false</code> otherwise.
	 */
	public boolean fetchData(Context context, Uri uri, Cursor cursor);
}
