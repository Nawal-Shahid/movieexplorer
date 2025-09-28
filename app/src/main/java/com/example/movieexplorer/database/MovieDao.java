package com.example.movieexplorer.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.movieexplorer.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    // Insert operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie movie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Movie> movies);

    // Update operations
    @Update
    void update(Movie movie);

    @Update
    void updateAll(List<Movie> movies);

    // Delete operations
    @Query("DELETE FROM movies WHERE id = :movieId")
    void deleteById(int movieId);

    @Query("DELETE FROM movies WHERE isFavorite = 0")
    void deleteNonFavorites();

    @Query("DELETE FROM movies")
    void deleteAll();

    // Query operations
    @Query("SELECT * FROM movies WHERE isFavorite = 1 ORDER BY title ASC")
    LiveData<List<Movie>> getFavoriteMovies();

    @Query("SELECT * FROM movies WHERE id = :movieId")
    LiveData<Movie> getMovieById(int movieId);

    @Query("SELECT * FROM movies WHERE id = :movieId")
    Movie getMovieByIdSync(int movieId);

    @Query("SELECT * FROM movies WHERE title LIKE '%' || :query || '%' ORDER BY title ASC")
    LiveData<List<Movie>> searchMovies(String query);

    @Query("SELECT * FROM movies ORDER BY voteAverage DESC LIMIT :limit")
    LiveData<List<Movie>> getTopRatedMovies(int limit);

    @Query("SELECT COUNT(*) FROM movies WHERE isFavorite = 1")
    LiveData<Integer> getFavoriteCount();

    // Favorite operations
    @Query("UPDATE movies SET isFavorite = :isFavorite WHERE id = :movieId")
    void setFavoriteStatus(int movieId, boolean isFavorite);

    @Query("SELECT isFavorite FROM movies WHERE id = :movieId")
    boolean isMovieFavorite(int movieId);

    @Query("UPDATE movies SET isFavorite = 0")
    void clearAllFavorites();
}