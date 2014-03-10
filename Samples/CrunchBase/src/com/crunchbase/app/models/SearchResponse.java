package com.crunchbase.app.models;

public class SearchResponse extends AbsSearchResponse {

	private static final int PAGE_SIZE = 30;
	
	public static class Fields extends AbsSearchResponse.Fields {}
	
	public int getNextPage() {
		final int total = getTotal();
		final int page = getPage();
		return (PAGE_SIZE * page) < total ? page + 1 : -1;
	}
}
