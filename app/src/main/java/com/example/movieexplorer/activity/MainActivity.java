package com.example.movieexplorer.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.movieexplorer.R;
import com.example.movieexplorer.service.AuthService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {
    private NavController navController;
    private static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        // Check authentication status
        AuthService authService = AuthService.getInstance();
        if (!authService.isUserLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        setupNavigation();
    }

    private void setupNavigation() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);

        // Configure top level destinations
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_favorites,
                R.id.navigation_profile)
                .build();

        // Get NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            // Use the MaterialToolbar as the app bar with per-destination labels
            if (toolbar != null) {
                // Optional: let NavController set titles from nav_graph labels
                toolbar.setTitle(null);
                NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
            }

            // Setup BottomNavigationView with NavController
            NavigationUI.setupWithNavController(navView, navController);
        }
    }

    public NavController getNavController() {
        return navController;
    }

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (navController != null) {
            return navController.navigateUp() || super.onSupportNavigateUp();
        }
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }
}