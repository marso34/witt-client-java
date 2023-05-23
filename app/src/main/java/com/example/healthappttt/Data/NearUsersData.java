package com.example.healthappttt.Data;

import com.google.gson.annotations.SerializedName;

public class NearUsersData {
    @SerializedName("dayOfWeek")
    private int dayOfWeek;
    @SerializedName("currentUser")
    private int currentUser;

    public NearUsersData(int dayOfWeek, int currentUser) {
        this.dayOfWeek = dayOfWeek;
        this.currentUser = currentUser;
    }
    public int getDayOfWeek() {
        return dayOfWeek;
    }
    public int getCurrentUser() {
        return currentUser;
    }
}

