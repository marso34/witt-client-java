package com.example.healthappttt.Data;

import com.google.gson.annotations.SerializedName;

public class RoutineData {
    @SerializedName("userID")
    private int userID;

    @SerializedName("dayOfWeek")
    private int dayOfWeek;

    @SerializedName("cat")
    private int cat;

    @SerializedName("startTime")
    private int startTime;

    @SerializedName("endTime")
    private int endTime;

    public RoutineData(int userID, int dayOfWeek, int cat, int startTime, int endTime) {
        this.userID = userID;
        this.dayOfWeek = dayOfWeek;
        this.cat = cat;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
