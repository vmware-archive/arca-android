package com.xtreme.rest.dispatcher;

import android.database.Cursor;
import android.os.Bundle;

public class CursorUtils {

	private static final class Extras {
//		private static final String CONTENT_STATE = "content_state";
//		private static final String IS_EXECUTING_REMOTE = "is_executing_remote";
		private static final String EXTRAS = "extras";
	}
	
//	public static boolean isExecutingRemote(final Cursor cursor) {
//		if (cursor != null) {
//			final Bundle extras = cursor.getExtras();
//			return extras.getBoolean(Extras.IS_EXECUTING_REMOTE);
//		} else {
//			return false;
//		}
//	}
//	
//	public static ContentState getContentState(final Cursor cursor) {
//		if (cursor != null) {
//			final Bundle extras = cursor.getExtras();
//			return (ContentState) extras.getSerializable(Extras.CONTENT_STATE);
//		} else {
//			return null;
//		}
//	}
	
	public static Bundle getExtras(final Cursor cursor) {
		if (cursor != null) {
			final Bundle extras = cursor.getExtras();
			return extras.getBundle(Extras.EXTRAS);
		} else {
			return null;
		}
	}
	
//	public static boolean isInvalid(final Cursor cursor) {
//		final ContentState state = CursorUtils.getContentState(cursor);
//		return state == ContentState.INVALID;
//	}

	
	// ================================================
	

//	public static void updateCursorWithExtras(final Context context, final Uri uri, final Cursor cursor, final DatasetValidator validator) {
//		final Uri notificationUri = UriUtils.stripQueryParameter(uri, ContentRequest.Params.FORCE_UPDATE);
//		final ContentResolver resolver = context.getContentResolver();
//		cursor.setNotificationUri(resolver, notificationUri);
//		
//		Logger.v("[Cursor Updated] NotificationUri : %s", notificationUri);
//		
//		final ContentState state = getContentState(context, uri, cursor, validator);
//		final boolean isExecutingRemote = isExecutingRemote(context, notificationUri, cursor, validator, state);
//		final Bundle extras = createExtras(cursor.getExtras(), state, isExecutingRemote);
//		cursor.respond(extras);
//		
//		Logger.v("[Cursor Updated] ContentState : %s, IsExecutingRemote : %b", state, isExecutingRemote);
//	}
//
//	private static ContentState getContentState(final Context context, final Uri uri, final Cursor cursor, final DatasetValidator validator) {
//		final String forceUpdate = uri.getQueryParameter(ContentRequest.Params.FORCE_UPDATE);
//		final boolean isInvalid = forceUpdate != null && Boolean.parseBoolean(forceUpdate);
//
//		if (isInvalid) {
//			return ContentState.INVALID;
//
//		} else if (validator == null) {
//			return ContentState.VALID;
//
//		} else {
//			return validator.validate(uri, cursor);
//		}
//	}
//	
//	private static boolean isExecutingRemote(final Context context, final Uri uri, final Cursor cursor, final DatasetValidator validator, final ContentState state) {
//		if (state != ContentState.VALID && validator != null) {
//			return validator.fetchData(context, uri, cursor);
//		} else {
//			return false;
//		}
//	}
//	
//	private static Bundle createExtras(final Bundle oldExtras, final ContentState contentState, final boolean isExecutingRemote) {
//		final Bundle extras = new Bundle();
//		extras.putBundle(Extras.EXTRAS, oldExtras);
//		extras.putSerializable(Extras.CONTENT_STATE, contentState);
//		extras.putBoolean(Extras.IS_EXECUTING_REMOTE, isExecutingRemote);
//		return extras;
//	}

}
