package com.example.movieexplorer.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieexplorer.api.ApiClient;
import com.example.movieexplorer.api.ApiService;
import com.example.movieexplorer.database.AppDatabase;
import com.example.movieexplorer.database.MovieDao;
import com.example.movieexplorer.model.Movie;
import com.example.movieexplorer.model.MovieResponse;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private ApiService apiService;
    private MovieDao movieDao;
    private Executor executor;
    private static MovieRepository instance;

    private MovieRepository(AppDatabase database) {
        apiService = ApiClient.getClient().create(ApiService.class);
        movieDao = database.movieDao();
        executor = Executors.newFixedThreadPool(4); // Increased thread pool for better performance
    }

    public static synchronized MovieRepository getInstance(AppDatabase database) {
        if (instance == null) {
            instance = new MovieRepository(database);
        }
        return instance;
    }

    // Network operations
    public LiveData<List<Movie>> getPopularMovies(int page) {
        MutableLiveData<List<Movie>> data = new MutableLiveData<>();

        apiService.getPopularMovies(page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getResults();
                    // Cache movies in database
                    cacheMovies(movies);
                    data.setValue(movies);
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<List<Movie>> searchMovies(String query, int page) {
        MutableLiveData<List<Movie>> data = new MutableLiveData<>();

        if (query == null || query.trim().isEmpty()) {
            data.setValue(null);
            return data;
        }

        apiService.searchMovies(query, page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body().getResults());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<Movie> getMovieDetails(int movieId) {
        MutableLiveData<Movie> data = new MutableLiveData<>();

        apiService.getMovieDetails(movieId).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    Movie movie = response.body();
                    // Cache the movie details
                    if (movie != null) {
                        cacheMovie(movie);
                    }
                    data.setValue(movie);
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<List<Movie>> getSimilarMovies(int movieId, int page) {
        MutableLiveData<List<Movie>> data = new MutableLiveData<>();

        apiService.getSimilarMovies(movieId, page).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body().getResults());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

    // Database operations
    public LiveData<List<Movie>> getFavoriteMovies() {
        return movieDao.getFavoriteMovies();
    }

    public LiveData<Movie> getMovieFromDb(int movieId) {
        return movieDao.getMovieById(movieId);
    }

    public void toggleFavorite(Movie movie) {
        executor.execute(() -> {
            Movie existingMovie = movieDao.getMovieByIdSync(movie.getId());
            if (existingMovie != null) {
                existingMovie.setFavorite(!existingMovie.isFavorite());
                movieDao.update(existingMovie);
            } else {
                movie.setFavorite(true);
                movieDao.insert(movie);
            }
        });
    }

    public void addToFavorites(Movie movie) {
        executor.execute(() -> {
            movie.setFavorite(true);
            Movie existing = movieDao.getMovieByIdSync(movie.getId());
            if (existing != null) {
                movieDao.update(movie);
            } else {
                movieDao.insert(movie);
            }
        });
    }

    public void removeFromFavorites(int movieId) {
        executor.execute(() -> {
            movieDao.setFavoriteStatus(movieId, false);
        });
    }
    public LiveData<Integer> getFavoriteCount() {
        return movieDao.getFavoriteCount();
    }

    // Caching methods
    private void cacheMovies(List<Movie> movies) {
        executor.execute(() -> {
            if (movies == null) return;
            for (Movie m : movies) {
                if (m == null) continue;
                boolean fav = movieDao.isMovieFavorite(m.getId());
                m.setFavorite(fav);
                movieDao.insert(m);
            }
        });
    }

    private void cacheMovie(Movie movie) {
        executor.execute(() -> {
            if (movie == null) return;
            boolean fav = movieDao.isMovieFavorite(movie.getId());
            movie.setFavorite(fav || movie.isFavorite());
            movieDao.insert(movie);
        });
    }

    public void clearCache() {
        executor.execute(() -> {
            movieDao.deleteNonFavorites();
        });
    }
}