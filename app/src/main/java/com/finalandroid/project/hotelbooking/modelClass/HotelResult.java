package com.finalandroid.project.hotelbooking.modelClass;

import com.google.gson.annotations.SerializedName;

public class HotelResult {

    @SerializedName("message")
    private String message;
    @SerializedName("error")
    private Boolean error;
    @SerializedName("hotel")
    private Hotel hotel;

    public HotelResult(String message, Boolean error, Hotel hotel) {
        this.message = message;
        this.error = error;
        this.hotel = hotel;
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

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
}
