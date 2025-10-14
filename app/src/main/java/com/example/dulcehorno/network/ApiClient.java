package com.example.dulcehorno.network;

import android.content.Context;

import com.example.dulcehorno.session.SessionManager;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ApiClient {

    private static ApiClient instance;
    private OkHttpClient client;
    private SessionManager sessionManager;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private ApiClient(Context context) {
        client = new OkHttpClient();
        sessionManager = new SessionManager(context);
    }

    public static ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context);
        }
        return instance;
    }

    // Método genérico para POST con token opcional
    public void post(String url, String jsonBody, boolean withAuth, Callback callback) {
        if (withAuth) {
            String token = sessionManager.getToken();
            if (token == null) {
                // Avisar inmediatamente que no hay token
                callback.onFailure(null, new IOException("No hay token disponible"));
                return;
            }
        }

        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request.Builder builder = new Request.Builder()
                .url(url)
                .post(body);

        if (withAuth) {
            String token = sessionManager.getToken();
            builder.addHeader("Authorization", "Bearer " + token);
        }

        Request request = builder.build();
        client.newCall(request).enqueue(callback);
    }


    public void get(String url, boolean withAuth, Callback callback) {
        Request.Builder builder = new Request.Builder().url(url).get();

        if (withAuth) {
            String token = sessionManager.getToken();
            if (token != null) {
                builder.addHeader("Authorization", "Bearer " + token);
            } else {
                // Opcional: avisar inmediatamente que no hay token
                callback.onFailure(null, new IOException("No hay token disponible"));
                return;
            }
        }

        Request request = builder.build();
        client.newCall(request).enqueue(callback);
    }

}
