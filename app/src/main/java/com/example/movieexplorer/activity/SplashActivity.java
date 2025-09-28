package com.example.movieexplorer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movieexplorer.R;
import com.example.movieexplorer.service.AuthService;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Fade in developer name
        TextView devName = findViewById(R.id.developerName);
        if (devName != null) {
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
            devName.startAnimation(fadeIn);
        }

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