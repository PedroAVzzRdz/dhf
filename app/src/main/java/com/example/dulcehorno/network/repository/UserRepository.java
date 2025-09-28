package com.example.dulcehorno.network.repository;

import android.content.Context;

import com.example.dulcehorno.MyApp;
import com.example.dulcehorno.network.ApiClient;

import okhttp3.Callback;

public class UserRepository {

    private final ApiClient apiClient;

    public UserRepository(Context context) {
        this.apiClient = ApiClient.getInstance(context);
    }

    // Método para obtener datos del usuario
    public void getUserProfile(Callback callback) {
        String url = MyApp.getInstance().getBaseUrl() + "profile"; // endpoint protegido
        // withAuth=true agrega el JWT automáticamente
        apiClient.get(url, true, callback);
    }
}
