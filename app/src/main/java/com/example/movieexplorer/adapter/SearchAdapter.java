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

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private List<Movie> searchResults;
    private OnSearchItemClickListener listener;

    public SearchAdapter(List<Movie> searchResults, OnSearchItemClickListener listener) {
        this.searchResults = searchResults;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_horizontal, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Movie movie = searchResults.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public void updateSearchResults(List<Movie> newResults) {
        this.searchResults.clear();
        this.searchResults.addAll(newResults);
        notifyDataSetChanged();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {
        private ImageView moviePoster;
        private TextView movieTitle, movieYear, movieRating;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.moviePoster);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            movieYear = itemView.findViewById(R.id.movieYear);
            movieRating = itemView.findViewById(R.id.movieRating);

            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onSearchItemClick(searchResults.get(getAdapterPosition()));
                }
            });
        }

        public void bind(Movie movie) {
            movieTitle.setText(movie.getTitle());

            // Extract year from release date
            if (movie.getReleaseDate() != null && movie.getReleaseDate().length() >= 4) {
                movieYear.setText(movie.getReleaseDate().substring(0, 4));
            }

            movieRating.setText(String.format("%.1f", movie.getVoteAverage()));

            // Load image with Glide
            Glide.with(itemView.getContext())
                    .load("https://image.tmdb.org/t/p/w300" + movie.getPosterPath())
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .into(moviePoster);
        }
    }

    public interface OnSearchItemClickListener {
        void onSearchItemClick(Movie movie);
    }
}