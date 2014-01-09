package com.appnet.app.providers;

import android.net.Uri;

import com.appnet.app.datasets.PostTable;
import com.xtreme.rest.providers.RestContentProvider;

public class AppNetContentProvider extends RestContentProvider {

	public static final String AUTHORITY = "com.appnet.app.providers.AppNetContentProvider";
	
	public static class Uris {
		public static final Uri POSTS_URI = Uri.parse("content://" + AUTHORITY + "/" + Paths.POSTS);
	}
	
	protected static class Paths {
		public static final String POSTS = "posts";
	}
	
	@Override
	public boolean onCreate() {
        registerDataset(AUTHORITY, Paths.POSTS, PostTable.class);
		registerDataset(AUTHORITY, Paths.POSTS + "/*", PostTable.class);
		return true;
	}
}