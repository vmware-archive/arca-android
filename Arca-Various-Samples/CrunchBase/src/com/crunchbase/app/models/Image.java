package com.crunchbase.app.models;

import java.util.List;

public class Image extends AbsImage {

	public static class Fields extends AbsImage.Fields {}

	public String getLastImageUrl() {
		final List<ImageSize> sizes = getAvailableSizes();
		if (sizes != null && sizes.size() > 0) {
			return "http://crunchbase.com/" + sizes.get(sizes.size() - 1).getUrl();
		} else { 
			return null;
		}
	}
	
}
