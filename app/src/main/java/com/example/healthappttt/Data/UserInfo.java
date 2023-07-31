package com.example.healthappttt.Data;


import com.google.gson.annotations.SerializedName;

public class UserInfo {
    @SerializedName("UserKey")
    private int userKey;
    @SerializedName("Name")
    private String name;
    @SerializedName("GymName")
    private String gymName;
    @SerializedName("GymAdress")
    private String gymAdress;
    @SerializedName("Latitude")
    private double latitude;
    @SerializedName("Longitude")
    private double longitude;
    @SerializedName("RoutineCategory")
    private int routineCategory;
    @SerializedName("Time")
    private int time;
    @SerializedName("DayOfWeek")
    private int dayOfWeek;



    private Integer distance = 0;
    // Getters and Setters
    public int getUserKey() {
        return userKey;
    }
    public void setDistance(Integer D){
        distance = D;
    }
    public Integer getDistance(){
        return distance;
    }

    public void setUserKey(int userKey) {
        this.userKey = userKey;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getGymName() {
        return gymName;
    }
    public void setGymName(String gymName) {
        this.gymName = gymName;
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
    public int getRoutineCategory() {
        return routineCategory;
    }
    public void setRoutineCategory(int routineCategory) {
        this.routineCategory = routineCategory;
    }
    public int getTime() { return this.time; };

    public void setTime(int time) {
        this.time = time;
    }
    public int getDayOfWeek() {
        return dayOfWeek;
    }
    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setGymAdress(String gymAdress) {
        this.gymAdress = gymAdress;
    }

    public String getGymAdress() {
        return gymAdress;
    }
}