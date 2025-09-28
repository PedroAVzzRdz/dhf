package com.example.dulcehorno.network.request;

public class SignupRequest {
    private String email;
    private String username;

    private String password;

    public SignupRequest(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
