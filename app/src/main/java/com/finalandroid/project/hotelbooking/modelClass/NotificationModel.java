package com.finalandroid.project.hotelbooking.modelClass;

import com.google.gson.annotations.SerializedName;

public class NotificationModel {

    @SerializedName("id")
    private final int id;
    @SerializedName("user_id")
    private final int user_id;
    @SerializedName("title")
    private String title;
    @SerializedName("text")
    private String text;
    @SerializedName("date_time")
    private String date_time;

    public NotificationModel(int id, int user_id, String title, String text, String date_time) {

        this.id = id;
        this.user_id = user_id;
        this.title = title;
        this.text = text;
        this.date_time = date_time;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}