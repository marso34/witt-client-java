package com.example.healthappttt.Data;

import com.google.gson.annotations.SerializedName;

public class LocInfo {
    @SerializedName("userLat")
    private double userLat;

    @SerializedName("userLon")
    private double userLon;

    @SerializedName("gymNm")
    private String gymNm;

    @SerializedName("gymLat")
    private double gymLat;

    @SerializedName("gymLon")
    private double gymLon;

    public LocInfo(double userLat, double userLon, String gymNm, double gymLat, double gymLon) {
        this.userLat = userLat;
        this.userLon = userLon;
        this.gymNm = gymNm;
        this.gymLat = gymLat;
        this.gymLon = gymLon;
    }
    public double getUserLat() {
        return userLat;
    }

    public void setUserLat(double userLat) {
        this.userLat = userLat;
    }

    public double getUserLon() {
        return userLon;
    }

    public void setUserLon(double userLon) {
        this.userLon = userLon;
    }

    public String getGymNm() {
        return gymNm;
    }

    public void setGymNm(String gymNm) {
        this.gymNm = gymNm;
    }

    public double getGymLat() {
        return gymLat;
    }

    public void setGymLat(double gymLat) {
        this.gymLat = gymLat;
    }

    public double getGymLon() {
        return gymLon;
    }

    public void setGymLon(double gymLon) {
        this.gymLon = gymLon;
    }
}