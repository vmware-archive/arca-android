package com.crunchbase.app.models;

public class Company extends AbsCompany {

	public static class Fields extends AbsCompany.Fields {}
	
	public String getImageUrl() {
	    final Image image = getImage();
	    return image != null ? image.getLastImageUrl() : null;
	}
}
