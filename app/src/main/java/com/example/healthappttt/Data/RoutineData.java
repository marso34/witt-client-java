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
    private String startTime;

    @SerializedName("endTime")
    private String endTime;

    public RoutineData(int userID, int dayOfWeek, int cat, String startTime, String endTime) {
        this.userID = userID;
        this.dayOfWeek = dayOfWeek;
        this.cat = cat;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
