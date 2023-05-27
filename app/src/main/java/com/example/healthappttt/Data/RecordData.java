package com.example.healthappttt.Data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RecordData {
    @SerializedName("ID")
    private int ID;

    @SerializedName("userID")
    private int userID;

    @SerializedName("oUserID")
    private int oUserID;

    @SerializedName("cat")
    private int cat;

    @SerializedName("startTime")
    private String startTime;

    @SerializedName("endTime")
    private String endTime;

    @SerializedName("runTime")
    private String runTime;

    @SerializedName("promiseID")
    private int promiseID;

    @SerializedName("exercises")
    private List<ExerciseData> exercises;

    public RecordData(int ID, int userID, int oUserID, int cat, String startTime, String endTime, String runTime, int promiseID, List<ExerciseData> exercises) {
        this.ID = ID;
        this.userID = userID;
        this.oUserID = oUserID;
        this.cat = cat;
        this.startTime = startTime;
        this.endTime = endTime;
        this.runTime = runTime;
        this.promiseID = promiseID;
        this.exercises = exercises;
    }

    public RecordData(int userID, int oUserID, int cat, String startTime, String endTime, String runTime, int promiseID, List<ExerciseData> exercises) {
        this.ID = ID;
        this.userID = userID;
        this.oUserID = oUserID;
        this.cat = cat;
        this.startTime = startTime;
        this.endTime = endTime;
        this.runTime = runTime;
        this.promiseID = promiseID;
        this.exercises = exercises;
    }

    public RecordData(int userID, int cat, String startTime, String endTime, String runTime, List<ExerciseData> exercises) {
        this.ID = ID;
        this.userID = userID;
        this.cat = cat;
        this.startTime = startTime;
        this.endTime = endTime;
        this.runTime = runTime;
        this.exercises = exercises;
    }

}
