package com.example.dulcehorno.network.repository;

import android.content.Context;

import com.example.dulcehorno.MyApp;
import com.example.dulcehorno.network.ApiClient;
import com.example.dulcehorno.network.request.LoginRequest;

import okhttp3.Callback;

public class ProductsRepository {

    private final ApiClient apiClient;

    public ProductsRepository(Context context) {
        this.apiClient = ApiClient.getInstance(context);
    }

    public void getProducts(Callback callback) {
        String url = MyApp.getInstance().getBaseUrl() + "products";
        apiClient.get(url, false, callback);
    }
}
