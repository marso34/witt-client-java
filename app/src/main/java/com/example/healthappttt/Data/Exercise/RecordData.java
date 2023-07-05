package com.example.healthappttt.Data.Exercise;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecordData implements Serializable {
    @SerializedName("ID")
    private int ID;

    @SerializedName("userID")
    private int userID;

    @SerializedName("oUserID")
    private int oUserID;

    @SerializedName("promiseID")
    private int promiseID;

    @SerializedName("cat")
    private int cat;

    @SerializedName("startTime")
    private String startTime;

    @SerializedName("endTime")
    private String endTime;

    @SerializedName("runTime")
    private String runTime;

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

    public RecordData(int userID, int cat, String startTime, String endTime, String runTime) {
        this.ID = ID;
        this.userID = userID;
        this.cat = cat;
        this.startTime = startTime;
        this.endTime = endTime;
        this.runTime = runTime;
    }


    public int getID()                                      { return this.ID; }
    public int getUserID()                                  { return this.userID; }
    public int getoUserID()                                 { return this.oUserID; }
    public int getPromiseID()                               { return this.promiseID; }
    public int getCat()                                     { return this.cat; }
    public String getStartTime()                            { return this.startTime; }
    public String getEndTime()                              { return this.endTime; }
    public String getRunTime()                              { return this.runTime; }
    public ArrayList<ExerciseData> getExercises()           { return (ArrayList<ExerciseData>) this.exercises; }

    public void setID(int ID)                               { this.ID = ID; }
    public void setUserID(int userID)                       { this.userID = userID; }
    public void setoUserID(int oUserID)                     { this.oUserID = oUserID; }
    public void setPromiseID(int promiseID)                 { this.promiseID = promiseID; }
    public void setCat(int cat)                             { this.cat = cat; }
    public void setStartTime(String startTime)              { this.startTime = startTime; }
    public void setEndTime(String endTime)                  { this.endTime = endTime; }
    public void setRunTime(String runTime)                  { this.runTime = runTime; }
    public void setExercises(ArrayList<ExerciseData> exercises)  { this.exercises = exercises; }


}
