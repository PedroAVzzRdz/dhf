package com.example.dulcehorno;

import com.example.dulcehorno.model.UserProfileResponse;

public class UserSession {
    private static UserSession instance;
    private UserProfileResponse userProfile;

    private UserSession() {}

    public static synchronized UserSession getInstance() {
        if (instance == null) instance = new UserSession();
        return instance;
    }

    public void setUserProfile(UserProfileResponse profile) {
        this.userProfile = profile;
    }

    public UserProfileResponse getUserProfile() {
        return userProfile;
    }

    public boolean hasUserProfile() {
        return userProfile != null;
    }

    public void clear() {
        userProfile = null;
    }
}
