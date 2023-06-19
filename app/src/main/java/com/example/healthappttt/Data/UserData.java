package com.example.healthappttt.Data;

import com.google.gson.annotations.SerializedName;

public class UserData {
    @SerializedName("email")
    private String email;
    @SerializedName("squatValue")
    private int squatValue;

    @SerializedName("benchValue")
    private int benchValue;

    @SerializedName("deadliftValue")
    private int deadliftValue;

    @SerializedName("height")
    private int height;

    @SerializedName("weight")
    private int weight;

    @SerializedName("name")
    private String name;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("loName")
    private String loName;

    @SerializedName("ip")
    private String ip;

    public UserData(String email, String name,int squatValue, int benchValue, int deadliftValue, int height, int weight, double latitude, double longitude, String loName,) {
        this.email = email;
        this.name = name;
        this.squatValue = squatValue;
        this.benchValue = benchValue;
        this.deadliftValue = deadliftValue;
        this.height = height;
        this.weight = weight;
        this.ip = ip
        this.latitude = latitude;
        this.longitude = longitude;
        this.loName = loName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    // Getters and Setters

    public int getSquatValue() {
        return squatValue;
    }

    public void setSquatValue(int squatValue) {
        this.squatValue = squatValue;
    }

    public int getBenchValue() {
        return benchValue;
    }

    public void setBenchValue(int benchValue) {
        this.benchValue = benchValue;
    }

    public int getDeadliftValue() {
        return deadliftValue;
    }

    public void setDeadliftValue(int deadliftValue) {
        this.deadliftValue = deadliftValue;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLoName() {
        return loName;
    }

    public void setLoName(String loName) {
        this.loName = loName;
    }

}