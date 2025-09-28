package com.example.movieexplorer.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieexplorer.R;
import com.example.movieexplorer.adapter.MovieAdapter;
import com.example.movieexplorer.model.Movie;
import com.example.movieexplorer.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements MovieAdapter.OnMovieClickListener {
    private EditText searchEditText;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private MovieAdapter adapter;
    private MovieViewModel movieViewModel;
    private List<Movie> searchResults = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        setupViewModel();
        setupSearchListener();
    }

    private void initViews(View view) {
        searchEditText = view.findViewById(R.id.searchEditText);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void setupRecyclerView() {
        adapter = new MovieAdapter(searchResults, this, isDarkMode());
        int span = getResources().getInteger(R.integer.grid_span_count);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), span));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(null); // Disable animations to prevent crashes
    }

    private void setupViewModel() {
        movieViewModel = new ViewModelProvider(requireActivity()).get(MovieViewModel.class);

        movieViewModel.getSearchResults().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null && !movies.isEmpty()) {
                searchResults.clear();
                searchResults.addAll(movies);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            } else if (movies != null && movies.isEmpty()) {
                searchResults.clear();
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
        });

        movieViewModel.getLoadingState().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });

        movieViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                progressBar.setVisibility(View.GONE);
                // Optionally show a toast or error message here
            }
        });
    }

    private void setupSearchListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.length() > 2) {
                    movieViewModel.searchMovies(query);
                } else {
                    searchResults.clear();
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private boolean isDarkMode() {
        return (getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK)
                == android.content.res.Configuration.UI_MODE_NIGHT_YES;
    }

    @Override
    public void onMovieClick(Movie movie) {
        // Navigate to detail fragment
        if (getActivity() != null) {
            androidx.navigation.NavController navController = androidx.navigation.Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
            if (navController != null) {
                com.example.movieexplorer.navigation.AppNavigator appNavigator = new com.example.movieexplorer.navigation.AppNavigator(navController);
                appNavigator.navigateToMovieDetail(movie.getId());
            }
        }
    }

    @Override
    public void onFavoriteClick(Movie movie, int position) {
        movieViewModel.toggleFavorite(movie);
        adapter.notifyItemChanged(position);
    }
}