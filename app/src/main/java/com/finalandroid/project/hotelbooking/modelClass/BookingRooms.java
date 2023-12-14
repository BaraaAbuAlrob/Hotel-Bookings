package com.finalandroid.project.hotelbooking.modelClass;

import com.google.gson.annotations.SerializedName;

public class BookingRooms {

    @SerializedName("roomId")
    int roomId;
    @SerializedName("img")
    private String img;
    @SerializedName("num_days")
    private String numDays;
    @SerializedName("price_night")
    private String priceNight;

    public BookingRooms(int roomId, String img, String numDays, String priceNight, String message, Boolean error) {
        this.roomId = roomId;
        this.img = img;
        this.numDays = numDays;
        this.priceNight = priceNight;
    }

    public int getRoomId() {
        return roomId;
    }

//    public void setRoomId(int roomId) {
//        this.roomId = roomId;
//    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getNumDays() {
        return numDays;
    }

    public void setNumDays(String numDays) {
        this.numDays = numDays;
    }

    public String getPriceNight() {
        return priceNight;
    }

    public void setPriceNight(String priceNight) {
        this.priceNight = priceNight;
    }
}