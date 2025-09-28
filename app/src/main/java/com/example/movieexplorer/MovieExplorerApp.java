package com.example.movieexplorer;

import android.app.Application;

import com.example.movieexplorer.database.AppDatabase;

public class MovieExplorerApp extends Application {
    private static MovieExplorerApp instance;
    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = AppDatabase.getDatabase(this);
    }

    public static MovieExplorerApp getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
