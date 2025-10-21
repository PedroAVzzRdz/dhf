package com.example.dulcehorno.network.repository;

import android.content.Context;

import com.example.dulcehorno.MyApp;
import com.example.dulcehorno.model.Receipt;
import com.example.dulcehorno.network.ApiClient;
import com.google.gson.Gson;

import okhttp3.Callback;
import okhttp3.Request;

public class UserRepository {

    private final ApiClient apiClient;
    private final Gson gson = new Gson();

    public UserRepository(Context context) {
        this.apiClient = ApiClient.getInstance(context);
    }

    public void getUserProfile(Callback callback) {
        String url = MyApp.getInstance().getBaseUrl() + "profile";
        // withAuth=true agrega el JWT automáticamente
        apiClient.get(url, true, callback);
    }

    public void getUserReceipts(Callback callback){
        String url = MyApp.getInstance().getBaseUrl() + "receipts";
        apiClient.get(url,true,callback);
    }

    public void addUserReceipt(Receipt request, Callback callback){
        String url = MyApp.getInstance().getBaseUrl() + "receipts";
        String jsonBody = gson.toJson(request);
        apiClient.post(url,jsonBody,true,callback);
    }

    public void getUserReceiptsCount(Callback callback){
        String url = MyApp.getInstance().getBaseUrl() + "receipts/count";
        apiClient.get(url,true,callback);
    }
}
