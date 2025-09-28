package com.example.movieexplorer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movieexplorer.R;
import com.example.movieexplorer.service.AuthService;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Shorter delay for better UX
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            AuthService authService = AuthService.getInstance();
            Intent intent;
            
            if (authService.isUserLoggedIn()) {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            
            startActivity(intent);
            finish();
        }, 1200);
    }
}