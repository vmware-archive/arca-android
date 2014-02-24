package com.rottentomatoes.app.application;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Locale;

import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;

import com.arca.utils.Logger;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.rottentomatoes.app.models.MoviesResponse;
import com.xtreme.network.NetworkRequest;
import com.xtreme.network.NetworkRequestLauncher;
import com.xtreme.network.NetworkResponse;

public class RottenTomatoesRequests {

	private static final String API_KEY = RottenTomatoesApi.KEY;
	
	private static final String BASE_URL = "http://api.rottentomatoes.com/api/public/v1.0";


	public static MoviesResponse getMovieList(final String type, final int limit, final String country) throws ClientProtocolException, IOException {
		final String url = String.format(Locale.getDefault(), "%s/lists/movies/%s.json?limit=%d&country=%s&apikey=%s", BASE_URL, type, limit, country, API_KEY);
		return executeRequest(new NetworkRequest(url), new Gson(), MoviesResponse.class);
	}
	

	// ==============================================
	
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
