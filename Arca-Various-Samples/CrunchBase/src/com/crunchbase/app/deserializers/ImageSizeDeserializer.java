package com.crunchbase.app.deserializers;

import java.lang.reflect.Type;

import com.crunchbase.app.models.ImageSize;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class ImageSizeDeserializer implements JsonDeserializer<ImageSize> {

	@Override
	public ImageSize deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		
		final JsonArray array = (JsonArray) json;
		final JsonArray size = array.get(0).getAsJsonArray();
		
		final Long width = size.get(0).getAsLong();
		final Long height = size.get(1).getAsLong();
		final String url = array.get(1).getAsString();
		
		final ImageSize imageSize = new ImageSize();
		imageSize.setWidth(width);
		imageSize.setHeight(height);
		imageSize.setUrl(url);
		
		return imageSize;
	}

}
