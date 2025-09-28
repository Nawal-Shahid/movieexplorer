package com.example.movieexplorer.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieexplorer.MovieExplorerApp;
import com.example.movieexplorer.model.Movie;
import com.example.movieexplorer.repository.MovieRepository;

import java.util.List;

public class MovieViewModel extends ViewModel {
    private MovieRepository movieRepository;
    private MutableLiveData<List<Movie>> popularMovies;
    private MutableLiveData<List<Movie>> searchResults;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<String> errorMessage;

    public MovieViewModel() {
        movieRepository = MovieRepository.getInstance(MovieExplorerApp.getInstance().getDatabase());
        popularMovies = new MutableLiveData<>();
        searchResults = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(false);
        errorMessage = new MutableLiveData<>();

        loadPopularMovies();
    }

    public LiveData<List<Movie>> getPopularMovies() {
        return popularMovies;
    }

    public LiveData<List<Movie>> getSearchResults() {
        return searchResults;
    }

    public LiveData<Boolean> getLoadingState() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return movieRepository.getFavoriteMovies();
    }

    public LiveData<Movie> getMovieDetails(int movieId) {
        return movieRepository.getMovieDetails(movieId);
    }

    public LiveData<Movie> getMovieFromDb(int movieId) {
        return movieRepository.getMovieFromDb(movieId);
    }

    public void loadPopularMovies() {
        isLoading.setValue(true);
        LiveData<List<Movie>> moviesLiveData = movieRepository.getPopularMovies(1);
        moviesLiveData.observeForever(movies -> {
            if (movies != null) {
                popularMovies.setValue(movies);
                errorMessage.setValue(null);
            } else {
                errorMessage.setValue("Failed to load movies");
            }
            isLoading.setValue(false);
            // Remove observer after first result to prevent memory leaks
            moviesLiveData.removeObservers(null);
        });
    }

    public void searchMovies(String query) {
        isLoading.setValue(true);
        LiveData<List<Movie>> searchLiveData = movieRepository.searchMovies(query, 1);
        searchLiveData.observeForever(movies -> {
            if (movies != null) {
                searchResults.setValue(movies);
                errorMessage.setValue(null);
            } else {
                errorMessage.setValue("Search failed");
            }
            isLoading.setValue(false);
            // Remove observer after first result to prevent memory leaks
            searchLiveData.removeObservers(null);
        });
    }

    public void refreshMovies() {
        loadPopularMovies();
    }

    public void toggleFavorite(Movie movie) {
        movieRepository.toggleFavorite(movie);
    }

    public void addToFavorites(Movie movie) {
        movieRepository.toggleFavorite(movie);
    }

    public void removeFromFavorites(int movieId) {
        // Implementation would update the movie's favorite status
    }
}