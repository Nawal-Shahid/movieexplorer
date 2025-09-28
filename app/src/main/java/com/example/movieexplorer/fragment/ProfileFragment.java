package com.example.movieexplorer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.movieexplorer.R;
import com.example.movieexplorer.activity.LoginActivity;
import com.example.movieexplorer.model.User;
import com.example.movieexplorer.service.AuthService;

public class ProfileFragment extends Fragment {
    private TextView userNameText;
    private TextView userEmailText;
    private TextView appNameText;
    private TextView versionText;
    private TextView descriptionText;
    private Button logoutButton;
    private AuthService authService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);
        setupUserInfo();
        return view;
    }

    private void initViews(View view) {
        userNameText = view.findViewById(R.id.userNameText);
        userEmailText = view.findViewById(R.id.userEmailText);
        appNameText = view.findViewById(R.id.appNameText);
        versionText = view.findViewById(R.id.versionText);
        descriptionText = view.findViewById(R.id.descriptionText);
        logoutButton = view.findViewById(R.id.logoutButton);

        authService = AuthService.getInstance();
        setupClickListeners();
    }

    private void setupUserInfo() {
        User currentUser = authService.getCurrentUser();
        if (currentUser != null) {
            userNameText.setText(currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "User");
            userEmailText.setText(currentUser.getEmail());
        } else {
            userNameText.setText("User");
            userEmailText.setText("Not logged in");
        }

        appNameText.setText("Movie Explorer");
        versionText.setText("Version 1.0");
        descriptionText.setText("Discover and explore your favorite movies with Movie Explorer. Browse popular films, search for specific titles, and save your favorites for later.");
    }

    private void setupClickListeners() {
        if (logoutButton != null) {
            logoutButton.setOnClickListener(v -> logout());
        } else {
            Log.e("ProfileFragment", "Logout button is null, cannot set click listener");
        }
    }

    private void logout() {
        if (authService != null) {
            authService.signOut();
            Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    }
}