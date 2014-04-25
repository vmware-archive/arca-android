package com.appnet.app.models;

public class Post extends AbsPost {

	public static class Fields extends AbsPost.Fields {}

	public String getImageUrl() {
		final User user = getUser();
		return user != null ? user.getImageUrl() : null;
	}
	
}
