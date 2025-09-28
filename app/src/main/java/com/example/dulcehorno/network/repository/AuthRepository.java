package com.example.dulcehorno.network.repository;

import android.content.Context;

import com.example.dulcehorno.MyApp;
import com.example.dulcehorno.network.ApiClient;
import com.example.dulcehorno.session.SessionManager;
import com.example.dulcehorno.network.request.LoginRequest;
import com.google.gson.Gson;

import okhttp3.Callback;

public class AuthRepository {
    private final ApiClient apiClient;
    private final Gson gson;
    private final SessionManager sessionManager;

    public AuthRepository(Context context) {
        this.apiClient = ApiClient.getInstance(context);
        this.gson = new Gson();
        this.sessionManager = new SessionManager(context);
    }

    public void login(LoginRequest request, Callback callback) {
        String url = MyApp.getInstance().getBaseUrl() + "login";
        String jsonBody = gson.toJson(request);
        apiClient.post(url, jsonBody, false, callback);
    }

    public void saveToken(String token) {
        sessionManager.saveToken(token);
    }

    public String getToken() {
        return sessionManager.getToken();
    }
}
