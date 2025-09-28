package com.example.movieexplorer.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.movieexplorer.R;
import com.example.movieexplorer.activity.MainActivity;
import com.example.movieexplorer.adapter.MovieAdapter;
import com.example.movieexplorer.model.Movie;
import com.example.movieexplorer.viewmodel.MovieViewModel;
import com.google.android.material.search.SearchBar;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements MovieAdapter.OnMovieClickListener {
    private static final String TAG = "HomeFragment";

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchBar searchBar;
    private MovieAdapter adapter;
    private MovieViewModel movieViewModel;
    private List<Movie> movieList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "HomeFragment onViewCreated");
        setupRecyclerView();
        setupViewModel();
        setupSwipeRefresh();
        setupSearchBar();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        searchBar = view.findViewById(R.id.searchBar);
    }

    private void setupRecyclerView() {
        adapter = new MovieAdapter(movieList, this, isDarkMode());
        int span = getResources().getInteger(R.integer.grid_span_count);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), span));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    private void setupViewModel() {
        movieViewModel = new ViewModelProvider(requireActivity()).get(MovieViewModel.class);

        movieViewModel.getPopularMovies().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null) {
                movieList.clear();
                movieList.addAll(movies);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                Log.d(TAG, "Loaded " + movies.size() + " movies");
            }
        });

        movieViewModel.getLoadingState().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if (!isLoading) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            movieViewModel.refreshMovies();
        });
    }

    private void setupSearchBar() {
        searchBar.setOnClickListener(v -> {
            navigateToSearch();
        });
    }

    private void navigateToSearch() {
        try {
            NavController navController = getNavController();
            if (navController != null) {
                navController.navigate(R.id.navigation_search);
                Log.d(TAG, "Navigated to search successfully");
            } else {
                Log.e(TAG, "NavController is null in navigateToSearch");
                Toast.makeText(getContext(), "Navigation not available", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error navigating to search: " + e.getMessage(), e);
            Toast.makeText(getContext(), "Search not available", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isDarkMode() {
        return (getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK)
                == android.content.res.Configuration.UI_MODE_NIGHT_YES;
    }

    @Override
    public void onMovieClick(Movie movie) {
        Log.d(TAG, "Movie clicked: " + (movie != null ? movie.getTitle() : "null"));

        if (movie == null) {
            Toast.makeText(getContext(), "Movie data is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        navigateToMovieDetail(movie);
    }

    private void navigateToMovieDetail(Movie movie) {
        try {
            NavController navController = getNavController();

            if (navController == null) {
                Log.e(TAG, "NavController is null");
                Toast.makeText(getContext(), "Navigation not available", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d(TAG, "NavController found, current destination: " +
                    (navController.getCurrentDestination() != null ?
                            navController.getCurrentDestination().getId() : "null"));

            // Create bundle with movie data (key must match nav_graph: movie_id)
            Bundle bundle = new Bundle();
            bundle.putInt("movie_id", movie.getId());

            // Try multiple navigation methods
            boolean navigationSuccessful = false;

            // Method 1: Try using action
            try {
                navController.navigate(R.id.action_navigation_home_to_navigation_detail, bundle);
                navigationSuccessful = true;
                Log.d(TAG, "Navigation successful using action");
            } catch (IllegalArgumentException e) {
                Log.w(TAG, "Action navigation failed, trying direct: " + e.getMessage());
            }

            // Method 2: Try direct navigation if action failed
            if (!navigationSuccessful) {
                try {
                    navController.navigate(R.id.navigation_detail, bundle);
                    navigationSuccessful = true;
                    Log.d(TAG, "Navigation successful using direct destination");
                } catch (Exception e) {
                    Log.e(TAG, "Direct navigation also failed: " + e.getMessage());
                }
            }

            if (navigationSuccessful) {
                Log.d(TAG, "Navigation to detail successful for movie ID: " + movie.getId());
            } else {
                Toast.makeText(getContext(), "Unable to navigate to movie details", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e(TAG, "Navigation error: " + e.getMessage(), e);
            Toast.makeText(getContext(), "Error: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private NavController getNavController() {
        try {
            // Method 1: Try from MainActivity
            MainActivity mainActivity = (MainActivity) requireActivity();
            NavController navController = mainActivity.getNavController();
            if (navController != null) {
                return navController;
            }
        } catch (Exception e) {
            Log.w(TAG, "Failed to get NavController from MainActivity: " + e.getMessage());
        }

        try {
            // Method 2: Try from view
            View view = getView();
            if (view != null) {
                return Navigation.findNavController(view);
            }
        } catch (Exception e) {
            Log.w(TAG, "Failed to get NavController from view: " + e.getMessage());
        }

        try {
            // Method 3: Try from activity
            return Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        } catch (Exception e) {
            Log.w(TAG, "Failed to get NavController from activity: " + e.getMessage());
        }

        return null;
    }

    @Override
    public void onFavoriteClick(Movie movie, int position) {
        if (movie != null) {
            // Persist toggle
            movieViewModel.toggleFavorite(movie);
            // Update the clicked item (adapter already optimistically toggled)
            adapter.notifyItemChanged(position);

            boolean nowFavorite = movie.isFavorite();
            String message = nowFavorite ? "Added to favorites" : "Removed from favorites";
            View anchor = getView() != null ? getView() : recyclerView;
            if (anchor != null) {
                Snackbar.make(anchor, message, Snackbar.LENGTH_LONG)
                        .setAction("Undo", v -> {
                            // Revert toggle
                            movie.setFavorite(!movie.isFavorite());
                            adapter.notifyItemChanged(position);
                            movieViewModel.toggleFavorite(movie);
                        })
                        .show();
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}