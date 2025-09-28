package com.example.movieexplorer.repository;

import androidx.lifecycle.LiveData;

import com.example.movieexplorer.database.AppDatabase;
import com.example.movieexplorer.database.MovieDao;
import com.example.movieexplorer.model.Movie;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FavoriteRepository {
    private MovieDao movieDao;
    private Executor executor;
    private static FavoriteRepository instance;

    private FavoriteRepository(AppDatabase database) {
        movieDao = database.movieDao();
        executor = Executors.newSingleThreadExecutor();
    }

    public static synchronized FavoriteRepository getInstance(AppDatabase database) {
        if (instance == null) {
            instance = new FavoriteRepository(database);
        }
        return instance;
    }

    public LiveData<List<Movie>> getFavorites() {
        return movieDao.getFavoriteMovies();
    }

    public LiveData<Integer> getFavoriteCount() {
        return movieDao.getFavoriteCount();
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

    public void toggleFavorite(Movie movie) {
        executor.execute(() -> {
            Movie existing = movieDao.getMovieByIdSync(movie.getId());
            if (existing != null) {
                existing.setFavorite(!existing.isFavorite());
                movieDao.update(existing);
            } else {
                movie.setFavorite(true);
                movieDao.insert(movie);
            }
        });
    }

    public void clearAllFavorites() {
        executor.execute(() -> {
            movieDao.clearAllFavorites();
        });
    }

    public boolean isMovieFavorite(int movieId) {
        return movieDao.isMovieFavorite(movieId);
    }

    public LiveData<Boolean> isMovieFavoriteLive(int movieId) {
        return new LiveData<Boolean>() {
            @Override
            protected void onActive() {
                super.onActive();
                executor.execute(() -> {
                    boolean isFavorite = movieDao.isMovieFavorite(movieId);
                    postValue(isFavorite);
                });
            }
        };
    }
}