package com.example.movieexplorer.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.movieexplorer.MovieExplorerApp;
import com.example.movieexplorer.repository.MovieRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private MovieRepository movieRepository;

    public ViewModelFactory() {
        this.movieRepository = MovieRepository.getInstance(MovieExplorerApp.getInstance().getDatabase());
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MovieViewModel.class)) {
            return (T) new MovieViewModel();
        } else if (modelClass.isAssignableFrom(SharedViewModel.class)) {
            return (T) new SharedViewModel();
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}