package com.example.movieexplorer.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieexplorer.model.Movie;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<Movie> selectedMovie = new MutableLiveData<>();
    private MutableLiveData<Boolean> isDarkMode = new MutableLiveData<>(false);
    private MutableLiveData<String> currentSearchQuery = new MutableLiveData<>("");

    public void selectMovie(Movie movie) {
        selectedMovie.setValue(movie);
    }

    public LiveData<Movie> getSelectedMovie() {
        return selectedMovie;
    }

    public void setDarkMode(boolean darkMode) {
        isDarkMode.setValue(darkMode);
    }

    public LiveData<Boolean> getIsDarkMode() {
        return isDarkMode;
    }

    public void setSearchQuery(String query) {
        currentSearchQuery.setValue(query);
    }

    public LiveData<String> getCurrentSearchQuery() {
        return currentSearchQuery;
    }

    public void clearSelectedMovie() {
        selectedMovie.setValue(null);
    }
}