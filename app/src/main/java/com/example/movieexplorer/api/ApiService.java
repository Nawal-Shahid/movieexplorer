package com.example.movieexplorer.api;

import com.example.movieexplorer.model.Movie;
import com.example.movieexplorer.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    /**
     * Get popular movies
     * @param page Page number (default: 1)
     */
    @GET(ApiConstants.ENDPOINT_POPULAR)
    Call<MovieResponse> getPopularMovies(
            @Query(ApiConstants.PARAM_PAGE) int page
    );

    /**
     * Get top rated movies
     * @param page Page number (default: 1)
     */
    @GET(ApiConstants.ENDPOINT_TOP_RATED)
    Call<MovieResponse> getTopRatedMovies(
            @Query(ApiConstants.PARAM_PAGE) int page
    );

    /**
     * Search movies by query
     * @param query Search query string
     * @param page Page number (default: 1)
     */
    @GET(ApiConstants.ENDPOINT_SEARCH)
    Call<MovieResponse> searchMovies(
            @Query(ApiConstants.PARAM_QUERY) String query,
            @Query(ApiConstants.PARAM_PAGE) int page
    );

    /**
     * Get movie details by ID
     * @param movieId The movie ID
     */
    @GET(ApiConstants.ENDPOINT_MOVIE_DETAILS)
    Call<Movie> getMovieDetails(
            @Path("movie_id") int movieId
    );

    /**
     * Get similar movies by movie ID
     * @param movieId The movie ID
     * @param page Page number (default: 1)
     */
    @GET(ApiConstants.ENDPOINT_SIMILAR)
    Call<MovieResponse> getSimilarMovies(
            @Path("movie_id") int movieId,
            @Query(ApiConstants.PARAM_PAGE) int page
    );

    /**
     * Get now playing movies
     * @param page Page number (default: 1)
     */
    @GET("movie/now_playing")
    Call<MovieResponse> getNowPlayingMovies(
            @Query(ApiConstants.PARAM_PAGE) int page
    );

    /**
     * Get upcoming movies
     * @param page Page number (default: 1)
     */
    @GET("movie/upcoming")
    Call<MovieResponse> getUpcomingMovies(
            @Query(ApiConstants.PARAM_PAGE) int page
    );
}