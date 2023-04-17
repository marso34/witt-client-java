package com.example.healthappttt.Data;

import com.google.gson.annotations.SerializedName;

public class RoutineExerciseData {
    @SerializedName("routineID")
    private int routineID;

    @SerializedName("exerciseName")
    private String exerciseName;

    @SerializedName("cat")
    private int cat;

    @SerializedName("setOrTime")
    private int setOrTime;

    @SerializedName("volume")
    private int volume;

    @SerializedName("cntOrDis")
    private int cntOrDis;

    @SerializedName("index")
    private int index;


    public RoutineExerciseData(int routineID, String exerciseName, int cat, int setOrTime, int volume, int cntOrDis, int index) {
        this.routineID = routineID;
        this.exerciseName = exerciseName;
        this.cat = cat;
        this.setOrTime = setOrTime;
        this.volume = volume;
        this.cntOrDis = cntOrDis;
        this.index = index;
    }
}
