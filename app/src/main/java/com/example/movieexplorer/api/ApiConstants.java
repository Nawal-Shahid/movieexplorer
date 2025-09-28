package com.example.movieexplorer.api;

public class ApiConstants {
    // Base URLs
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";

    // Image sizes
    public static final String POSTER_SIZE_W500 = "w500";
    public static final String POSTER_SIZE_W300 = "w300";
    public static final String BACKDROP_SIZE_W780 = "w780";
    public static final String BACKDROP_SIZE_W1280 = "w1280";
    
    public static final String API_KEY = "6345e606e656a292ed857fa013b67cc7";

    // Endpoints
    public static final String ENDPOINT_POPULAR = "movie/popular";
    public static final String ENDPOINT_TOP_RATED = "movie/top_rated";
    public static final String ENDPOINT_SEARCH = "search/movie";
    public static final String ENDPOINT_MOVIE_DETAILS = "movie/{movie_id}";
    public static final String ENDPOINT_SIMILAR = "movie/{movie_id}/similar";

    // Query parameters
    public static final String PARAM_API_KEY = "api_key";
    public static final String PARAM_PAGE = "page";
    public static final String PARAM_QUERY = "query";
    public static final String PARAM_LANGUAGE = "language";

    // Default values
    public static final String DEFAULT_LANGUAGE = "en-US";
    public static final int DEFAULT_PAGE = 1;

    // Timeouts
    public static final int CONNECT_TIMEOUT = 30; // seconds
    public static final int READ_TIMEOUT = 30; // seconds
    public static final int WRITE_TIMEOUT = 30; // seconds
}