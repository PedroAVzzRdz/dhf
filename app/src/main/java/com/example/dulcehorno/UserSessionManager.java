package com.example.dulcehorno;

import com.example.dulcehorno.model.UserProfileResponse;

public class UserSessionManager {
    private static UserSessionManager instance;
    private UserProfileResponse userProfile;

    private UserSessionManager() {}

    public static synchronized UserSessionManager getInstance() {
        if (instance == null) instance = new UserSessionManager();
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
