package com.finalandroid.project.hotelbooking.modelClass;

import com.google.gson.annotations.SerializedName;

public class BookedRoomResult {


    @SerializedName("idUser")
    private int userId;
    @SerializedName("username")
    private String username;
    @SerializedName("numDays")
    private String numDays;
    @SerializedName("roomImg")
    private String roomImg;

    public BookedRoomResult(int userId, String username, String numDays, String roomImg) {
        this.userId = userId;
        this.username = username;
        this.numDays = numDays;
        this.roomImg = roomImg;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNumDays() {
        return numDays;
    }

    public void setNumDays(String numDays) {
        this.numDays = numDays;
    }

    public String getRoomImg() {
        return roomImg;
    }

    public void setRoomImg(String roomImg) {
        this.roomImg = roomImg;
    }
}