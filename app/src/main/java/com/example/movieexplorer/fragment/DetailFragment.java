package com.example.movieexplorer.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.movieexplorer.R;
import com.example.movieexplorer.model.Movie;
import com.example.movieexplorer.viewmodel.MovieViewModel;
import com.google.android.material.button.MaterialButton;

public class DetailFragment extends Fragment {
    private static final String TAG = "DetailFragment";

    private ImageView backdropImage, posterImage;
    private TextView titleText, overviewText, ratingText, releaseDateText, voteCountText;
    private MaterialButton favoriteButton;
    private ProgressBar progressBar;
    private MovieViewModel movieViewModel;
    private Movie currentMovie;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "DetailFragment created");
        setupViewModel();
        loadMovieDetails();
    }

    private void initViews(View view) {
        backdropImage = view.findViewById(R.id.backdropImage);
        posterImage = view.findViewById(R.id.posterImage);
        titleText = view.findViewById(R.id.titleText);
        overviewText = view.findViewById(R.id.overviewText);
        ratingText = view.findViewById(R.id.ratingText);
        releaseDateText = view.findViewById(R.id.releaseDateText);
        voteCountText = view.findViewById(R.id.voteCountText);
        favoriteButton = view.findViewById(R.id.favoriteButton);
        progressBar = view.findViewById(R.id.progressBar);

        Log.d(TAG, "Views initialized");
    }

    private void setupViewModel() {
        movieViewModel = new ViewModelProvider(requireActivity()).get(MovieViewModel.class);
        Log.d(TAG, "ViewModel setup");
    }

    private void loadMovieDetails() {
        if (getArguments() != null) {
            Log.d(TAG, "Arguments received: " + getArguments().toString());

            int movieId = getArguments().getInt("movieId", 0);
            if (movieId == 0) {
                movieId = getArguments().getInt("movie_id", 0);
            }

            Log.d(TAG, "Movie ID: " + movieId);

            if (movieId == 0) {
                Toast.makeText(getContext(), "Error: No movie ID provided", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            Log.d(TAG, "Loading movie details for ID: " + movieId);

            movieViewModel.getMovieDetails(movieId).observe(getViewLifecycleOwner(), movie -> {
                if (movie != null) {
                    currentMovie = movie;
                    displayMovieDetails(movie);
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "Movie details loaded: " + movie.getTitle());
                } else {
                    Toast.makeText(getContext(), "Failed to load movie details", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, "Failed to load movie details");
                }
            });

            // Check favorite status
            movieViewModel.getMovieFromDb(movieId).observe(getViewLifecycleOwner(), dbMovie -> {
                if (dbMovie != null) {
                    updateFavoriteButton(dbMovie.isFavorite());
                    Log.d(TAG, "Favorite status: " + dbMovie.isFavorite());
                } else {
                    updateFavoriteButton(false);
                    Log.d(TAG, "Movie not in favorites");
                }
            });
        } else {
            Toast.makeText(getContext(), "Error: No arguments provided", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "No arguments provided");
        }
    }

    private void displayMovieDetails(Movie movie) {
        titleText.setText(movie.getTitle());
        overviewText.setText(movie.getOverview());
        ratingText.setText(String.format("%.1f", movie.getVoteAverage()));
        releaseDateText.setText(movie.getReleaseDate());

        if (voteCountText != null) {
            voteCountText.setText(String.valueOf(movie.getVoteCount()));
        }

        // Load images with Glide
        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w780" + movie.getBackdropPath())
                .placeholder(R.drawable.ic_movie_placeholder)
                .error(R.drawable.ic_error)
                .into(backdropImage);

        Glide.with(this)
                .load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath())
                .placeholder(R.drawable.ic_movie_placeholder)
                .error(R.drawable.ic_error)
                .into(posterImage);

        favoriteButton.setOnClickListener(v -> toggleFavorite());

        Log.d(TAG, "Movie details displayed: " + movie.getTitle());
    }

    private void toggleFavorite() {
        if (currentMovie != null) {
            movieViewModel.toggleFavorite(currentMovie);
            boolean newFavoriteState = !currentMovie.isFavorite();
            updateFavoriteButton(newFavoriteState);
            Toast.makeText(getContext(),
                    newFavoriteState ? "Added to favorites" : "Removed from favorites",
                    Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Favorite toggled: " + newFavoriteState);
        }
    }

    private void updateFavoriteButton(boolean isFavorite) {
        if (isFavorite) {
            favoriteButton.setIconResource(R.drawable.ic_favorite_filled);
            favoriteButton.setText("Remove from Favorites");
        } else {
            favoriteButton.setIconResource(R.drawable.ic_favorite_border);
            favoriteButton.setText("Add to Favorites");
        }
    }
}