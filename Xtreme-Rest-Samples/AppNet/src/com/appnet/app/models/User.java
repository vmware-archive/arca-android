package com.appnet.app.models;

public class User extends AbsUser {

	public static class Fields extends AbsUser.Fields {}

	public String getImageUrl() {
		final AvatarImage image = getAvatarImage();
		return image != null ? image.getUrl() : null;
	}
	
}
