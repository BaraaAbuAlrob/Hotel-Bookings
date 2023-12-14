package com.finalandroid.project.hotelbooking.modelClass;

import com.google.gson.annotations.SerializedName;

public class Room {

    @SerializedName("id")
    private int id;
    @SerializedName("hotel_id")
    private int hotelId;
    @SerializedName("img")
    private String img;
    @SerializedName("num_days")
    private String numDays;
    @SerializedName("price_night")
    private  String priceNight;
    @SerializedName("description")
    private  String description;

    public Room(String img, String numDays, String priceNight) {
        this.img = img;
        this.numDays = numDays;
        this.priceNight = priceNight;
    }

    public Room(int id, int hotelId, String img, String numDays, String priceNight, String description) {
        this.id = id;
        this.hotelId = hotelId;
        this.img = img;
        this.numDays = numDays;
        this.priceNight = priceNight;
        this.description = description;
    }

    public int getHotelId() {
        return hotelId;
    }

//    public void setHotelId(int hotelId) {
//        this.hotelId = hotelId;
//    }

    public int getId() {
        return id;
    }

//    public void setId(int id) {
//        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}