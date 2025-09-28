package com.example.movieexplorer.navigation;

import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.NavDirections;

import com.example.movieexplorer.R;

public class AppNavigator {
    private NavController navController;

    public AppNavigator(NavController navController) {
        this.navController = navController;
    }

    public void navigateToHome() {
        if (navController != null) {
            navController.navigate(R.id.navigation_home);
        }
    }

    public void navigateToSearch() {
        if (navController != null) {
            navController.navigate(R.id.navigation_search);
        }
    }

    public void navigateToFavorites() {
        if (navController != null) {
            navController.navigate(R.id.navigation_favorites);
        }
    }

    public void navigateToProfile() {
        if (navController != null) {
            navController.navigate(R.id.navigation_profile);
        }
    }

    public void navigateToMovieDetail(int movieId) {
        if (navController != null && movieId > 0) {
            Bundle bundle = new Bundle();
            bundle.putInt("movie_id", movieId);
            navController.navigate(R.id.navigation_detail, bundle);
        }
    }

    public void navigateToMovieDetail(NavDirections directions) {
        if (navController != null) {
            navController.navigate(directions);
        }
    }

    public void navigateBack() {
        if (navController != null && navController.getCurrentDestination() != null) {
            navController.popBackStack();
        }
    }

    public void navigateToRoot() {
        if (navController != null) {
            navController.popBackStack(R.id.navigation_home, false);
        }
    }

    public boolean canGoBack() {
        return navController != null &&
                navController.getCurrentDestination() != null &&
                navController.getCurrentDestination().getId() != R.id.navigation_home;
    }

    public int getCurrentDestination() {
        return navController != null && navController.getCurrentDestination() != null ?
                navController.getCurrentDestination().getId() : -1;
    }

    public void setNavController(NavController navController) {
        this.navController = navController;
    }
}