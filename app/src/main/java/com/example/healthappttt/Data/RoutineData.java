package com.example.healthappttt.Data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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

    @SerializedName("exercises")
    private List<RoutineExerciseData> exercises;

    public RoutineData(int userID, int dayOfWeek, int cat, String startTime, String endTime, List<RoutineExerciseData> exercises) {
        this.userID = userID;
        this.dayOfWeek = dayOfWeek;
        this.cat = cat;
        this.startTime = startTime;
        this.endTime = endTime;
        this.exercises = exercises;
    }
}
