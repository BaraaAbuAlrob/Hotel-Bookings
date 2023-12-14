package com.finalandroid.project.hotelbooking.modelClass;

import com.google.gson.annotations.SerializedName;

public class Hotel {

    @SerializedName("id")
    private final int id;
    @SerializedName("hotelOwnerId")
    private final int hotelOwnerId;
    @SerializedName("hotelOwnerName")
    private String hotelOwnerName;
    @SerializedName("img")
    private String img;
    @SerializedName("name")
    private String name;
    @SerializedName("location")
    private String location;

    public Hotel(int id, int hotelOwnerId, String hotelOwnerName, String img, String name, String location) {
        this.id = id;
        this.hotelOwnerId = hotelOwnerId;
        this.hotelOwnerName = hotelOwnerName;
        this.img = img;
        this.name = name;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public int getHotelOwnerId() {
        return hotelOwnerId;
    }

    public String getHotelOwnerName() {
        return hotelOwnerName;
    }

    public void setHotelOwnerName(String hotelOwnerName) {
        this.hotelOwnerName = hotelOwnerName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
