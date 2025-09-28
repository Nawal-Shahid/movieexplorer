package com.example.movieexplorer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieexplorer.R;
import com.example.movieexplorer.model.Movie;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {
    private List<Movie> favoriteMovies;
    private OnFavoriteClickListener listener;

    public FavoritesAdapter(List<Movie> favoriteMovies, OnFavoriteClickListener listener) {
        this.favoriteMovies = favoriteMovies;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new FavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        Movie movie = favoriteMovies.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return favoriteMovies.size();
    }

    public void updateFavorites(List<Movie> newFavorites) {
        this.favoriteMovies.clear();
        this.favoriteMovies.addAll(newFavorites);
        notifyDataSetChanged();
    }

    class FavoritesViewHolder extends RecyclerView.ViewHolder {
        private ImageView moviePoster;
        private TextView movieTitle, ratingText;
        private MaterialButton favoriteButton;

        public FavoritesViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.moviePoster);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            ratingText = itemView.findViewById(R.id.ratingText);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);

            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onFavoriteClick(favoriteMovies.get(getAdapterPosition()));
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
                    .into(moviePoster);

            // Favorite button is always filled for favorites
            favoriteButton.setIconResource(R.drawable.ic_favorite_filled);
            favoriteButton.setIconTintResource(R.color.primary);

            favoriteButton.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onRemoveFavorite(favoriteMovies.get(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Movie movie);
        void onRemoveFavorite(Movie movie, int position);
    }
}
