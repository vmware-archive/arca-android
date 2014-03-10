package com.crunchbase.app.application;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import org.apache.http.client.ClientProtocolException;

import com.arca.utils.Logger;
import com.crunchbase.app.deserializers.ImageSizeDeserializer;
import com.crunchbase.app.models.ImageSize;
import com.crunchbase.app.models.SearchResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.xtreme.network.INetworkRequestLauncher;
import com.xtreme.network.NetworkRequest;
import com.xtreme.network.NetworkRequestLauncher;
import com.xtreme.network.NetworkResponse;

public class CrunchBaseRequests {

	private static final String API_KEY = CrunchBaseApi.KEY;
	private static final String BASE_URL = "http://api.crunchbase.com/v/1/";
	
	private static final class Apis {
		public static final String SEARCH = BASE_URL + "search.js"; 
		// public static final String COMPANIES = BASE_URL + "companies.js";
	}
	
	public static SearchResponse getSearchResults(final String geo, final int page) throws ClientProtocolException, IOException {
		final String url = Apis.SEARCH + "?geo=" + geo + "&page=" + page + "&api_key=" + API_KEY;
		final GsonBuilder gson = new GsonBuilder().registerTypeAdapter(ImageSize.class, new ImageSizeDeserializer());
		return executeRequest(new NetworkRequest(url), gson.create(), SearchResponse.class);
	}
	
	private static <T> T executeRequest(final NetworkRequest request, final Gson gson, final Type type) throws ClientProtocolException, IOException {
		Logger.v("Request URL: %s", request.getUrl());
		final INetworkRequestLauncher launcher = NetworkRequestLauncher.getInstance();
		final NetworkResponse response = launcher.executeRequestSynchronously(request);
		final InputStream inputStream = response.getInputStream();
		final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		final JsonReader jsonReader = new JsonReader(inputStreamReader);
		final T list = gson.fromJson(jsonReader, type);
		jsonReader.close();
		return list;
	}

}
