package com.finalandroid.project.hotelbooking.modelClass;

import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("message")
    private String message;
    @SerializedName("error")
    private Boolean error;

    public Result(String message, Boolean error) {
        this.message = message;
        this.error = error;
    }

    public Result(String message) {

        this.message = message;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }
}