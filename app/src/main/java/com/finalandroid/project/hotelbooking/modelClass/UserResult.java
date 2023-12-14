package com.finalandroid.project.hotelbooking.modelClass;

import com.google.gson.annotations.SerializedName;

public class UserResult {

    @SerializedName("message")
    private String message;

    @SerializedName("error")
    private Boolean error;

    @SerializedName("user")
    private User user;

    public UserResult(String message, boolean error, User user) {
        this.message = message;
        this.error = error;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
