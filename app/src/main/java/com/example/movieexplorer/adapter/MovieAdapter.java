package com.example.movieexplorer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.movieexplorer.R;
import com.example.movieexplorer.model.Movie;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private List<Movie> movies;
    private OnMovieClickListener listener;
    private boolean isDarkMode;

    public MovieAdapter(List<Movie> movies, OnMovieClickListener listener, boolean isDarkMode) {
        this.movies = movies;
        this.listener = listener;
        this.isDarkMode = isDarkMode;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public long getItemId(int position) {
        return movies.get(position).getId();
    }

    public void updateMovies(List<Movie> newMovies) {
        this.movies.clear();
        this.movies.addAll(newMovies);
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView moviePoster;
        private TextView movieTitle, ratingText;
        private MaterialButton favoriteButton;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.moviePoster);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            ratingText = itemView.findViewById(R.id.ratingText);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);

            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onMovieClick(movies.get(getAdapterPosition()));
                }
            });
        }

        public void bind(Movie movie) {
            movieTitle.setText(movie.getTitle());
            ratingText.setText(String.format("%.1f", movie.getVoteAverage()));

            // Load image with Glide
            Glide.with(itemView.getContext())
                    .load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath())
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .error(R.drawable.ic_movie_error)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(moviePoster);

            // Update favorite button
            updateFavoriteButton(movie.isFavorite());

            favoriteButton.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (listener != null && pos != RecyclerView.NO_POSITION) {
                    // Optimistically toggle local state for instant UI feedback
                    Movie m = movies.get(pos);
                    m.setFavorite(!m.isFavorite());
                    updateFavoriteButton(m.isFavorite());
                    // Bounce animation for feedback
                    favoriteButton.animate().cancel();
                    favoriteButton.setScaleX(1f);
                    favoriteButton.setScaleY(1f);
                    favoriteButton.animate()
                            .scaleX(1.15f)
                            .scaleY(1.15f)
                            .setDuration(120)
                            .setInterpolator(new OvershootInterpolator())
                            .withEndAction(() -> favoriteButton.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(120)
                                    .start())
                            .start();
                    // Notify persistence layer
                    listener.onFavoriteClick(m, pos);
                }
            });
        }

        private void updateFavoriteButton(boolean isFavorite) {
            if (isFavorite) {
                favoriteButton.setIconResource(R.drawable.ic_favorite_filled);
                favoriteButton.setIconTintResource(R.color.primary);
            } else {
                favoriteButton.setIconResource(R.drawable.ic_favorite_border);
                favoriteButton.setIconTintResource(android.R.color.white);
            }
        }
    }

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
        void onFavoriteClick(Movie movie, int position);
    }
}