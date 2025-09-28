package com.example.dulcehorno;

import android.app.Application;

public class MyApp extends Application {
    private static MyApp instance;

    // Base URL de tu API
    private final String BASE_URL = "http://10.0.2.2:3000/api/";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApp getInstance() {
        return instance;
    }

    public String getBaseUrl() {
        return BASE_URL;
    }
}
