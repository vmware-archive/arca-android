package com.appnet.app.application;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;

import com.appnet.app.models.PostsResponse;
import com.arca.utils.Logger;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.xtreme.network.NetworkRequest;
import com.xtreme.network.NetworkRequestLauncher;
import com.xtreme.network.NetworkResponse;

public class AppNetRequests {

	private static final String BASE_URL = "https://alpha-api.app.net/";
	
	private static final class Apis {
		public static final String GLOBAL_STREAM = BASE_URL + "stream/0/posts/stream/global";
	}
	
	public static PostsResponse getPostList(final int count) throws ClientProtocolException, IOException {
		final String url = Apis.GLOBAL_STREAM + "?count=" + count;
		return executeRequest(new NetworkRequest(url), new Gson(), PostsResponse.class);
	}

	
	// =============================================
	
	
	private static <T> T executeRequest(final NetworkRequest request, final Gson gson, final Type type) throws ClientProtocolException, IOException {
		Logger.v("Requesting url : %s", request.getUrl());
		final NetworkResponse response = NetworkRequestLauncher.getInstance().executeRequestSynchronously(request);
		final StatusLine status = response.getStatus();
		Logger.v("Received response [%d] %s", status.getStatusCode(), status.getReasonPhrase());
		final InputStreamReader inputStreamReader = new InputStreamReader(response.getInputStream());
		final JsonReader jsonReader = new JsonReader(inputStreamReader);
		final T list = gson.fromJson(jsonReader, type);
		jsonReader.close();
		return list;
	}
}
