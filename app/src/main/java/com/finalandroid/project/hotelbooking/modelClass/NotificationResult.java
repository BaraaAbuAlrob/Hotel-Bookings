package com.finalandroid.project.hotelbooking.modelClass;

import com.google.gson.annotations.SerializedName;

public class NotificationResult {

    @SerializedName("message")
    private String message;

    @SerializedName("error")
    private Boolean error;

    @SerializedName("notification")
    private NotificationModel notification;

    public NotificationResult(String message, Boolean error, NotificationModel notification) {

        this.message = message;
        this.error = error;
        this.notification = notification;
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

    public NotificationModel getNotification() {
        return notification;
    }

    public void setNotification(NotificationModel notification) {
        this.notification = notification;
    }
}
