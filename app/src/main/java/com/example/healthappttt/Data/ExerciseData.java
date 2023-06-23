package com.example.healthappttt.Data;

import com.google.gson.annotations.SerializedName;

public class ExerciseData {
    @SerializedName("ID")
    private int ID;
    @SerializedName("parentID")
    private int parentID;

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


    public ExerciseData(int parentID, String exerciseName, int cat, int setOrTime, int volume, int cntOrDis, int index) {
        this.parentID = parentID;
        this.exerciseName = exerciseName;
        this.cat = cat;
        this.setOrTime = setOrTime;
        this.volume = volume;
        this.cntOrDis = cntOrDis;
        this.index = index;
    }

    public ExerciseData(int ID, int parentID, String exerciseName, int cat, int setOrTime, int volume, int cntOrDis, int index) {
        this.ID = ID;
        this.parentID = parentID;
        this.exerciseName = exerciseName;
        this.cat = cat;
        this.setOrTime = setOrTime;
        this.volume = volume;
        this.cntOrDis = cntOrDis;
        this.index = index;
    }

    public ExerciseData(String exerciseName, int cat, int setOrTime, int volume, int cntOrDis, int index) {
        this.exerciseName = exerciseName;
        this.cat = cat;
        this.setOrTime = setOrTime;
        this.volume = volume;
        this.cntOrDis = cntOrDis;
        this.index = index;
    }
}
