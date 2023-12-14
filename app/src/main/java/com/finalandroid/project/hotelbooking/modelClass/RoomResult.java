package com.finalandroid.project.hotelbooking.modelClass;

import com.google.gson.annotations.SerializedName;

public class RoomResult {

    @SerializedName("message")
    private String message;
    @SerializedName("error")
    private Boolean error;
    @SerializedName("room")
    private Room room;

    public RoomResult(String message, Boolean error, Room hotel) {
        this.message = message;
        this.error = error;
        this.room = hotel;
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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}