package com.example.movieexplorer.navigation;

import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;

import com.example.movieexplorer.R;

public class NavGraphHelper {

    public static void setupNavigationGraph(NavController navController) {
        // This class helps with programmatic navigation setup if needed
    }

    public static NavGraph createNavGraph(NavInflater inflater) {
        return inflater.inflate(R.navigation.nav_graph);
    }

    public static class Destinations {
        public static final int HOME = R.id.navigation_home;
        public static final int SEARCH = R.id.navigation_search;
        public static final int FAVORITES = R.id.navigation_favorites;
        public static final int PROFILE = R.id.navigation_profile;
        public static final int DETAIL = R.id.navigation_detail;
    }

    public static class Actions {
        public static final int HOME_TO_DETAIL = R.id.action_home_to_detail;
        public static final int SEARCH_TO_DETAIL = R.id.action_search_to_detail;
        public static final int FAVORITES_TO_DETAIL = R.id.action_favorites_to_detail;
    }
}