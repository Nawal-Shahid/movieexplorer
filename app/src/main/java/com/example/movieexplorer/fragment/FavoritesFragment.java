package com.example.movieexplorer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieexplorer.R;
import com.example.movieexplorer.activity.LoginActivity;
import com.example.movieexplorer.adapter.MovieAdapter;
import com.example.movieexplorer.model.Movie;
import com.example.movieexplorer.service.AuthService;
import com.example.movieexplorer.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment implements MovieAdapter.OnMovieClickListener {
    private RecyclerView recyclerView;
    private TextView emptyText;
    private TextView loginPromptText;
    private Button loginButton;
    private MovieAdapter adapter;
    private MovieViewModel movieViewModel;
    private AuthService authService;
    private List<Movie> favoriteMovies = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authService = AuthService.getInstance();
        
        if (authService.isUserLoggedIn()) {
            setupRecyclerView();
            setupViewModel();
        } else {
            showLoginPrompt();
        }
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        emptyText = view.findViewById(R.id.emptyText);
        loginPromptText = view.findViewById(R.id.loginPromptText);
        loginButton = view.findViewById(R.id.loginButton);
    }

    private void setupRecyclerView() {
        adapter = new MovieAdapter(favoriteMovies, this, isDarkMode());
        int span = getResources().getInteger(R.integer.grid_span_count);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), span));
        recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        movieViewModel = new ViewModelProvider(requireActivity()).get(MovieViewModel.class);

        movieViewModel.getFavoriteMovies().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null && !movies.isEmpty()) {
                favoriteMovies.clear();
                favoriteMovies.addAll(movies);
                adapter.notifyDataSetChanged();
                emptyText.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                favoriteMovies.clear();
                adapter.notifyDataSetChanged();
                emptyText.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
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
        if (authService.isUserLoggedIn()) {
            movieViewModel.toggleFavorite(movie);
            // Item will be removed from the list by LiveData observation
        } else {
            showLoginPrompt();
        }
    }

    private void showLoginPrompt() {
        recyclerView.setVisibility(View.GONE);
        emptyText.setVisibility(View.GONE);
        loginPromptText.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.VISIBLE);
        
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });
    }

    private void hideLoginPrompt() {
        loginPromptText.setVisibility(View.GONE);
        loginButton.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}